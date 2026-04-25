# Java Memory Leaks — Complete Interview Reference

## Table of Contents
1. [What is a Memory Leak in Java?](#1-what-is-a-leak)
2. [How the Garbage Collector (GC) Works](#2-gc-basics)
3. [The Static Map Problem (Most Common Leak)](#3-static-maps)
4. [Unclosed Resources (Streams, Connections)](#4-unclosed-resources)
5. [Inner Classes and Anonymous Classes](#5-inner-classes)
6. [The Classic Interview Trap: String.intern()](#6-the-classic-interview-trap-strings)
7. [Detecting Leaks with VisualVM and JConsole](#7-detection-tools)
8. [Analyzing Heap Dumps (.hprof files)](#8-heap-dumps)
9. [Memory Leak vs Memory Pressure](#9-leak-vs-pressure)
10. [Using WeakReferences and SoftReferences](#10-references)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is a Memory Leak?
In Java, a memory leak happens when objects are no longer needed by the application, but they are still being referenced by some other object, preventing the Garbage Collector from reclaiming the memory.

---

## 2. How GC Works
The GC looks for objects that are "unreachable" from the **GC Roots** (Thread stacks, Static variables, etc.). If an object is still reachable, it stays in the heap. A leak occurs when we keep a reference to an object forever (e.g., in a static collection).

---

## 3. Static Maps
The #1 cause of memory leaks. Since static variables are GC roots, any object added to a static `Map` or `List` will live for the entire duration of the application unless explicitly removed.

---

## 4. Unclosed Resources
Not closing database connections, file streams, or network sockets can lead to memory exhaustion because the OS and the JVM keep buffers open for these handles.

---

## 5. Inner Classes
Non-static inner classes hold a hidden reference to their outer class. If you pass an inner class object to a long-lived service, the outer class (which might be a large Controller or Activity) will also be leaked.

---

## 6. The Classic Interview Trap: Proper Equals/HashCode
**The Trap**: You use a custom object as a key in a `HashMap`.
**The Problem**: If you don't override `equals` and `hashCode`, or if you change the fields used in `hashCode` after putting the object in the map, you can never retrieve it or remove it. The map will grow forever with duplicate "orphaned" keys.
**The Fix**: Always use immutable objects (like `String` or `Integer`) as map keys, or ensure your `hashCode` logic is stable.

---

## 7. Detection Tools
- **JConsole**: Basic real-time graphs of heap usage.
- **VisualVM**: Allows you to browse the heap and see which classes are consuming the most memory.
- **Eclipse MAT**: The most powerful tool for deep heap dump analysis.

---

## 8. Heap Dumps
A snapshot of the entire JVM memory at a point in time. Use `-XX:+HeapDumpOnOutOfMemoryError` to automatically generate one when your app crashes.

---

## 9. Leak vs Pressure
- **Leak**: Memory usage increases slowly over time and never goes down.
- **Pressure**: Memory usage is high but stays stable. This often means your heap is too small for the amount of data you are processing.

---

## 10. WeakReferences
A `WeakReference` does not prevent an object from being garbage collected. This is perfect for caches where you want to keep data as long as memory is available, but you don't want the cache itself to cause an `OutOfMemoryError`.

---

## 11. Common Mistakes
1. Using static collections as caches without an eviction policy (like TTL or Max Size).
2. Forgetting to clear `ThreadLocal` variables (especially in application servers with thread pools).
3. Not using `try-with-resources` for I/O.

---

## 12. Quick-Fire Interview Q&A
**Q: Can a Java application have a memory leak?**  
A: Yes, it is a very common issue despite having an automatic Garbage Collector.  
**Q: What is the most common symptom of a leak?**  
A: Increasing frequency of GC pauses (GC Thrashing) followed eventually by an `java.lang.OutOfMemoryError: Java heap space`.
