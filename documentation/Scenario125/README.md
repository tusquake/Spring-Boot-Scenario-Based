# Scenario 125: Lazy vs Eager Loading (The N+1 Problem)

In JPA/Hibernate, **FetchType** determines when related entities are loaded from the database. Improper configuration often leads to the **N+1 Selection Problem**, which can drastically slow down your application.

## High-Level Concepts

### 1. FetchType.EAGER
- Related entities are loaded **immediately** with the parent.
- **Risk**: Can lead to "cartesian product" explosions and fetching massive amounts of data that you don't actually need.
- **Example**: Loading an `Author` automatically loads all 100 of their `Books`.

### 2. FetchType.LAZY (Default for @OneToMany)
- Related entities are loaded only **when they are first accessed** (e.g., calling `author.getBooks()`).
- **Risk**: The **N+1 Problem**. If you fetch 10 authors and then loop through them to get their books, Hibernate will issue 1 query for the authors + 10 separate queries for the books.

---

## How to Test this Scenario

### Step 1: Initialize Data
Seed the H2 database with authors and books:
`GET http://localhost:8080/debug-application/scenario125/initialize`

### Step 2: Trigger the N+1 Problem
Fetch all authors and access their books in a loop:
`GET http://localhost:8080/debug-application/scenario125/n-plus-one`

**Check your console logs**. You will see something like this:
```sql
-- 1 query for all authors
select * from scenario125_authors;

-- 10 separate queries (one for each author's books)
select * from scenario125_books where author_id = 1;
select * from scenario125_books where author_id = 2;
...
select * from scenario125_books where author_id = 10;
```

### Step 3: Trigger the Optimized Solution
Fetch everything in a single atomic join using `JOIN FETCH`:
`GET http://localhost:8080/debug-application/scenario125/optimized`

**Check your console logs**. You will see exactly **ONE** query using an inner join:
```sql
select a.*, b.* from scenario125_authors a 
left join scenario125_books b on a.id = b.author_id;
```

---

## 🚀 The Common "Gotcha": `LazyInitializationException`
You will often see this error: 
`org.hibernate.LazyInitializationException: could not initialize proxy - no Session`

**Why?** You tried to access a Lazy-loaded collection (like `author.getBooks()`) **outside** of a transaction (after the Hibernate Session was closed).

**Solution**: 
1. Use `JOIN FETCH` (as shown in this scenario).
2. Use `@EntityGraph`.
3. Keep the transaction open longer (using `@Transactional` on the calling service).

---

## Interview Tip: "Should I just use EAGER everywhere?"
**Absolutely not.** `FetchType.EAGER` is often a "lazy" way to fix the `LazyInitializationException` but it kills performance at scale. Always default to **LAZY** and use **explicit fetching** (`JOIN FETCH`) only when you know you need the child data.
