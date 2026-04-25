# One-to-Many & Many-to-One Relationships — Complete Interview Reference

## Table of Contents
1. [Understanding One-to-Many vs Many-to-One](#1-understanding)
2. [Owning Side vs Non-Owning Side](#2-owning-side)
3. [The Role of mappedBy](#3-mapped-by)
4. [Bidirectional Mapping](#4-bidirectional)
5. [The Classic Interview Trap: Syncing both sides](#5-the-classic-interview-trap-sync)
6. [Lazy Loading and the N+1 Problem](#6-lazy-loading)
7. [JoinTable vs JoinColumn](#7-join-strategies)
8. [Orphan Removal Recap](#8-orphan-removal)
9. [Circular References in JSON Serialization](#9-json-recursion)
10. [Using Set vs List for Collections](#10-set-vs-list)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. Understanding the Relationship
- **Many-to-One**: Many students belong to one class. This is the most common mapping. The foreign key (`class_id`) sits in the `Student` table.
- **One-to-Many**: One class has many students. It is often the inverse of a many-to-one relationship.

---

## 2. Owning Side
In JPA, the "Owning Side" is responsible for updating the foreign key in the database. 
**Rule of thumb**: The side that has the `@JoinColumn` is the owning side. In a Student/Class relationship, the `Student` is almost always the owning side.

---

## 3. The mappedBy Attribute
The `mappedBy` attribute is used on the non-owning side (the Class) to point to the field in the owning entity (the Student) that defines the relationship.
`@OneToMany(mappedBy = "schoolClass")`

---

## 4. Bidirectional Mapping
Allows you to navigate the relationship in both directions: `student.getSchoolClass()` and `schoolClass.getStudents()`.

---

## 5. The Classic Interview Trap: Syncing
**The Trap**: You add a student to a class: `schoolClass.getStudents().add(student)`. Then you save the class.
**The Problem**: The foreign key in the database will be `NULL`. Because the `Student` is the owning side, JPA only looks at `student.setSchoolClass(schoolClass)` to determine what to write to the DB.
**The Fix**: Always create a helper method in the Parent to update both sides:
```java
public void addStudent(Student student) {
    this.students.add(student);
    student.setSchoolClass(this);
}
```

---

## 6. Lazy Loading
By default, `@OneToMany` is `LAZY` (students aren't fetched until you call `getStudents()`), while `@ManyToOne` is `EAGER`. Be careful with EAGER loading as it can cause performance issues.

---

## 7. JoinTable vs JoinColumn
- **JoinColumn**: Adds a foreign key directly to the child table. (Most efficient).
- **JoinTable**: Uses a separate link table to connect the two. (Necessary for Many-to-Many, optional for One-to-Many).

---

## 8. JSON Recursion
If you try to serialize a bidirectional relationship to JSON, you will get a `StackOverflowError` because Class has Students, and Student has a Class.
**The Fix**: Use `@JsonManagedReference` on the Parent and `@JsonBackReference` on the Child, or use DTOs.

---

## 9. Set vs List
- **Set**: Prevents duplicates. Generally preferred for relationships.
- **List**: Allows duplicates and maintains order. However, using a `List` with `mappedBy` can lead to inefficient SQL updates in some Hibernate versions.

---

## 10. Performance (The N+1 Problem)
If you fetch 10 classes and then iterate through their students, Hibernate might execute 1 query for classes and then 10 separate queries for students. Use `JOIN FETCH` to solve this.

---

## 11. Common Mistakes
1. Not setting `mappedBy`, which results in JPA creating an unnecessary join table.
2. Forgetting to update both sides of the relationship in Java code.
3. Over-using `EAGER` fetching.

---

## 12. Quick-Fire Interview Q&A
**Q: Which side should be the owning side?**  
A: The side that holds the foreign key (the "Many" side).  
**Q: Can a relationship be One-to-Many only (Unidirectional)?**  
A: Yes, but it usually requires a join table and is less efficient than a bidirectional mapping with a foreign key.
