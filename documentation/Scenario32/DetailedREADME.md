# JPA Entity Graphs — Complete Interview Reference

## Table of Contents
1. [What is an Entity Graph?](#1-what-is-an-entity-graph)
2. [Eager vs Lazy Loading Recap](#2-loading-recap)
3. [The Problem with Global Eager Loading](#3-global-eager-problem)
4. [Named Entity Graphs (@NamedEntityGraph)](#4-named-graphs)
5. [The Classic Interview Trap: Cartesian Product Problem](#5-the-classic-interview-trap-cartesian)
6. [Ad-hoc Entity Graphs (@EntityGraph annotation)](#6-ad-hoc-graphs)
7. [Fetch Graphs vs Load Graphs](#7-fetch-vs-load)
8. [Subgraphs for Deep Nesting](#8-subgraphs)
9. [Dynamic Entity Graphs (Programmatic API)](#9-dynamic-graphs)
10. [Using Entity Graphs with Specifications](#10-specifications)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is an Entity Graph?
An Entity Graph is a template used to define which fields and relationships should be fetched eagerly for a specific query, overriding the default `FetchType.LAZY` setting.

---

## 2. Eager vs Lazy Loading Recap
- **LAZY**: Load only the main object. Load children only when accessed (can lead to N+1).
- **EAGER**: Load everything in one go.

---

## 3. The Problem with Global Eager Loading
If you set `@ManyToOne(fetch = FetchType.EAGER)` in your entity, it applies to **EVERY** query. This causes massive performance issues when you only need a list of names but JPA fetches 5 related tables for every name.

---

## 4. Named Entity Graphs
You define the graph on the Entity class using `@NamedEntityGraph`.
```java
@NamedEntityGraph(name = "Customer.orders", attributeNodes = @NamedAttributeNode("orders"))
@Entity public class Customer { ... }
```

---

## 5. The Classic Interview Trap: Cartesian Product
**The Trap**: You fetch two collections (`orders` and `addresses`) using a single `JOIN FETCH` or Entity Graph.
**The Problem**: The database returns a "Cartesian Product" (e.g., if a user has 10 orders and 5 addresses, you get 50 rows). This results in duplicates and huge memory consumption.
**The Fix**: Only fetch one collection eagerly using a graph, and use `@BatchSize` or a separate query for the other.

---

## 6. Ad-hoc Entity Graphs
In Spring Data JPA, you can use the `@EntityGraph` annotation directly on a repository method.
```java
@EntityGraph(attributePaths = {"orders"})
List<Customer> findAll();
```

---

## 7. Fetch Graphs vs Load Graphs
- **FETCH**: Only the attributes specified in the graph are eager; all others are lazy.
- **LOAD**: Attributes in the graph are eager; all others maintain their default fetch type.

---

## 8. Subgraphs for Deep Nesting
If you need to fetch `Customer -> Orders -> OrderItems`, you can use a **Subgraph** to define the nesting level.

---

## 9. Dynamic Entity Graphs
You can build a graph programmatically using the `EntityManager`:
```java
EntityGraph graph = entityManager.createEntityGraph(Customer.class);
graph.addAttributeNodes("orders");
```

---

## 10. Using Entity Graphs with Specifications
To combine dynamic filtering with entity graphs, you must pass the graph as a "hint" to the `findAll` method of the repository.

---

## 11. Common Mistakes
1. Using Entity Graphs for every query (defeats the purpose of Lazy loading).
2. Forgetting that Entity Graphs only work for JPA-managed entities (not DTOs).
3. Trying to fetch multiple `@OneToMany` collections in a single graph (Cartesian product).

---

## 12. Quick-Fire Interview Q&A
**Q: What is the benefit of @EntityGraph over JOIN FETCH?**  
A: `@EntityGraph` is more declarative and easier to apply to Spring Data JPA repository methods without writing custom JPQL.  
**Q: Can we use Entity Graphs with native queries?**  
A: No. Entity Graphs are part of the JPA persistence context logic and don't apply to native SQL.
