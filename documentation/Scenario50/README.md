# Scenario 50: GraphQL Advanced (Batching & Validation)

## 1. Batch Mapping (@BatchMapping)
The **N+1 problem** is the most common performance issue in GraphQL. 
- **Problem**: If you fetch 10 projects and each project has members, a standard resolver would call `getMembers()` 10 times.
- **Solution**: `@BatchMapping` collects all the projects first and makes **one single call** to fetch members for all of them.

### Testing Batching
Run this in GraphiQL and check your server console. You will see "--- BATCH LOADING MEMBERS ---" printed **only once**, even though we fetch multiple projects.
```graphql
query {
  allProjects {
    name
    members {
      name
      role
    }
  }
}
```

---

## 2. Input Validation
In GraphQL, we use **Input Objects** for mutations. By using standard JSR-303 annotations (`@NotBlank`, `@Size`) and the `@Valid` annotation in the controller, we can enforce strict data rules.

### Testing Validation
Try adding a project with a name that is too short.
```graphql
mutation {
  addProject(input: { name: "Ab", description: "Too short name" }) {
    id
  }
}
```
**Response**: You will receive a structured `errors` array in the JSON response explaining that the name must be at least 3 characters.

---

## 3. Masterclass Interview Q&A

**Q: How does @BatchMapping differ from a DataLoader?**  
A: `@BatchMapping` is a high-level Spring abstraction that simplifies the use of `DataLoader`. While a `DataLoader` is more flexible (it can be used anywhere in the code), `@BatchMapping` is specifically designed for controller methods and is much easier to read and maintain.

**Q: Where are validation errors displayed in GraphQL?**  
A: Unlike REST which uses HTTP status codes (like 400 Bad Request), GraphQL usually returns a **200 OK** but includes an `errors` list in the response body. This allows the client to see exactly which fields failed validation.

**Q: Can you use @BatchMapping for deep nesting?**  
A: Yes. You can chain batch mappings. For example, you can batch load `Members` for `Projects`, and then batch load `Skills` for those `Members`.
