# Quartz Scheduler — Complete Interview Reference

## Table of Contents
1. [What is Quartz?](#1-introduction)
2. [Quartz vs Spring @Scheduled](#2-quartz-vs-spring)
3. [The Three Pillars: Job, JobDetail, and Trigger](#3-three-pillars)
4. [JobDataMap: Passing Parameters](#4-job-data-map)
5. [The Classic Interview Trap: Job Instance Serialization](#5-the-classic-interview-trap-serialization)
6. [Quartz Clustering and Persistence](#6-clustering)
7. [CronTriggers vs SimpleTriggers](#7-triggers)
8. [Misfire Instructions](#8-misfire)
9. [Quartz Interruptions and Listeners](#9-listeners)
10. [Using Quartz with Spring Dependency Injection](#10-spring-di)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is Quartz?
Quartz is a richly featured, open source job scheduling library that can be integrated within virtually any Java application. It is the enterprise standard for complex scheduling needs.

---

## 2. Quartz vs Spring @Scheduled
- **@Scheduled**: Simple, memory-based, no clustering (unless using ShedLock), no job history.
- **Quartz**: Persistent (stores jobs in DB), supports complex schedules, natively supports clustering, and provides sophisticated error handling/misfire logic.

---

## 3. The Three Pillars
1. **Job**: The interface you implement containing the execution logic.
2. **JobDetail**: An instance of a job with specific properties (like name and group).
3. **Trigger**: Defines the schedule (When the job runs).

---

## 4. JobDataMap
A map that allows you to pass data to the job instance. You can put Strings, Integers, or even serialized objects in this map when defining the `JobDetail`.

---

## 5. The Classic Interview Trap: Dependency Injection in Jobs
**The Trap**: You try to `@Autowired` a service inside your Quartz `Job` class, but it is always `null`.
**The Problem**: By default, Quartz instantiates Job classes using its own `JobFactory`, which doesn't know about the Spring Application Context.
**The Fix**: You must configure a `SpringBeanJobFactory` and set it as the job factory in the `SchedulerFactoryBean`.

---

## 6. Clustering and Persistence
Quartz can store its state in a database (JDBC JobStore). This allows multiple nodes to coordinate. If one node fails, another node in the cluster will pick up the job. This is much more powerful than ShedLock.

---

## 7. Triggers
- **SimpleTrigger**: For simple intervals (e.g., "every 10 seconds, repeat 5 times").
- **CronTrigger**: For calendar-based schedules (e.g., "at 10:15 AM on the last Friday of every month").

---

## 8. Misfire Instructions
What happens if a job was supposed to run at 2:00 PM but the server was down? Quartz allows you to define "Misfire Instructions" (e.g., "Run immediately once", "Skip this execution", or "Fire all missed executions").

---

## 9. Listeners
Quartz provides `JobListener` and `TriggerListener` interfaces that allow you to "spy" on the scheduler and perform actions (like logging or alerting) before or after a job runs.

---

## 10. Spring Boot Integration
Spring Boot provides a `spring-boot-starter-quartz` which automatically configures the `Scheduler` and picks up any `JobDetail` or `Trigger` beans you define in your configuration classes.

---

## 11. Common Mistakes
1. Not setting `spring.quartz.job-store-type=jdbc` (default is `memory`, which is not persistent).
2. Forgetting to create the 11 standard Quartz database tables (QRTZ_*).
3. Putting massive amounts of data in the `JobDataMap` (it is stored as a BLOB in the DB).

---

## 12. Quick-Fire Interview Q&A
**Q: Can a Job have multiple Triggers?**  
A: Yes. One `JobDetail` can be associated with any number of `Triggers`.  
**Q: What is @DisallowConcurrentExecution?**  
A: It is an annotation for Job classes that prevents multiple instances of the same job from running at the same time (even if the trigger fires).
