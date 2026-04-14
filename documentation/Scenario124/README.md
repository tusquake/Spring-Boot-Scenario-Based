# Scenario 124: Memory Leak Detection in Spring Boot

A **Memory Leak** in Java occurs when objects are no longer needed by the application but are still referenced by a GC Root (like a static variable), preventing the Garbage Collector from reclaiming their memory.

## How to Trigger
1. Start the application.
2. Hit the leak endpoint multiple times to occupy heap space:
   `GET http://localhost:8080/debug-application/scenario124/leak?count=50`
   (This leaks 50MB per call)
3. Check the status:
   `GET http://localhost:8080/debug-application/scenario124/status`

## How to Analyze

### Step 1: Monitor Memory
Use `jconsole` or `VisualVM` to connect to the process and watch the "Heap Memory Usage" graph. After hitting the leak endpoint, you'll see the "base" memory consumption (the valleys after GC) increases.

### Step 2: Generate a Heap Dump
Use Spring Boot Actuator to download a heap dump:
`http://localhost:8080/debug-application/actuator/heapdump`

This will download a large binary file (usually named `heapdump` or `heapdump.hprof`).

### Step 3: Analyze with Eclipse MAT (Memory Analyzer Tool)
1. Open the heap dump in **Eclipse MAT**.
2. Run the **"Leak Suspects"** report.
3. Look at "Problem Suspect 1". It will likely point to:
   `com.interview.debug.service.Scenario124MemoryLeakService`
4. Use the **"Path to GC Roots"** -> **"exclude all phantom/weak/soft references"** to see why the object is being held.
5. You will see that the `LEAK_CACHE` (a `static HashMap`) is holding onto thousands of `byte[]` objects.

## The Fix
To fix this specific leak:
1. Avoid using `static` collections for caching unless you use a library with an eviction policy (like Caffeine or Ehcache).
2. If using a Map as a cache, use a **WeakHashMap** (if appropriate) or manually clear it periodically.
3. Use specialized caching annotations like `@Cacheable`.

---

## 💡 Interview Q&A: Deep Dive into `static`

**Q: Why are `static` variables not Garbage Collected?**  
A: Garbage Collection (GC) only removes objects that are unreachable from any "GC Root." `static` fields are variables of the **Class** object. As long as the **ClassLoader** that loaded the class is alive, the class is alive, and so are its `static` fields. In a typical Spring application, the ClassLoader lives for the duration of the process.

**Q: What is the use case for `static` then?**  
A: Use `static` for:
- **Constants**: `public static final String KEY = "value";`
- **Utility Methods**: Stateless logic like `Math.abs()`.
- **Global Resources**: Shared thread pools or configuration that truly must span all instances.

**Q: Should we avoid `static`?**  
A: For mutable data (Maps, Lists, etc.), **YES**. In modern Spring apps, prefer **Dependency Injection (DI)**. Making a collection a field in a `@Service` bean allows Spring to manage its lifecycle better and makes it far easier to reset or mock during unit testing.

---

## Technical Details
This scenario uses a `static HashMap<String, byte[]>`. Since the `HashMap` is static, it lives in the "Metaspace" or is reachable from the ClassLoader, making it a **GC Root**. Any object added to it remains in memory until the map is cleared or the application restarts.

