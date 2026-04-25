# @ModelAttribute Masterclass — Complete Interview Reference

## Table of Contents
1. [What is @ModelAttribute?](#1-what-is-modelattribute)
2. [Method-Level @ModelAttribute](#2-method-level)
3. [Parameter-Level @ModelAttribute](#3-parameter-level)
4. [@ModelAttribute vs @RequestBody](#4-vs-requestbody)
5. [The Classic Interview Trap: Execution Order](#5-the-classic-interview-trap-order)
6. [Binding Form Data (URL Encoded)](#6-binding-form-data)
7. [Data Binding and Validation](#7-binding-validation)
8. [Using @ModelAttribute in @ControllerAdvice](#8-controller-advice)
9. [Implicit vs Explicit Naming](#9-naming)
10. [Handling Nested Objects in Forms](#10-nested-objects)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is @ModelAttribute?
`@ModelAttribute` is used to bind a method parameter or a method return value to a named model attribute. It is primarily used in traditional web applications (JSP/Thymeleaf) but has specific use cases in REST APIs for form submission.

---

## 2. Method-Level @ModelAttribute
When you place this annotation on a method, Spring will call that method **BEFORE** any of the `@RequestMapping` methods in the same controller. The return value is added to the `Model`.
**Use Case**: Pre-populating a list of "Countries" or "Roles" for a dropdown menu.

---

## 3. Parameter-Level @ModelAttribute
When used on a method parameter, it indicates that the parameter should be retrieved from the model. If it's not present in the model, it should be instantiated and then populated from the request parameters (query string or form data).

---

## 4. @ModelAttribute vs @RequestBody
- **@RequestBody**: Used for JSON/XML. It uses `HttpMessageConverters` to deserialize the body.
- **@ModelAttribute**: Used for `application/x-www-form-urlencoded`. It uses a `DataBinder` to map request parameters to Java fields.

---

## 5. The Classic Interview Trap: Order of Execution
**The Trap**: You have a `@ModelAttribute` method and a `@GetMapping` method. You want to use the attribute in the GET method.
**The Problem**: Developers often forget that the `@ModelAttribute` method runs every time, even for unrelated requests in the same controller. This can lead to unnecessary database calls if not managed carefully.
**The Fix**: Keep `@ModelAttribute` methods lightweight or use them inside a specific `@ControllerAdvice` to share data across multiple controllers.

---

## 6. Binding Form Data
`@ModelAttribute` is the standard way to handle HTML `<form>` submissions. It automatically converts strings from the request into complex Java types (Dates, Integers, etc.) using the `ConversionService`.

---

## 7. Validation
You can use `@Valid` or `@Validated` alongside `@ModelAttribute`. Any errors during binding or validation will be placed in the `BindingResult` argument.

---

## 8. Global @ModelAttribute
If you define a `@ModelAttribute` method inside a `@ControllerAdvice`, that attribute will be available to **ALL** controllers in your application.

---

## 9. Naming
By default, Spring uses the camelCase version of the class name (e.g., `UserForm` becomes `userForm`). You can override this using the annotation value: `@ModelAttribute("myCustomName")`.

---

## 10. Nested Objects
`@ModelAttribute` supports nested property binding. If your form has a field named `address.city`, Spring will call `form.getAddress().setCity("...")`.

---

## 11. Common Mistakes
1. Using `@ModelAttribute` to try and parse JSON (it won't work).
2. Forgetting that the method-level version runs for every single request in the controller.
3. Not handling `BindingResult`, which leads to generic 400 errors instead of specific field error messages.

---

## 12. Quick-Fire Interview Q&A
**Q: Can I use @ModelAttribute in a @RestController?**  
A: Yes, but it's less common. It's useful if you need to support both JSON (`@RequestBody`) and traditional Form Post (`@ModelAttribute`) for the same endpoint.  
**Q: Does @ModelAttribute require a no-args constructor?**  
A: Yes, or a constructor with parameters that match the request parameters.
