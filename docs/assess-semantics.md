# ASSESS: semantics as implemented

This document defines what an ASSESS query computes in DelianCube, step by step, and checks each step against the code and against the two papers it builds on: the original operator (Francia et al., EDBT 2021, "EDBT" below) and the comparison/label-composition paper (Vassiliadis, Marcel, Chanson, DOLAP 2024, "DOLAP" below).

Each point carries a status:

- **holds**: the definition is clear and the code implements it.
- **gap**: the definition is missing, incomplete, or only implicit in the code.
- **defect**: the code does something the definition does not allow, or something wrong.
- **decide**: there are several reasonable definitions and one must be chosen.

Section 11 collects all open points in one list.

---

## 1. Data model

We use the EDBT definitions unchanged (EDBT §2).

- A **cube schema** is C = (H, M): linear hierarchies H, measures M, each measure with an aggregation operator.
- A **cube query** is q = (C0, G, P, M_q): a detailed cube C0, a group-by set G (at most one level per hierarchy), conjunctive selection predicates P (one level each), and measures M_q.
- The result of q is a **derived cube**: a partial function from coordinates γ of G that satisfy P to measure values. It is sparse: a coordinate exists only if some fact rolls up to it.
- A **cell** is c = ⟨γ, t⟩. We write v(c) for the value of the assessed measure at c.

**Restriction (holds).** Predicates in ASSESS are equalities only (`level = 'value'`), see the `predicate` rule in `src/main/java/intentional/assess/syntax/AssessQuery.g:57`. EDBT allows the same. DESCRIBE's richer predicates (`IN`, ranges) are not available in ASSESS.

---

## 2. The query

As accepted by `src/main/java/intentional/assess/syntax/AssessQuery.g:26`:

```
WITH C0 [FOR P] BY G
ASSESS m [AS alias]
[ AGAINST b1 [USING f1], ..., bn [USING fn]  |  USING f ]
LABELS λ
```

- `m` is a measure expression over aggregates, for example `sum(amount) - sum(payments) AS profit` (`AssessQuery.g:118`).
- Each AGAINST entry is a **comparison**: a benchmark b_i together with its own comparison function f_i. With no AGAINST, the query holds one comparison with no benchmark (`src/main/java/intentional/assess/AssessQueryBuilder.java:165`).
- `LABELS λ` names exactly one labeling scheme, shared by all comparisons (`AssessQueryBuilder.java:205`).

The operator then runs one comparison per entry and a consensus over the results (`src/main/java/intentional/assess/AssessOperator.java:55-67`).

**Formal shape.** For a query Q with comparisons (b_1, f_1), …, (b_n, f_n) and scheme λ:

```
T          = [ (C0, G, P, m) ]                          the target cube
L_i        = λ( f_i( T ⋈_{b_i} B_i ) )                  one labeling per comparison
K          = consensus(L_1, ..., L_n)                   when n ≥ 2
ASSESS(Q)  = ( T, L_1, ..., L_n, K )
```

Sections 3–9 define each piece.

---

## 3. Target cube

T is the cube query (C0, G, P, m), translated through the text template in `src/main/java/intentional/assess/CubeManagerAdapter.java:125`. A derived measure is swapped in after translation (`CubeManagerAdapter.java:99-114`), so every benchmark query built from the same template carries the same expression. **Holds.**

---

## 4. Benchmarks and matching

A benchmark is a cube B together with a **matching**: a partial function μ from the cells of T to the cells of B (`src/main/java/intentional/assess/benchmarks/AssessBenchmark.java:13`). A target cell c is **matched** when μ(c) is defined. The benchmark value is v_B(c) = v(μ(c)).

### 4.1 Constant: `AGAINST 500000`

- B has the coordinates of T and the constant value everywhere, so every cell is matched (`src/main/java/intentional/assess/benchmarks/ConstantBenchmark.java:15-18`). **Holds** (EDBT §4.1).

### 4.2 Sibling: `AGAINST district = 'Brno'`

- B = (C0, G, P_B, m), where P_B is P with the predicate on level l_s replaced by `l_s = u_sib` (`src/main/java/intentional/assess/fetch/SerialFetcher.java:58-67`).
- μ(c) is the cell of B whose coordinate equals c's on every level except l_s (`src/main/java/intentional/assess/benchmarks/SiblingBenchmark.java:33-42`).
- Preconditions: l_s ∈ P, which is checked (`SerialFetcher.java:61`), and l_s ∈ G, which is not checked. If l_s is not in G, `locateSiblingField` returns -1 and `List.remove(-1)` throws an index exception (`SiblingBenchmark.java:35`). **Holds**, matching EDBT §4.3. **gap:** the l_s ∈ G precondition should fail with a message.

### 4.3 Past: `AGAINST PAST k`

- Let l_t be the date level in P, with value u. The k previous members u_1, …, u_k of l_t are computed by `DatesHandler.decrementDate` (`src/main/java/intentional/assess/utils/DatesHandler.java:64`). One cube is fetched per member (`SerialFetcher.java:69-85`).
- Cells are matched on every level except l_t. The benchmark value is the **mean** of the matched past values (`src/main/java/intentional/assess/benchmarks/PastBenchmark.java:42-63`):

  ```
  v_B(c) = (1 / |S(c)|) · Σ_{j ∈ S(c)} v(c with l_t := u_j),   S(c) = { j : that past cell exists }
  ```

Open points:

- **decide (deviation from EDBT).** EDBT defines the past benchmark as a time-series *prediction* (`regression` over the pivoted past slices, EDBT §4.3). The code uses the plain mean. Either the paper states the mean as a deliberate, simpler predictor, or a predictor parameter is added (mean, last value, linear trend).
- **gap (missing periods).** The mean runs over the periods where the cell exists, so a cell missing in 2 of 3 past years is compared against one year alone. For additive measures (sum, count) a missing cell arguably means 0. The definition must pick one: "missing = 0" for additive measures, or "missing = unknown" with a minimum-support threshold. A period with no cells at all is dropped silently (`SerialFetcher.java:77`).
- **gap.** l_t ∈ G is required (`PastBenchmark.java:44`), but the requirement is undocumented. Only one date dimension is supported (`CubeManagerAdapter.java:41`).

### 4.4 External: `AGAINST cube.measure`

- Parsed (`AssessQuery.g:98`) but not implemented: every fetcher throws (`SerialFetcher.java:50`). **decide:** implement it (EDBT defines it as equality of coordinates after reconciliation), or scope it out of the paper explicitly.

### 4.5 assess vs assess\*

EDBT defines two variants. `assess` keeps only matched cells (inner join); `assess*` keeps all target cells, with null labels for the unmatched ones (left outer join).

What the code does:

- Each comparison labels **only matched cells**. Holistic transforms and scheme fitting see only matched cells (`src/main/java/intentional/assess/deltas/DeltaScheme.java:197-229`). That is inner-join semantics *per comparison*.
- The result keeps **all target cells** (the `LabeledResult` data is T), and unmatched cells are reported as "had no match". That is outer-join semantics *for the result*.

**decide.** Either add the `assess`/`assess*` keyword pair as in EDBT, or state the current hybrid as the definition: labels are computed on the inner join, and the result is the outer join. With several benchmarks the matched sets differ per comparison, so the choice also fixes which cells the consensus sees (§8).

---

## 5. Comparison (`USING`)

### 5.1 Expressions

`USING` is an expression tree (`src/main/java/intentional/assess/syntax/AssessQuery.g:102`). Every function names its own arguments, and an argument is either an operand or another call:

```
e ::= operand | f(e, e) | h(e) | percOfTotal(e [, e])
operand ::= m | benchmark.m | number
```

- `m` is the target value v(c), written as the measure name or alias; `benchmark.m` is the benchmark value v_B(c); a number is a constant. A name that is not the carried measure fails the query, and so does `benchmark.m` in a query without AGAINST (`src/main/java/intentional/assess/AssessQueryBuilder.java:135-155`). **Holds.**
- **Cell-wise functions** f take exactly two arguments and work per cell: `difference` (a − b), `invDifference` (b − a), `absolute` (|a − b|), `ratio` (a / b) (`src/main/java/intentional/assess/deltas/DeltaScheme.java:21-24`).
- **Holistic functions** h take one argument and rewrite its whole column: `minMaxNorm`, `zscore` (sample σ), `rank` (ascending, ties averaged). `percOfTotal(x, y)` computes x / Σ y; without y it divides by the total of the target column, as in EDBT's examples. A holistic function over a constant is rejected (`DeltaScheme.java:158-191`).
- Wrong arities fail at parse time with a message naming the expected form, e.g. "ratio compares two values, e.g. ratio(total, benchmark.total)".

Examples:

```
difference(m, benchmark.m)                          m − b
ratio(difference(m, benchmark.m), benchmark.m)      (m − b) / b
ratio(difference(m, benchmark.m), m)                (m − b) / m
minMaxNorm(difference(m, benchmark.m))              EDBT form
percOfTotal(difference(m, benchmark.m))             EDBT form: (m − b) / Σ m
difference(zscore(m), zscore(benchmark.m))          each side normalized against its own population
```

This matches EDBT's model: a nested composition of cell transforms ⊟ and holistic transforms ⊡ (EDBT §4.2–4.3). EDBT writes transforms over m and m′ (§3 step 2) but gives no syntax for them; here they are ordinary nodes of the tree.

### 5.2 Evaluation

Each node maps the columns of the compared cells to a column (`DeltaScheme.java:232-280`). The compared cells are the matched cells (§4), or all target cells when there is no benchmark.

- A leaf reads v(c), v_B(c) or the constant.
- A cell-wise node applies f per cell to its two argument columns.
- A holistic node applies h to its argument column, **over the cells where that argument is defined** (§5.4). For `percOfTotal(x, y)`, that means the cells where both x and y are defined.

So the population of every holistic function is local and fixed by its own subtree. Under several benchmarks, `zscore` in two comparisons runs over two different cell sets, because the matched sets differ (§4.5).

### 5.3 Defaults

- **No AGAINST, no `USING`:** the compared quantity is v(c) itself (`DeltaScheme.java:147`). This is EDBT's dummy benchmark of zeros with `difference`.
- **AGAINST entry without `USING`:** `difference(m, benchmark.m)` (`DeltaScheme.java:152`, chosen in `AssessQueryBuilder.java:158-163`). This follows DOLAP §2.4: when only one comparison is shown, it should be the absolute difference.

### 5.4 Undefined values

A cell is **comparable** when every value its expression reads or computes is finite. The rule is applied at every node: an infinite or NaN input, or a non-finite result (x / 0, 0 / 0, overflow), makes the cell undefined from that node upward (`DeltaScheme.java:89-92`, `:247`, `:275`).

- An undefined cell is left out of the comparison, like an unmatched cell: it gets no label, and the report says "has an undefined comparison value". Because holistic functions run only over defined cells, one bad cell never shifts a mean, a range or a rank.
- A comparison where no cell is comparable fails the query, naming the benchmark (`src/main/java/intentional/assess/ComparisonModel.java:61-65`). Labeling schemes are therefore never fitted on an empty set.
- `percOfTotal` over a total of zero fails the query (`DeltaScheme.java:301-315`): the shares are undefined for all cells at once, so this is a property of the data, not of one cell.
- A column without spread maps to 0 under `zscore` and `minMaxNorm`, and so does `zscore` over a single cell: no spread, no deviation. This is a convention, not an error.

Tests: `src/test/java/intentional/assess/deltas/DeltaSchemeTest.java` covers each function, both defaults, each guard, and each rejected form.

---

## 6. Labeling (`LABELS`)

A labeling scheme maps the comparison values to labels of a domain D, ordered or unordered (`src/main/java/intentional/labeling/LabelingScheme.java:9`). Data-driven schemes are **fitted** on the comparison values first (`src/main/java/intentional/labeling/Labeling.java:55-67`). The fitting population is the matched cells of that comparison.

| Scheme | Rule | Domain | Fitted on | Code |
|---|---|---|---|---|
| custom `{[a,b): l, …}` | analyst ranges | ordered by range | nothing | `src/main/java/intentional/labeling/schemes/CustomLabelingScheme.java:199` |
| `EquiDepth` | k equal-count buckets, extremes folded into end buckets | ordered | values | `src/main/java/intentional/labeling/schemes/EquiDepthScheme.java:42-71` |
| `EquiWidth` | k equal-width intervals | ordered | values | `src/main/java/intentional/labeling/schemes/EquiWidthScheme.java:41-66` |
| `ZScoreBased` | bands on z at ±near, ±extreme | ordered, 5 labels | values | `src/main/java/intentional/labeling/schemes/ZScoreLabelingScheme.java:60-83` |
| `KPIMedianBased` | OK within median ± ½·\|median\|, Low below, High above; a zero median fails | ordered | values | `src/main/java/intentional/labeling/schemes/MedianDistanceScheme.java:39-64` |
| `KMeansApache` | nearest centroid of the optimal one-dimensional k-means | ordered by centroid | values | `src/main/java/intentional/labeling/schemes/KMeansScheme.java:42-78` |

### 6.1 Invariance: when the comparison function matters

Data-driven schemes ignore some transformations of their input. This decides which `USING` expressions can change the labels at all, and it belongs in the paper as a short proposition.

| Scheme | Labels unchanged under |
|---|---|
| EquiDepth | any strictly increasing map (depends only on the order) |
| EquiWidth, ZScoreBased, KMeans | any map x ↦ a·x + b with a > 0 |
| KPIMedianBased | any map x ↦ a·x with a > 0 |
| custom ranges | nothing |

Consequences, which follow directly from §5.2:

- `zscore`, `minMaxNorm`, `rank` and `percOfTotal` (with a positive total) are increasing. Wrapped around the whole comparison they never change an EquiDepth labeling. `zscore`, `minMaxNorm` and `percOfTotal` are positive affine maps, so they also leave EquiWidth, ZScoreBased and KMeans labelings unchanged. They matter only for custom ranges, where they bring the values into the ranges' units.
- With a **constant** benchmark v > 0, `difference(m, v)` is a shift and `ratio(m, v)` a positive scale. Under EquiDepth, EquiWidth and ZScoreBased, both give exactly the labeling of the raw measure. A constant benchmark is informative only under custom ranges (and, for `difference`, under KPIMedianBased).
- Sibling and past benchmarks do change the labels under every scheme, because b varies per cell.

### 6.2 Points on the schemes

- **gap (custom ranges with holes).** EDBT §4.1 leaves completeness to the user. A value outside every range gets the label `"no match"` (`CustomLabelingScheme.java:204`), which is not in the domain. Such a cell then has rank −1 in the consensus (§8.2).
- **holds (KPIMedianBased).** The band is measured in units of |median|, so it works for negative medians; a zero median fails the query.
- **holds (KMeans).** Exact one-dimensional k-means, deterministic, clusters ordered by centroid. The domain is always the k labels; fewer than k distinct values fail the query.
- **holds (shared scheme instance).** All comparisons of a query share one scheme object and each refits it (`AssessOperator.java:61-63`, `Labeling.java:57`). Labels are assigned at fit time, and every scheme's domain is fixed when it is created (`LabelingScheme.java:29-34`), so a refit cannot change what an earlier labeling reports.

---

## 7. Several benchmarks

A query with n AGAINST entries produces n labelings L_1 … L_n under the one scheme λ. Comparison i labels the cells matched by b_i, using f_i, with λ fitted on those values. The restriction to "n benchmarks ⇒ one scheme" is enforced (`AssessQueryBuilder.java:205-210`).

What one labeling means depends on the scheme:

- **Data-driven scheme:** L_i(c) says where c stands *among the cells matched by b_i*, measured by f_i. The labels are relative, and the label sets of different benchmarks are comparable as ranks.
- **Custom ranges:** L_i(c) is an absolute judgment of the value f_i(c). It is comparable across benchmarks **only if all f_i produce values in the same units**.

**defect / decide (units).** Each entry may carry its own `USING`. A query like `AGAINST PAST 3 USING ratio(m, benchmark.m), district = 'Brno' USING difference(m, benchmark.m) LABELS {[-inf,0.9): low, …}` applies one set of thresholds to a ratio (around 1) and to a difference (in amount units). The query is accepted, and the second labeling is meaningless. Options:

- (a) with custom ranges, require all f_i to have the same outermost function;
- (b) allow one `USING` per query when the scheme is custom;
- (c) document it as the user's responsibility, as EDBT does for range completeness.

---

## 8. Results from several labelings

### 8.1 The four results

A query with several AGAINST entries gives one labeling per benchmark (§7). From them ASSESS can derive four different results:

| Result | Question | Input | Output |
|---|---|---|---|
| R1 evaluation per cell | how did this cell do against its benchmarks? | the cell's label from each benchmark | one label per cell |
| R2 evaluation per cube | how did the whole result do? | the per-cell evaluations, or one benchmark's labeling | one label for the result |
| R3 standing after evaluation | where does each cell sit, judged by its evaluation? | the per-cell evaluations (R1) | an order or groups of cells |
| R4 standing before evaluation | where does each cell sit, across all benchmarks jointly? | all benchmarks' labels (or values) together | an order or groups of cells |

There is no standing of the cube within one query: standing is defined among the cells, so it would need other cubes to compare with.

**Evaluation or standing depends on the scheme.** A custom range gives an evaluation (OK = ratio in [0.9, 1.1]). A data-driven scheme refit per benchmark gives a standing against that benchmark: EquiDepth's High means "top third of the cells against this benchmark" (§6). Under data-driven schemes, R1 is therefore a consensus of standings, and R2 over a single EquiDepth labeling says little, because its buckets hold equal counts by construction.

**Running example**, used throughout §8:

```
WITH loan FOR year = '1997' BY district
ASSESS sum(amount) AS total
AGAINST B1 = sibling region, B2 = PAST 2, B3 = 500000      -- each USING ratio(total, benchmark.total)
LABELS {[0, 0.9): Low, [0.9, 1.1]: OK, (1.1, inf): High}
```

| Cell | B1 sibling | B2 PAST 2 | B3 constant |
|---|---|---|---|
| Praha | 1.30 High | 1.25 High | 1.05 OK |
| Brno | 1.00 OK | 1.20 High | 0.85 Low |
| Ostrava | 0.80 Low | 1.00 OK | 0.60 Low |
| Plzen | 0.85 Low | 1.05 OK | 1.15 High |
| Zlin | 0.70 Low | 0.85 Low | 1.20 High |

### 8.2 R1: evaluation per cell

**Problem.** Each cell's m labels on one ordered scale are combined into one label, and the result for a cell may use only that cell's labels. In the literature this is *grade aggregation*, where judges grade one candidate on a common scale and each candidate's collective grade depends only on its own grades (Balinski and Laraki, PNAS 2007; Stanford Encyclopedia, "Social Choice Theory" §4.5). It is also *label fusion*, i.e. combining classifier label outputs (Kuncheva 2014, ch. 4). With one benchmark, R1 is that benchmark's labeling.

**Options**, with the example's outcome:

| Rule | Example result (Praha, Brno, Ostrava, Plzen, Zlin) | Pros | Cons |
|---|---|---|---|
| plurality label | High, tie, Low, tie, Low | simplest | ignores the order of labels; frequent ties |
| median (majority judgment) | High, OK, Low, OK, Low | always a label; independent of clause order; order methods are the only strategy-proof grading rules (Balinski and Laraki 2007) | even m needs a tie-break: lower median, majority gauge, or Fabre (SCW 2021) |
| min (strict) / max (lenient) | OK, Low, Low, Low, Low / High, High, OK, High, High | explicit worst- or best-case meaning | one benchmark decides |
| mean rank, rounded | High, OK, Low, OK, **OK** | smooth | treats ordinal labels as numbers: Zlin's single High pulls it to OK |
| weighted median (B2 weight 3) | High, **High**, **OK**, OK, Low | encodes trust in a benchmark; log-odds weights are optimal for independent voters (Kuncheva 2014, §4.4) | the weights need a justification |
| combine values, then label (median of ratios) | High, OK, Low, OK, Low | one labeling from one quantity | needs values on one scale; different USING functions need standardizing first (§7) |
| latent-bias model (Dawid–Skene type) | no change here: estimated biases are B1 −0.2, B2 +0.4, B3 0 | corrects a systematically harsh or lenient benchmark | pools all cells to estimate the biases; not in the reading list yet |

Grades from different scales do not mean the same thing (Delemazure et al., SCW 2026), so R1 combines only labelings over one domain. Custom ranges with unequal steps are covered by majority judgment on non-uniform scales (García-Lapresta and Marques Pereira, EJOR 2022).

**Missing votes.** A cell not labeled by some benchmark can take the median of the votes it has, with a minimum support s. Today the consensus covers only the cells every benchmark labels (`ConsensusRule.java:89-104`), which Brancotte et al. (VLDB 2015) call *projection*; they show it drops relevant elements.

**What ASSESS implements.** R1 is computed today with Kemeny (`src/main/java/intentional/assess/ConsensusModel.java:44-64`, `src/main/java/intentional/labeling/consensus/ConsensusRule.java`), which is a standing rule (§8.5) whose buckets are read back as labels (`ConsensusRule.java:215-224`). **Defect:**
- Kemeny compares cells with each other, so a cell's label depends on other cells. In the example Plzen gets Low, the label of the cells it is tied with, although its own votes have median OK.
- When fewer buckets than labels are used, the labels are not determined at all, and the code returns the first voter's labels: swapping the AGAINST entries changes them (`ConsensusRule.java:52-61`, `:140-153`).

**decide:** the median as R1, with min and max as the strict and lenient variants.

**Magnitudes (defect).** The consensus inherits each cell's mean magnitude and mean reference (`ConsensusRule.java:107-137`), which feed weighted R2. The mean is meaningful only when all comparisons share one function; ratings on different scales need normalizing before they are combined (Langville and Meyer 2012, ch. 14). **decide:** inherit nothing, inherit only when all comparisons share one function, or normalize.

**"no match" (defect).** A `"no match"` label (§6.2) gets rank −1 (`ConsensusRule.java:48`), and the read-back throws if that state is kept (`ConsensusRule.java:221`).

### 8.3 R2: evaluation per cube

**Problem.** The cells' labels (from R1, or from one benchmark) become one label for the result. The literature gives two readings:
- **A single-winner election** (DOLAP §3.3–3.4): cells are voters, labels are candidates, and each ballot names one label. That is a social choice function (Brams and Fishburn 2002; Endriss 2013).
- **Grading one candidate,** the result, by n cells.

Neither reading's justification fits well. The epistemic argument for majority voting assumes independent judges of one truth (Condorcet's jury theorem; Stanford Encyclopedia §2.3), and strategy-proofness concerns judges who can misreport. Cells are parts of the result, not judges of it, so R2 is a summary of the parts. DOLAP's volume weights point the same way (§3.4, for additive measures). **Alternative:** label the result directly, comparing the result's total with the benchmark's total once.

**Options**, on the median R1 (High, OK, Low, OK, Low):

| Rule | Example | Pros | Cons |
|---|---|---|---|
| plurality | tie between OK and Low: none | simple | ignores the order of labels |
| absolute majority | none (no label holds more than half) | a strong claim | often elects nothing |
| median | OK (cumulative 2/5 at Low, 4/5 at OK) | the Condorcet winner under single-peaked ballots (Black 1948), strategy-proof there (Endriss 2013) | an exact split needs a tie-break (lower median) |
| Copeland / Condorcet methods | OK | | equal to the median under single-peaked ballots |
| true Borda (rankings by distance to each ballot) | OK (Low 5, OK 7, High 3) | uses every label's position | can violate the Condorcet principle (Endriss 2013) |
| quantile or threshold ("High if ≥ 40% of cells are High") | not High | explicit, risk-oriented semantics | a parameter to justify |
| weighted (C1 reference, C2 magnitude) | — | the weighted median is DOLAP's barycenter (§3.4) | weights must be on one scale |
| label the total | needs the totals | no voting needed | needs the result-level comparison |

On the Kemeny labels (High, OK, Low, Low, Low) the same rules elect Low. The choice made for R1 changes R2.

**What ASSESS implements.** The label-predominance archetype (`src/main/java/intentional/model/archetypes/labelpredominance/VotingRule.java`, `LabelDistributionAlgorithm.java`) runs on every labeling, the consensus included. Its default is the median for ordered domains and majority otherwise (`VotingRule.java:106`). The four rules (`VotingRule.java:18-100`) form two families:
- **Plurality type.** BORDA scores 1 per label with a smaller tally and ½ per tie, which elects the plurality winner: Copeland over tallies, not the Borda count. MAJORITY is plurality with a > ½ condition.
- **Median type.** VALENCE_MEDIAN elects what MEDIAN_VOTER elects, since ballots are single-peaked. Both elect nothing on an exact half–half split.

**decide:** the two families, BORDA renamed, and further rules from the table if wanted.

**Weighted ballots.**
- **C2:** a cell weighs |magnitude| (`LabelDistributionAlgorithm.java:112-121`). That is DOLAP's absolute delta only under `difference`/`absolute`. **gap.**
- **C1:** a cell weighs |v_B(c)|, and 0 without a reference (`LabelDistributionAlgorithm.java:124-126`), so it elects nothing in a benchmark-less ASSESS. **gap.**

**Order of composition (decide).** Two procedures exist, and they can disagree:
- aggregate per cell across benchmarks, then across cells;
- elect per benchmark across cells, then aggregate across benchmarks (DOLAP §3.6).

This is the premise-based versus conclusion-based choice of judgment aggregation, and no rule avoids the disagreement while keeping basic majority properties (List and Pettit 2002; Handbook of Computational Social Choice 2016, ch. 17). The code uses the first.

### 8.4 R3: standing after evaluation

**Problem.** Order the cells by their R1 evaluations. Grades give rankings as well as grades: Balinski and Laraki's *majority-ranking* orders candidates by their *majority-value*, the sequence of medians obtained by repeatedly removing the median grade, compared lexicographically (PNAS 2007).

**Example.** The medians alone give three groups: {Praha} > {Brno, Plzen} > {Ostrava, Zlin}. The majority-value then separates Zlin, (Low, Low, High), from Ostrava, (Low, Low, OK): Praha > Brno = Plzen > Zlin > Ostrava. Brno and Plzen have identical votes and stay tied.

**Labels.** A standing is labeled by position (top, middle, bottom), not by evaluation. Not implemented: **gap.**

### 8.5 R4: standing before evaluation

**Problem.** Order the cells using all benchmarks' labels jointly, so that a cell's place depends on how every benchmark compared it with the other cells. In the literature this is rank aggregation with ties (Fagin et al., PODS 2004; Brancotte et al., VLDB 2015) over p-ratings, i.e. bucket orders with at most |D| buckets (Ailon, Algorithmica 2010). Cut into k groups it is the optimal bucket order problem (Gionis et al., KDD 2006). The rule is needed when the inputs share no common scale, as in DOLAP §3.5, where different schemes vote.

**Options:**

| Rule | Example | Pros | Cons |
|---|---|---|---|
| Kemeny with ties | {Praha} > {Brno} > {Ostrava, Plzen, Zlin}: B1's own labeling, score 10 (the median labeling scores 14) | unique rule with Young–Levenglick's axioms; maximum-likelihood order (Young 1988) | NP-hard for m ≥ 4 (Dwork et al., WWW 2001); several optima |
| Borda / Copeland over cells | rank sums 5, 3, 1, 3, 2: Praha > Brno = Plzen > Zlin > Ostrava | O(n·m) | ignores the cost of untying (Brancotte et al. 2015) |
| footrule / median rank | — | polynomial; within a factor 2 of Kemeny (Dwork et al. 2001; Fagin et al. 2004) | approximate |
| rank by the combined value (median ratio) | Praha 1.25 > Plzen 1.05 > Brno 1.00 > Zlin 0.85 > Ostrava 0.80 | uses the numbers, not the labels | needs values on one scale |

Kemeny keeps Plzen with Ostrava and Zlin because some benchmark ties it with each of them. Borda and the combined value instead put it with Brno or above.

**What ASSESS implements.** Kemeny, with labelings grouped by identical ordered domain and the generalized Kendall-τ (`ConsensusRule.java:198-212`), over the cells every benchmark labels. Its output is used as R1 (§8.2), though it is an R4 result.

**Exact vs heuristic (decide).**
- **Exact:** an ILP with pair variables x_{a<b} and x_{a=b}, feasible up to about 60 elements (Brancotte et al. 2015). Preprocessing splits it: Extended-Condorcet reductions (Betzler et al., JAAMAS 2014) and the finest-Condorcet partition for ties (Akbari and Escobedo, COR 2023). Bounds: Davenport and Kalagnanam (AAAI 2004), Conitzer et al. (AAAI 2006).
- **Heuristic:** the code's climb (`ConsensusRule.java:139-183`) is BioConsert without its new-bucket move. BioConsert was best on 88.6% of datasets with a 0.03% gap (Brancotte et al. 2015). Other options: KwikSort (Ailon et al., JACM 2008), local Kemenization (Dwork et al. 2001).
- **Several optima:** inherent to the rule (Young 1988). They can be detected (Langville and Meyer 2012, ch. 15), merged into one (Muravyov and Emelyanova, Measurement 2021), or summarized by the frontier common to all optima (Andrieu et al., FGCS 2021).

**Labels.** By position only. A standing says nothing about absolute level, so R4 does not feed R2.

### 8.6 Agreement between benchmarks (gap)

How much the benchmarks disagree is itself a result. Measures: the mean pairwise Kendall-τ of the labelings (Brancotte et al. 2015, eq. 5), or inter-rater κ with benchmarks as raters and cells as subjects (Kuncheva 2014, §8.2), in its weighted form for ordinal labels. In the example, B3 disagrees with B1 on four of five cells. Not computed today.

---

## 9. Evaluation plans (fetching)

The comparisons need the cubes T, B_1, …, B_n. Three strategies fetch them (`src/main/java/intentional/assess/fetch/FetchStrategy.java`):

- `SCAN_PER_SLICE`: one query per slice, i.e. the target, each sibling, and each past period (`SerialFetcher.java`).
- `SCAN_PER_BENCHMARK`: a past window in one query (date predicate widened to `IN`, rows split per period), and identical slices fetched once (`src/main/java/intentional/assess/fetch/BatchedFetcher.java:32-56`).
- `SCAN_PER_QUERY`: one query for everything. Every level whose value differs across slices is widened to `IN (all values)`, and rows are partitioned back per slice (`src/main/java/intentional/assess/fetch/SingleScanFetcher.java:36-55`, `:103-168`).

### 9.1 Why the plans give the same cubes (needs writing in the paper)

Let V be the levels whose value differs across the slices, and suppose **V ⊆ G**. In the widened query every output group fixes one value for each level of V, so the group's facts are exactly the facts of that group in the one slice query with those values. Its aggregate is therefore identical for *any* aggregation function (no additivity needed), and the slice's cells are exactly the rows whose V-columns carry the slice's values.

- The precondition V ⊆ G is enforced (`SingleScanFetcher.java:138-150`). It coincides with the matching precondition of §4.2–4.3.
- A test pins the equivalence on one query (a sibling, a past window and a constant): identical labels and magnitudes under all three strategies, at 4, 3 and 1 scans (`src/test/java/intentional/assess/AssessOperatorTest.java:320-347`). **Holds**, once the argument above is written down.
- **gap (cost).** With two or more varied levels, the widened query fetches the cross product of their values. Example: a sibling on district and a past window on year fetch (Brno, 1996), which no slice needs. The result is still correct, but the plan's cost should account for it. A query that widens per *benchmark group* avoids the waste.

### 9.2 Relation to EDBT

EDBT §5 already has a Naive Plan, a Join-Optimized Plan, and a Pivot-Optimized Plan that fetches target and benchmark slices in one query. `SCAN_PER_SLICE` is EDBT's Naive Plan, and `SCAN_PER_QUERY` is the pivot idea generalized to **several benchmarks of mixed kinds** in one query. The new contribution is that sharing *across* benchmarks. The paper should say so plainly. Unlike EDBT's Join-Optimized Plan, matching and transforms run in memory, not in SQL.

---

## 10. What the result contains

Per comparison i and matched cell c (`src/main/java/intentional/assess/ComparisonModel.java:53-81`):

- v(c), the target value (the result data),
- v_B(c), the reference (`Labeling.referenceOf`),
- f_i(c), the comparison value (`Labeling.magnitudeOf`),
- L_i(c), the label, and the benchmark's identity as a parameter of the result.

This is EDBT's output schema ⟨m, m_B, m_Δ, m_λ⟩, once per benchmark. The consensus adds K(c), plus inherited magnitudes and references (§8.2). **Holds.** DOLAP §2.4 recommends showing actual, benchmark, absolute *and* relative difference together. The result holds only the one f_i the user asked for. **gap:** whether the report adds the other difference.

---

## 11. Open points

**Defects (code against definition)**

1. R1 is computed with a standing rule (Kemeny): a cell's label depends on other cells, and on the order of the AGAINST entries (§8.2).
2. Consensus magnitudes and references average incomparable quantities when comparisons use different functions (§8.2).
3. `"no match"` labels enter the consensus as rank −1 and can crash the read-back (§6.2, §8.2).

**Gaps (definitions to write)**

4. Missing past periods and cells: mean over the periods present, versus missing = 0 (§4.3).
5. The population of scheme fitting = comparable cells of that comparison (§5.2, §6).
6. The invariance proposition: which `USING` expressions can change the labels under which schemes (§6.1).
7. Units across benchmarks under custom ranges (§7).
8. R3, standing after evaluation, not implemented (§8.4).
9. Agreement between benchmarks, reported with the result (§8.6).
10. The fetch-equivalence argument and the cross-product cost (§9.1).
11. The weighted-ballot definitions: C2 under `ratio`, C1 without a benchmark (§8.3).
12. Preconditions with clear errors: sibling level ∈ G (§4.2), date level ∈ G (§4.3).

**Decisions**

13. Past benchmark: mean as the predictor, or a predictor parameter, against EDBT's regression (§4.3).
14. `assess`/`assess*`, or the current hybrid stated as the definition (§4.5).
15. External benchmarks: implement or scope out (§4.4).
16. R1 rule: the median, with min and max as variants (§8.2).
17. Cells R1 covers: projection, or every cell with at least s votes (§8.2).
18. R2 rules: the two families, BORDA renamed, further rules; or label the total (§8.3).
19. The order of composition for R2 (§8.3).
20. R4: exact ILP with Condorcet partitioning versus local search, with a measured gap (§8.5).
