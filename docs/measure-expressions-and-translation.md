# Target-measure expressions and the three translation chains

## What ASSESS accepts now

The target measure of an ASSESS query is a full arithmetic expression over aggregates,
with an optional alias:

```
ASSESS sum(amount)                                          -- plain, as always
ASSESS sum(amount) - sum(payments) AS Profit                -- derived measure
ASSESS (sum(amount) - sum(payments)) / 1000 AS ProfitK      -- parentheses and constants
ASSESS max(amount) - max(payments) AS Spread AGAINST PAST 5 -- works with benchmarks
```

Grammar shape (`AssessQuery.g`): `measure_expression` → terms joined by `+ -`,
terms → factors joined by `* /`, factors → `AGG( expression )`, `( expression )`,
a column name, or a number. Everything the expression allows ends up in the SQL as written,
so standard SQL operator precedence applies.

## How it works

The engine has three different roads from a query string to a `CubeQuery`, one per operator.
They all end at the same measure representation — a `QueryMeasure` whose *name* can be a
formula string with an alias — but they get there differently.

| Operator | Parser | Translation | Measures |
|---|---|---|---|
| DESCRIBE | own recursive expression grammar | `DescribeQueryGenerator` builds the `CubeQuery` **object directly**; each raw measure string becomes `new QueryMeasure(raw)` | many per query, full expressions |
| ASSESS | recursive expression grammar (same shape) | `CubeManagerAdapter` writes the legacy **text mini-language** (`CubeName:/AggrFunc:/Measure:/Gamma:/Sigma:`) and calls `createCubeQueryFromString`; for a derived measure, the translated query's measure is then **swapped** for `new QueryMeasure(expression AS alias)` | one per query, full expressions |
| ANALYZE | own grammar, `agg(measure)` only | `AnalyzeTranslationManager` builds `CubeQuery` **objects directly** for the whole query family (base, siblings, drill-downs), from a single `aggrFunc` + `measure` pair | one per query, no expressions |

The ASSESS road in detail:

1. The parser captures the expression **as text** and hands it to
   `AssessQueryBuilder.setTargetMeasure(text, alias)`.
2. A single plain aggregate (`sum(amount)`, no alias) takes the original path untouched —
   old queries translate exactly as before.
3. Anything else is a *derived measure*: the builder extracts the first aggregate and
   column (they anchor the text template's `AggrFunc:`/`Measure:`/`Name:` lines, which
   the mini-language requires), and the adapter, after `createCubeQueryFromString`
   returns, replaces the query's measure list with the full expression as one
   `QueryMeasure` — the same constructor DESCRIBE's generator uses.
4. **Benchmarks inherit the expression for free**: `PAST n` and sibling benchmarks are
   produced by re-running `translateToCubeQuery()` with a shifted selection, and the
   swap happens inside that method — so every benchmark query computes the same
   expression as the target.

The delta clause's operands bind to real values: a bare name is the target's measure,
`benchmark.X` the matched benchmark cell's value, and a number a constant —
`USING ratio(total, 20000)` divides by 20000, not by the AGAINST value. The operand
name must be the measure the result carries: the alias when the target is aliased,
the base column of a bare `sum(amount)` otherwise. **Preferred style: alias the
target and reference the alias** — it reads as SQL:

```
ASSESS sum(amount) AS total AGAINST PAST 2
USING ratio(total, benchmark.total)
```

The bare-name shorthand (`ratio(amount, benchmark.amount)`) stays valid.

The comparison itself is an expression tree (`intentional/assess/deltas/DeltaScheme.java`)
whose every function names its arguments: cell-wise functions take two
(`ratio`, `difference`, `invDifference`, `absolute`), holistic functions take one
and rewrite a whole column (`minMaxNorm`, `zscore`, `rank`, and `percOfTotal`, which
divides by the target measure's total or by the column given as its second argument).
A holistic function can wrap the comparison or either operand:

```
USING minMaxNorm(difference(total, benchmark.total))
USING difference(zscore(total), zscore(benchmark.total))
USING ratio(difference(total, benchmark.total), benchmark.total)
```

## Why the chains differ

They were written in different eras. ANALYZE and the original ASSESS predate the
expression-capable `QueryMeasure`; DESCRIBE's translation was built later, directly on
it. Rather than rebuilding ASSESS's translation (its benchmark machinery is welded to
the text template), the derived-measure support goes *around* the template with the
post-translation swap — full expression power, zero disturbance to benchmarks.

If ANALYZE ever needs derived measures, it is the easier port: its translation already
builds `CubeQuery` objects directly, so it needs only the richer grammar plus
`QueryMeasure(raw)` at its construction points — the DESCRIBE recipe, not the ASSESS one.

## Limits

- One target measure per ASSESS query (the assessment pipeline is single-quantity:
  one delta map, one labeled quantity, one election subject).
- The expression must contain at least one aggregate.
- ANALYZE remains `agg(measure)` only.
- ASSESS selection predicates are equality-only (`level = 'value'`); DESCRIBE's richer
  filters (`IN`, `NOT IN`, inequalities, nested `WITH`) were never ported.

## Future work (deferred deliberately)

1. **Filter vocabulary port to ASSESS.** Bring DESCRIBE's filter language
   (`sigmaExpression` in `intentional/describe/syntax/DescribeOperator.g`) into the
   ASSESS grammar (`selection_predicates` in `intentional/assess/syntax/AssessQuery.g`).
   Expect friction at the text template: `CubeManagerAdapter.generateQuery()` renders
   filters as `Sigma:level='value'` pairs for `createCubeQueryFromString`
   (`parsermgr/CubeSql.g`), which may not express `IN`/ranges — either a
   post-translation workaround like the measure swap, or the trigger for item 2.

2. **One translation core ("cube query building").** Today three parallel roads exist
   (see the table above): `DescribeQueryGenerator` (objects, rich),
   `CubeManagerAdapter` (text template + measure swap), `AnalyzeTranslationManager`
   (objects, poor grammar). Target: one lexer/token vocabulary for the intentional
   operators, a shared measure-expression grammar (ANTLR composite import — blocked
   today by the incompatible token sets `WORD/NUMBER/AGGRFUNC` vs
   `ID/INT/FLOAT/AGGREGATE`), and one object-building translation service, retiring
   the text mini-language and the swap. The risky part is ASSESS's benchmark
   machinery (`collectPastRecords` re-translation), which is welded to the template.

3. **The `assess`/`assess*` join pair** per the assess-queries paper (EDBT 2021, §4.1).
   Today the engine returns all target cells with unmatched ones left unlabeled — the
   paper's `assess*` left-outer behavior. The strict inner `assess` (unmatched cells
   dropped from the result cube, so labelings, archetypes, and highlights see only the
   compared universe) is not implemented; the star is not in the grammar.

4. **Alias-only operand naming.** Decide whether `USING` operands must reference the
   target by alias (rejecting the bare base-column shorthand). Today both are accepted;
   the alias form is the documented preference.
