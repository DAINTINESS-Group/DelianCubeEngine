# Highlights as a First-Class Result Type in DelianCubeEngine

A design for representing **data-analysis highlights** as first-class values in the engine,
following the ER Forum 2025 paper *"A Conceptual Model for Data Analysis Highlights"* (Vassiliadis,
Peralta, Marcel, Gkitsakis, Dougia, El Outa).

Highlights are the result of an analysis pipeline, not something an operator hand-builds. An
operator produces **data** and the **models** the query asked for; **archetype properties** are
tested over that by their **algorithms**; the algorithms attach **scores**; and the meaningful
results that surface are **highlights**. Highlights form a **closed algebra**: a `HighlightSet` can
be pruned, expanded, ordered, combined, and fed back into operators — like relations in SQL.

---

## 1. Concepts (from the paper)

The paper separates a **metamodel** (declarative types — what *can* be highlighted) from a
**model** (materialized instances — what actually *did* highlight for a given dataset).

- **Dataset / Schema** — a set of Facts under a schema of **Measure Types** and **Character Types**.
- **Character** — a dimension member (e.g. `Prague`), typed by a Character Type (a cube `Level`).
- **Archetype Property** *(metamodel)* — a pattern hypothesis (benchmark tendency, mega-contributor,
  trend, modality…) declared over a **Main Measure Role**, a set of **Explanator Roles**, a set of
  candidate **Algorithms**, its **Holistic-Highlight Score Types**, and its **Elementary-Highlight
  Roles**.
- **Algorithm** *(metamodel)* — a candidate way to test an archetype property. Its **execution**
  produces a **Result** (a verdict + auxiliary metrics).
- **Holistic Highlight** *(model)* — a testimony that an archetype property holds over the *entire*
  dataset. It materializes an archetype property via an **Algorithm Execution**, over a concrete
  Main Measure and Explanators, carrying the result, scores, and the **Elementary Highlights** that
  detail it.
- **Elementary Highlight** *(model)* — a specific fact (characters + a measure value) that stands
  out, playing an **Elementary Highlight Role** (assessed cell, mega-contributor, peak…), with its
  own scores.
- **Score / Score Type** — significance on an ordinal domain (Peculiarity, Novelty, Relevance,
  Surprise; or an enumerated label rank). Significant results are kept by pruning, `|score| ≥ θ`.

---

## 2. Architecture: a two-stage, closed pipeline

```
Stage 1  Operator  →  OperatorResult   the cube query Result (DATA) + the query-requested
                                        AbstractModels that ran over it (ASSESS's benchmark+
                                        delta+labeling; DESCRIBE's USING Rank; ...).

Stage 2  HighlightExtractor            over the OperatorResult, for each candidate ArchetypeProperty:
                                          • the Main Measure Role constraint must be satisfied
                                            (e.g. ADDITIVE vs the query's aggregation)
                                          • pick the first candidate Algorithm that appliesTo it
                                          • run it → ArchetypeResult (verdict + scores + findings)
                                          • build one HolisticHighlight + its ElementaryHighlights
                             │
                             ▼  HighlightSet — closed algebra (every op returns a HighlightSet)
                       prune · expand · topK · union · project
                             │
                             ▼
                       consumed by other operators / rendered to a report
```

Two invariants:
- **Operators don't build highlights.** They produce data and the query-requested models, bundled as
  an `OperatorResult`. The extractor turns that into highlights.
- **Models live in the `OperatorResult` and are consumed, not rebuilt.** An algorithm reads the
  model it needs (e.g. `LabelDistributionAlgorithm` reads the `AssessModel`); it never recomputes it
  from the raw data.

---

## 3. The layers and their packages

The paper's metamodel/model split is the package split. `AbstractModel` (the engine's existing model
base in `model.abstracts`) is *the* model abstraction; there is no parallel highlight-local model
interface.

```
result.highlights            core / pipeline
  OperatorResult · HighlightExtractor · HighlightSet · CubeSchemaResolver

result.highlights.metamodel  declarative types (the paper's metamodel layer)
  ArchetypeProperty · Algorithm · AlgorithmParams
  MainMeasureRole · ExplanatorRole · ExplanatorConstraint · MeasureConstraint · Additivity
  ElementaryHighlightRole · CharacterRole · MeasureRole
  ScoreType · LabelScoreType · InterestingnessFacet

result.highlights.instance   materialized highlights (the paper's model layer)
  Highlight · HolisticHighlight · ElementaryHighlight
  AlgorithmExecution · AlgorithmResult · ArchetypeResult · ScoredFinding
  Character · MeasureValue · Score

result.highlights.archetypes concrete, reusable archetype library
  MegaContributorArchetype · MarginalContributionAlgorithm
```

Dependency direction: `instance → metamodel`, `core → both`. The single metamodel→instance edge is
`Algorithm → ArchetypeResult`, which is real and paper-faithful: an algorithm execution *produces* a
result. Operator-specific archetypes are **not** in the library — ASSESS's `BenchmarkTendencyArchetype`
and `LabelDistributionAlgorithm` live in the `assess` package.

### 3.1 The metamodel: archetype properties utilize algorithms

An archetype property is a declarative bundle of roles and candidate algorithms; it owns no
computation itself.

```java
// result/highlights/metamodel/ArchetypeProperty.java
public final class ArchetypeProperty {
    public final String name;
    public final MainMeasureRole mainMeasureRole;            // name + MeasureConstraint (e.g. ADDITIVE)
    public final List<ExplanatorRole> explanatorRoles;       // name + ExplanatorConstraint (ANY, TIME_RELATED)
    public final List<Algorithm> candidateAlgorithms;        // how it may be tested
    public final List<ScoreType> hhScoreTypes;               // score types of the holistic highlight
    public final List<ElementaryHighlightRole> elementaryHighlightRoles;
}
```

```java
// result/highlights/metamodel/Algorithm.java
public interface Algorithm {
    String name();
    AlgorithmParams params();
    boolean appliesTo(OperatorResult context);   // data-driven applicability (e.g. needs AssessModel)
    ArchetypeResult run(OperatorResult context);  // consumes models in the context; produces the outcome
}
```

`MeasureConstraint.accepts(aggregation)` is the enforced handle for additivity: `ADDITIVE` defers to
`Additivity.isAdditive(...)` (sum/count yes; max/min/avg no), so an archetype whose main measure must
sum-to-a-total (mega-contributor) is skipped for a `max(...)` query. `ANY` accepts anything.

### 3.2 The instance layer: the algorithm outcome and the highlights

```java
// result/highlights/instance/ArchetypeResult.java — what Algorithm.run returns
public final class ArchetypeResult {
    public final AlgorithmExecution execution;    // name + params + AlgorithmResult (verdict + metrics)
    public final List<Score> holisticScores;      // scores for the holistic
    public final List<ScoredFinding> elementary;  // salient cells/members, each with its scores
}
```

```java
// result/highlights/instance/Highlight.java  (carries dataset + scores)
public abstract class Highlight { public Result getDataset(); public List<Score> getScores(); }

public class HolisticHighlight extends Highlight {          // a dataset-level testimony
    public final ArchetypeProperty archetype; public final AlgorithmExecution execution;
    public final Measure mainMeasure; public final List<Level> explanators;
    public final AlgorithmResult result; public final List<ElementaryHighlight> elementary;
}
public class ElementaryHighlight extends Highlight {        // a cell-level fact
    public final List<Character> characters; public final MeasureValue measureValue;
    public final ElementaryHighlightRole role;
}
```

A `HolisticHighlight` always materializes an archetype property via an execution — archetype-less
holistics are rejected in the constructor.

### 3.3 Scores (significance)

`Score` / `ScoreType` are an ordinal facet plus a value (arithmetic, or an enumerated label rank).
Arithmetic facets come from `InterestingnessFacet` (Peculiarity/Novelty/Relevance/Surprise);
enumerated facets from `LabelScoreType` (ordered labels → ranks). There is **no separate `Scorer`
stage**: the algorithm attaches the scores it computed — holistic scores on the `ArchetypeResult`,
per-finding scores on each `ScoredFinding`.

```java
public interface ScoreType { String name(); int compare(double a, double b); }
public enum InterestingnessFacet implements ScoreType { PECULIARITY, NOVELTY, RELEVANCE, SURPRISE; ... }
public final class LabelScoreType implements ScoreType { /* ordered labels → ranks */ }
public final class Score { public final ScoreType type; public final double value; public final String label; }
```

### 3.4 HighlightSet (the relation that flows)

A collection of highlights with a closed algebra; the value that flows out of Stage 2.

```java
// result/highlights/HighlightSet.java
public final class HighlightSet {
    public HighlightSet prune(Predicate<Highlight> keep);              // σ / WHERE
    public HighlightSet pruneByScore(String scoreType, double theta);  // |score| ≥ θ
    public HighlightSet expand(Function<Highlight,List<Highlight>> f); // holistic → its elementary, etc.
    public HighlightSet topK(String scoreType, int k);                 // order by score, take k
    public HighlightSet union(HighlightSet other);
    public HighlightSet project(Predicate<Highlight> kindFilter);

    // normalized (1NF) relational views for the SQL-like surface (see §4)
    public List<String[]> asHighlightRelation();
    public List<String[]> asScoreRelation();
    public List<String[]> asCharacterRelation();
}
```

### 3.5 The extractor (Stage 2)

Turns an `OperatorResult` and a list of candidate archetype properties into a `HighlightSet`. This
is where highlights are born — not in the operator. Cube-schema resolution (main measure,
explanators, and binding `(dimensionIndices, members)` → typed `Character`s) is delegated to
`CubeSchemaResolver`.

```java
// result/highlights/HighlightExtractor.java
public HighlightSet extract(OperatorResult result, List<ArchetypeProperty> candidates,
                            CubeSchemaResolver schema) {
    List<Highlight> out = new ArrayList<>();
    for (ArchetypeProperty archetype : candidates) {
        if (!measureSatisfies(archetype, result)) continue;         // Main Measure Role constraint
        Algorithm algorithm = applicableAlgorithm(archetype, result); // first candidate that appliesTo
        if (algorithm != null) out.add(buildHolistic(result, archetype, algorithm, schema));
    }
    return new HighlightSet(out);
}
```

---

## 4. The relational shape (SQL-like environment)

To be queryable, prunable, joinable, a `HighlightSet` exposes a normalized schema. Highlights are
heterogeneous and scores/characters are multi-valued, so they flatten into three relations (1NF),
keyed by the highlight's row index:

```
HIGHLIGHT( id, kind, archetype, role, mainMeasure, value, datasetRef )
SCORE(     highlightId, scoreType, value, label )
CHARACTER( highlightId, level, member )
```

- In memory each `Highlight` keeps its scores/characters nested (convenient object graph).
- `HighlightSet` exposes the normalized relations (`asHighlightRelation`, `asScoreRelation`,
  `asCharacterRelation`) as the canonical form for a SQL-like surface.
- `expand` of a holistic into its elementary detail is a join on parentage; `pruneByScore` is `σ`
  over `SCORE`.

---

## 5. The algebra

Closed, composable, fluent:

```java
HighlightSet result =
    extractor.extract(operatorResult, candidates, schema)
             .pruneByScore("PECULIARITY", 0.5)   // keep the significant
             .topK("PECULIARITY", 10)            // the ten most peculiar
             .expand(h -> ((HolisticHighlight) h).elementary);  // drill into their detail
```

| op | relational analogue | use |
|---|---|---|
| `prune` / `pruneByScore` | σ (WHERE) | keep significant / matching highlights |
| `expand` | join / unnest | holistic → its elementary; or generate further tests |
| `topK` / sort | ORDER BY + LIMIT | the most significant |
| `union` | ∪ | merge producers (e.g. two archetypes over one dataset) |
| `project` | π | restrict kind/fields |

A parseable query surface over highlights can layer on later; the fluent API is the first form.

---

## 6. Mapping to the existing engine

| Layer | Existing assets |
|---|---|
| Operator → OperatorResult | `DESCRIBE`/`ANALYZE`/`ASSESS`; `CubeManager` → `Result`; query-requested `AbstractModel`s |
| Metamodel (archetypes) | `MegaContributorArchetype` (+ `MarginalContributionAlgorithm`); ASSESS's `BenchmarkTendencyArchetype` (+ `LabelDistributionAlgorithm`) |
| Instance (highlights) | `result.highlights.instance` |
| Scores | `InterestingnessFacet` (Peculiarity/Novelty/Relevance/Surprise); `LabelScoreType` (labeling schemes) |
| Extractor + HighlightSet | `result.highlights` |
| Schema resolution | `cubemanager.cubebase.Measure` / `Level` via `CubeSchemaResolver` |

---

## 7. ASSESS in this architecture

ASSESS does not build highlights. It produces data and the query-defined model, then hands them to
the extractor.

1. **Stage 1 — OperatorResult:** run the target cube query → `Result`; run `AssessModel(benchmark,
   delta, labeling)` over it (`compute()` fills per-cell deltas + labels). Bundle both as an
   `OperatorResult`.
2. **Candidate archetypes:** `BenchmarkTendencyArchetype.create(orderedLabels)` and
   `MegaContributorArchetype.create()`.
3. **Stage 2 — extractor:**
   - *BenchmarkTendency* (`MainMeasureRole = ANY`) → `LabelDistributionAlgorithm` **reads the
     `AssessModel`** from the `OperatorResult`, computes the label distribution, and emits a
     `HolisticHighlight` (verdict = does a label dominate) with the salient cells as
     `AssessedCell` elementary highlights.
   - *MegaContributor* (`MainMeasureRole = ADDITIVE`) is evaluated only when the query aggregation is
     additive; `MarginalContributionAlgorithm` computes, per dimension, each member's share of the
     total and emits members above the dominance threshold as `MegaContributor` elementary
     highlights.
4. **Report:** `AssessOperator.exportToMD` renders the resulting `HighlightSet` under `## Highlights`.

---

## 8. Extending it

To add an archetype: declare an `ArchetypeProperty` (its roles + score types), implement one or more
`Algorithm`s whose `appliesTo` gates on the models/aggregation present in the `OperatorResult` and
whose `run` consumes those models to produce an `ArchetypeResult`, and add the archetype to the
operator's candidate list. Reusable archetypes go in `result.highlights.archetypes`; operator-specific
ones stay with the operator. No changes to `HighlightExtractor`, `HighlightSet`, or the instance
types are needed.
