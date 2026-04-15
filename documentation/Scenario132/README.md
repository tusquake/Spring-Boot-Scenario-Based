# Scenario 132: Mutation Testing (PITest)

This scenario demonstrates **Mutation Testing**, a powerful technique for verifying the quality of your test suite.

## The Problem: Binary Coverage vs. Logic Coverage
Traditional code coverage tools (like Jacoco) measure which lines of code were executed. However, you can achieve **100% line coverage** with tests that don't actually verify the logic correctly. 

For example, if you have `if (price > 1000)` and you only test `price = 1100`, your coverage is 100%. But اگر you change the code to `if (price >= 1000)`, your test will still pass. This is a **weakness** in your test suite.

## How PITest Works
1.  **Mutants**: PITest automatically modifies your code slightly (e.g., changes `>` to `>=`, swaps `+` with `-`, removes a method call).
2.  **Killed**: If a unit test fails when the code is mutated, the mutant is "Killed" (Good).
3.  **Survived**: If all your unit tests still pass despite the mutation, the mutant "Survived" (Bad).

## Implementation in this Scenario

### 1. `DiscountService.java`
Contains conditional logic for discounts:
- `price > 1000` -> 10%
- `price > 500` -> 5%

### 2. `DiscountServiceTest.java`
Achieves **100% line coverage** by testing values 1100, 600, and 200. However, it misses the **boundary value** of exactly 1000.

### 3. Running the Analysis
Since PITest is slow and heavy, we've isolated it in a the `mutation-test` Maven profile.

```bash
mvn pitest:mutationCoverage -Pmutation-test
```

## Expected Results
When you run the command:
1.  PITest will generate multiple mutants.
2.  A mutant where `>` is changed to `>=` (or vice versa) for the `1000` threshold will likely **SURVIVE**.
3.  The report will show a **Mutation Coverage** less than 100%, even if **Line Coverage** is 100%.

## Report Location
After running, open:
`debug-challenge/target/pit-reports/index.html`

## Interview Theory
- **"Who tests the tests?"**: PIT is the answer. It ensures your tests are meaningful.
- **Equivalent Mutants**: Sometimes a mutation doesn't change behavior (e.g., changing `i++` to `++i` in some cases). These are "equivalent mutants" and are a known limitation.
- **When to use?**: Best used on core business logic (Services/Entities) rather than boilerplate (DTOs/Controllers).
