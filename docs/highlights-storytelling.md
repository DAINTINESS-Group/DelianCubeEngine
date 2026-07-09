# Highlights → Storytelling — Design Spec

> **Working draft.** Sections marked **[OPEN]** are unresolved questions.

This document specifies how we turn the `HighlightSet` an intentional operator
produces into an ordered *story*. It covers the stages up to the storyboard
(SELECT and RELATE). The storyboard/walk is Part 8, not yet written.

Notation: `∈` "in", `⊆` "subset", `∩` "intersection", `∪` "union", `∅` "empty
set". Every formal line has a plain-English gloss.

---

## Part 1 — The cube (domain primitives)

| # | Term | Definition | Code |
|---|------|------------|------|
| D1 | **Dimension** | An axis of analysis (`Region`, `Time`). | `cubemanager.cubebase.Dimension` |
| D2 | **Hierarchy** | An ordering of levels within a dimension (`Country ▷ Region ▷ City`). | `cubemanager.cubebase.Hierarchy`, `LinearHierarchy` |
| D3 | **Level** | One granularity in a dimension (`Region`, `City`, `Quarter`). Has an ID attribute and a description attribute; knows its hierarchy position. | `cubemanager.cubebase.Level` (`levelIDAttribute`, `levelDescriptionAttribute`, `positionInHierarchy`) |
| D4 | **Member** | A distinct value of a level's **ID attribute** — the identity of a point at that level (the `Region` whose id is `North`). | a `String` id |
| D5 | **Measure** | A number analysed, with an aggregation carrying `additive`. | `cubemanager.cubebase.Measure`, `QueryMeasure`, `AggregationFunction` |
| D6 | **Query / result** | A query fixes a **gamma** (group-by levels) and a **sigma** (selection) + measures; the result is a set of cells. | `cubemanager.cubebase.CubeQuery`, `result.Result` |
| D7 | **Cell** | One data point: one member per grouped level + measure value(s). E.g. `(Region:North, Quarter:Q4) → Revenue 300`. | `result.Cell` |

---

## Part 2 — Highlights

| # | Term | Definition | Code |
|---|------|------------|------|
| D8 | **Archetype** | A reusable pattern hypothesis (Mega, Outlier, Modality, …). | `result.highlights.metamodel.ArchetypeProperty` |
| D9 | **Holistic highlight** | A **claim**: an archetype's verdict about **one measure**, across explanator levels, supported by elementary highlights. | `result.highlights.instance.HolisticHighlight` |
| D10 | **Elementary highlight** | A **supporting fact** under a holistic: members + a measure value + a role. | `result.highlights.instance.ElementaryHighlight` |

- **Shape claim** (Modality, Predominance): the verdict is about the distribution's
  shape; no single member is "the point."
- **Member-focused claim** (Mega, Outlier, TopK): evidenced by specific members —
  its elementaries *are* the detail.

A **measure-axis** archetype (Mega, Outlier, Modality, TopK) is evaluated **once per
applicable measure** → one holistic per measure (`Outlier(Rev)`, `Outlier(Margin)`).
A **labeling-axis** archetype (LabelPredominance) → one per labeling.

---

## Part 3 — The story node: a view over the highlights

A story node is **not a new data type** — it is a **view over the existing highlight
model** (`result.highlights.instance`). The graph stages read a few *derived*
attributes off the real objects; the full objects ride along for narration. There
are two structures: the cube-layer context, and the node-as-view.

```
QueryContext                                   // cube layer — one per cube query
  grain  : Set<Level>              // gamma — the group-by levels
  slice  : Set<(Level, Member)>    // sigma — the query's fixed member selections

StoryNode                                      // a VIEW, not a copy
  source : { holistic : HolisticHighlight,
             group    : List<ElementaryHighlight> }   // a role-group; ∅ for a shape claim
  // derived, for the graph algebra:
  lens()         = source.holistic.archetype
  measure()      = source.holistic.mainMeasure
  focus()        = ⋃ e.characters  for e in group      // ∅ for a shape claim
  context()      = ( grain = holistic.explanators, slice = query sigma )
  interestingness() = from Interestingness facets + the algorithm Scores
```

**Nothing is stripped.** The graph uses only the derived attributes, but everything
else stays reachable on `source` for the narrator:

| kept on `source` | narration uses it for |
|---|---|
| `holistic.result` (verdict + metrics) | *"concentrated (0.72)"*, *"holds"* |
| `elementary.role` | exemplar vs exception, top-contributor, outlier |
| `elementary.measureValue` | the number to state (*"Revenue 300"*) |
| `scores` (holistic + elementary) | ranking, and the figures in the sentence |
| `Character.description` | human-readable member names |

Where each derived attribute comes from:

| derived | from |
|---|---|
| `lens` | `HolisticHighlight.archetype` |
| `measure` | `HolisticHighlight.mainMeasure` |
| `focus` | union of the group's `ElementaryHighlight.characters` (∅ for a shape claim) |
| `context.grain` | `HolisticHighlight.explanators` |
| `context.slice` | the query's `sigma` |
| `interestingness` | `Interestingness` facets + the algorithm `Score`s |

`Character` is a highlights-layer type (`type : Level`, `id : String` = the member)
and appears only inside `focus`. The context's `slice` is the cube query's own
`(Level, Member)` selection, not `Character`; the two compare by `(level, member)`.

Why grain/slice sit on the context, not the node: they describe the *query*, shared by
all its highlights. Every holistic of a query carries the same `explanators` — the full
set of the query's gamma levels — so `grain`, `holistic.explanators`, and the gamma are
the one same value, constant across the query's nodes. An archetype's narrowing to a
member (Mega's `{(Region, North)}`) is its `focus`, taken from the elementaries'
characters: a subset of the grain that lives on the node, not the context. Grain and
slice vary only across queries (the multi-context case, Part 8).

> `Character.equals`/`hashCode` on `(type, id)` are required for the member-set
> operations the pipeline runs.

**Granularity — one holistic, several nodes.** A node's `group` is a **role-group**:
the elementaries of one holistic that share an `ElementaryHighlightRole` (and so a
level). The role names the narrative function of the members:

| role (`ElementaryHighlightRole.name`) | archetype | narrative function |
|---|---|---|
| `MegaContributor` | MegaContributor | the dominant contributor along a breakdown |
| `TopContributor` | TopKContributors | one of the *k* largest contributors |
| `Outlier` | Outlier | a cell far from the measure's distribution |
| `LabeledCell` | LabelPredominance | an assessed cell — an **exemplar** when its label is the dominant one, an **exception** otherwise |

Exemplar and exception are the two partitions of a `LabeledCell` group, split by each
elementary's label `Score` against the holistic's dominant label; they are functions of
a role-group, not roles. So one holistic yields **several** nodes — one per role-group —
plus, for a **shape-claim** holistic (Modality, LabelPredominance), a **claim node**
(`focus = ∅`) for the aggregate verdict. TopKContributors → *top Regions*, *top
Quarters*; LabelPredominance → *claim* + *exceptions*; Modality → *claim* only (it
carries no elementaries). Exemplar groups usually restate the claim and are pruned.

Two multiplicities produce this fan-out, both handled without new machinery — a
measure-axis archetype yields one holistic per applicable measure (nodes differing in
`measure`), and a holistic's role-groups yield nodes differing in `focus`. The pipeline
prunes the fan-out by interestingness.

---

## Part 4 — Interestingness

Interestingness draws on two sources, both already carried on the highlight objects:

- **Holistic facets** — the interestingness facets (`PECULIARITY`, `SURPRISE`, …) a
  `Interestingness` computes over the whole query result and attaches to the holistic.
  They are session-relative: a facet's value depends on the session's other results, so
  it ranks highlights within a session rather than scoring them absolutely, and any
  fixed threshold θ holds only within a session.
- **Elementary strength** — each elementary carries the archetype's own `Score` for its
  member: a contribution share (`ContributionShare`), a z-score (`ZScore`), a deviation
  magnitude (`Magnitude`). This is per-member and always present, independent of the
  session.

| # | Term | Definition | Code |
|---|------|------------|------|
| D11 | **Interestingness** | `[0,1]`. Claim node: the holistic's interestingness facet. Member node: that facet weighted by the member's own strength (share, \|z\|, magnitude), or the strength alone where no facet is computed. | `Interestingness`, `InterestingnessFacet`; each elementary's `Score`s |

---

## Part 5 — Pipeline

The pipeline turns the raw `HighlightSet` into a graph in three stages — extract
(given), SELECT (filter), RELATE (connect) — and its algebra is over members. Let
`involved(H) = focus(H) ∪ slice(context(H))`: every member a highlight touches, its own
`focus` characters plus its query's `slice`, compared by `(level, member)`.

**Stage 0 — Extract (given).** The engine runs every archetype over each query result →
raw holistics + elementaries; some are gated (Mega only on additive measures). Part 3
turns these into role-group nodes.

**Stage 1 — SELECT** (filtering only), on `HighlightSet`'s existing operators:
1. `prune` — keep nodes whose holistic verdict holds.
2. `threshold` — drop nodes below θ on an interestingness facet (`pruneByScore`).
3. `rank` — order by interestingness, keep the head (`topK`).
4. `dedup` — collapse **duplicates** (D15), keeping the stronger. Two nodes are
   duplicates when they share `context` and `measure` and their `focus` sets coincide
   *or one contains the other* — across archetypes too (a Mega member inside a TopK set,
   an Outlier on the same cell): the same members spotlighted.

**Stage 2 — RELATE** (connecting):
1. `theme edges` — X and Y form a **theme** (D12) when they share a **focus** member,
   `focus(X) ∩ focus(Y) ≠ ∅`: they spotlight the same entity. The query's `slice` sits in
   every node's `involved` set — shared background, not a discriminator — so theme keys
   on `focus`, not the whole `involved`.
2. `move edges` — a **move** is a transition between two nodes; `cost` = number of moves,
   **coherent** = cost 1:
   - *within one context* (grain, slice equal): `drill-across` — change `measure`, same
     `focus`; `refocus` — change `focus` as **refine** (add a member), **sibling** (swap
     at the same level), or **generalise** (drop a member).
   - *across contexts* (grain and/or slice differ — the ANALYZE transitions):
     `drill-down` — grain refines, slice gains the drilled member; `roll-up` / `sibling`
     — grain coarsens, slice changes. These arrive pre-typed from the operator
     (`AnalyzeQuery` records the gamma/sigma deltas); within-context moves are derived
     from `measure`/`focus`.
3. `spine` (D13) — the member appearing in the most `focus` sets: the **protagonist**.
   Across contexts its thread is *one node's `focus` = another's `context.slice`* — "drill
   into the member you spotlighted"; this is where `involved`'s `slice` part earns its
   place. Within one query there is no such thread — the spine is the most-recurring
   focus member.
4. `foil` (D14) — a high-interestingness node sharing no **focus** member with the spine:
   the contrast.

Output: `graph = (nodes, theme-edges, move-edges, spine, foil)`.

---

## Part 6 — Example A: ASSESS (single context)

*Assess 2023 Revenue & Margin% by Region×Quarter vs last year.* One query → **one**
context:

```
ctx = { grain: {Region, Quarter},  slice: {(Year, 2023)} }
```

`Year = 2023` is the query's `FOR`/sigma selection — in every node's `involved` set,
shared background, never the spine.

**Stage 0 — raw highlights.** What the extractor emits over the result, before SELECT:

| # | lens | role-group | measure | focus | verdict · strength |
|---|---|---|---|---|---|
| r1 | Modality | claim | Rev | {} | holds · concentration 0.71 |
| r2 | Modality | claim | Margin | {} | **fails** · uniform (0.12) |
| r3 | MegaContributor | `MegaContributor` | Rev | {(Region, North)} | holds · share 0.58 |
| r4 | TopKContributors | `TopContributor` | Rev | {(Region, North), (Region, South), (Region, East)} | holds · top-3 |
| r5 | Outlier | `Outlier` | Rev | {(Region, North), (Quarter, Q4)} | holds · z 3.1 |
| r6 | Outlier | `Outlier` | Rev | {(Region, East), (Quarter, Q1)} | holds · z 2.3 (**low**) |
| r7 | Outlier | `Outlier` | Margin | {(Region, South), (Quarter, Q2)} | holds · z 2.9 |
| r8 | LabelPredominance | claim | — | {} | holds · dominant share 0.70 |
| r9 | LabelPredominance | exception | — | {(Region, North), (Quarter, Q4)} | 2 exceptions |

**Stage 1 — SELECT** thins them:
- `prune` — **r2** dropped: its verdict fails (Margin is uniform, not concentrated).
- `threshold` — **r6** dropped: it holds, but its interestingness (z 2.3) is below θ.
- `dedup` — **r4** collapses into **r3** (D15): `MegaContributor`'s focus `{North}` is
  contained in `TopKContributors`' `{North, South, East}` at the same context and
  measure — the same member spotlighted — so keep the stronger, r3.

The six survivors become nodes (E and F share one `LabelPredominance` holistic — E its
claim, F its exceptions):

| node | lens | role-group | measure | focus |
|---|---|---|---|---|
| A · Rev-shape | Modality | claim | Rev | {} |
| B · North-mega | MegaContributor | `MegaContributor` | Rev | {(Region, North)} |
| C · North-Q4-out | Outlier | `Outlier` | Rev | {(Region, North), (Quarter, Q4)} |
| D · South-Q2-out | Outlier | `Outlier` | Margin | {(Region, South), (Quarter, Q2)} |
| E · assess-claim | LabelPredominance | claim | — | {} |
| F · North-Q4-exc | LabelPredominance | exception | — | {(Region, North), (Quarter, Q4)} |

**Stage 2 — RELATE.** Each node's `involved = focus ∪ {(Year, 2023)}`; the shared
`(Year, 2023)` is background, so theme keys on `focus`:

- **theme / spine:** `focus(B) ∩ focus(C) = {North}`, `focus(C) ∩ focus(F) = {North, Q4}`
  — B, C, F all carry `North` → **spine North = {B, C, F}**.
- **foil:** D (`South`/`Q2`, Margin) shares no focus member with `North` → **foil**. (Its
  `involved` still meets the others at the background `(Year, 2023)` — which is exactly
  why theme keys on `focus`, not the whole `involved`.)
- **moves** (one context — grain/slice fixed, only `measure`/`focus` vary):

| move | kind | cost |
|---|---|---|
| A → E | drill-across (Rev → labeling) | 1 |
| B → C | refocus: refine (`North` → `North, Q4`) | 1 |
| C → F | drill-across (Rev → labeling) | 1 |
| C → D | drill-across (Margin) + sibling (`North→South`, `Q4→Q2`) | 3 |

The cost-1 moves form one chain; `D` hangs off as the foil:

```
   E ───1─── A ───1─── B ───1─── C ───1─── F
 assess     Rev-shape  North-    North-Q4- North-Q4-
 claim      Modality   mega      out       exc

   spine North: B, C, F   ·   foil D (South/Q2, Margin), cheapest link cost 3
```

One context, so the graph reduces to `(measure, focus)`; `involved`'s `slice` part is
idle here and does its work across contexts (the ANALYZE walk-through, not yet written).

---


## Part 7 — Deferred items

- `Character.id` contract: it holds the level's **ID-attribute** value, with
  `description` for display — the javadoc is loose about id vs name.
- Cell-level bitmaps: a scale optimization only; overlap is drill structure, not
  redundancy, so no "freshness" metric. Add only if a coverage loop is proven hot.

---

## Part 8 — Not yet written

- **Example B — ANALYZE (multiple contexts):** the multi-context walk-through — contexts
  linked by `drill-down`/`roll-up`, the protagonist thread *one node's `focus` = another's
  `slice`*, and `involved`'s `slice` part carrying the theme across contexts.
- **Stage 3 — SEQUENCE (the walk):** ordering nodes into a story under an arc/role
  schedule, minimising move cost. Two backbones coexist here and SEQUENCE reconciles
  them: the cost-1 **move chain** (adjacent nodes one move apart) and the **theme spine**
  (nodes sharing the protagonist member). They need not coincide — a claim node with
  `focus = ∅` joins the move chain but not the spine — so the walk decides whether the
  order follows the members or the moves.
- **Storyboard IR** + narration adapters.
