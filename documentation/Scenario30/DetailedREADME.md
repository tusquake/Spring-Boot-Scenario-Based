# Pagination Strategies (Page vs Slice vs Window) — Complete Interview Reference

## Table of Contents
1. [Why Paginate?](#1-why-paginate)
2. [Offset Pagination (Page<T>)](#2-offset-pagination)
3. [Infinite Scroll Pagination (Slice<T>)](#3-slice-pagination)
4. [Keyset / Cursor Pagination (Window<T>)](#4-keyset-pagination)
5. [The Classic Interview Trap: Deep Pagination Performance](#5-the-classic-interview-trap-deep-pagination)
6. [The SELECT COUNT(*) Problem](#6-count-problem)
7. [Sorting in Pagination](#7-sorting)
8. [Pagination with JOIN FETCH (The Memory Trap)](#8-join-fetch-trap)
9. [Filtering in Paginated Queries](#9-filtering)
10. [Client-side vs Server-side Pagination](#10-client-vs-server)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Why Paginate?
Loading 1 million records into memory will crash your application and your user's browser. Pagination breaks the data into manageable "pages," reducing memory usage and network latency.

---

## 2. Offset Pagination (Page<T>)
The most common strategy. You request `page=2` and `size=10`.
- **SQL**: `SELECT * FROM table LIMIT 10 OFFSET 20`.
- **Pros**: Users can jump to a specific page (e.g., page 5).
- **Cons**: Issues an expensive `SELECT COUNT(*)` to calculate total pages.

---

## 3. Infinite Scroll Pagination (Slice<T>)
Used for "Load More" buttons or infinite scrolling.
- **SQL**: `SELECT * FROM table LIMIT 11 OFFSET 20`. (Fetches one extra record to see if a next page exists).
- **Pros**: No `COUNT(*)` query, making it faster than `Page`.
- **Cons**: User cannot jump to a specific page.

---

## 4. Keyset / Cursor Pagination (Window<T>)
Instead of an offset, you use the last ID from the previous page.
- **SQL**: `SELECT * FROM table WHERE id > 500 LIMIT 10`.
- **Pros**: Extremely fast! Constant performance regardless of how deep you go.
- **Cons**: Sorting must be done on a unique column (like ID). Harder to implement.

---

## 5. The Classic Interview Trap: Deep Pagination
**The Trap**: You use offset pagination (`LIMIT 10 OFFSET 1000000`).
**The Problem**: The database must scan and discard 1 million rows before returning the 10 you want. This is extremely slow.
**The Fix**: Use **Keyset/Cursor** pagination for large datasets.

---

## 6. The SELECT COUNT(*) Problem
On large tables (millions of rows), `COUNT(*)` can take seconds to run. If your UI doesn't strictly need "Total Pages" (e.g., infinite scroll), use `Slice` to avoid this query.

---

## 7. Sorting in Pagination
Always include a unique tie-breaker (like `id`) in your sort. If you sort by `name`, and two rows have the same name, they might appear on multiple pages or be skipped entirely as the database order isn't guaranteed.

---

## 8. Pagination with JOIN FETCH
**CRITICAL**: If you use `JOIN FETCH` with `Pageable`, Hibernate will fetch ALL records from the DB and perform pagination in Java memory. This is a massive performance killer. Use `@EntityGraph` or a two-step query instead.

---

## 9. Filtering in Paginated Queries
When adding `WHERE` clauses to paginated queries, ensure the filtered columns are indexed, or the `LIMIT` won't save you from a full table scan.

---

## 10. Client-side vs Server-side Pagination
- **Client-side**: Fetch all data once and paginate in Javascript. (Good for small lists < 1000 rows).
- **Server-side**: Fetch only the current page from the DB. (Essential for large datasets).

---

## 11. Common Mistakes
1. Not providing a default page size (risk of someone requesting `size=1000000`).
2. Hardcoding "0" as the first page (some APIs start at 1).
3. Using `Page` when `Slice` would be enough.

---

## 12. Quick-Fire Interview Q&A
**Q: How does Keyset pagination handle data inserts?**  
A: It is more stable. New records added at the beginning won't shift the data on your current page.  
**Q: What is a Window in Spring Data?**  
A: A new abstraction in Spring Data 3.x that supports keyset-based scrolling out of the box.
