# Scenario 46: Spring Boot Docker Compose Support Masterclass

## Table of Contents
1. [Overview](#1-overview)
2. [The "Zero-Config" Revolution](#2-the-zero-config-revolution)
3. [How it Works Under the Hood](#3-how-it-works)
4. [Service Discovery & Mapping](#4-service-discovery)
5. [Key Configuration Properties](#5-key-properties)
6. [Docker Compose vs Testcontainers](#6-docker-compose-vs-testcontainers)
7. [Common Mistakes](#7-common-mistakes)
8. [Masterclass Interview Q&A](#8-masterclass-interview-qa)

---

## 1. Overview
In the past, to develop locally, you had to manually run `docker-compose up`, then copy the ports and passwords into your `application.properties`. If the port changed, your app broke.

**Spring Boot Docker Compose Support** (introduced in 3.1) automates this. The application now "owns" its infrastructure lifecycle.

---

## 2. The "Zero-Config" Revolution
When you add the `spring-boot-docker-compose` dependency:
1. Spring Boot looks for `compose.yaml` on startup.
2. It runs `docker compose up`.
3. It **automatically overrides** your connection properties (URL, Port, Username, Password) with the ones from the running container.
4. You don't need to write a single line of configuration for Postgres, MySQL, Redis, MongoDB, etc.

---

## 3. How it Works Under the Hood
Spring Boot parses the `compose.yaml` file and identifies the images being used. If it sees `postgres:latest`, it knows it needs to provide connection details for a `DataSource`. It uses **Service Connection Creators** to map Docker metadata to Spring environment properties.

---

## 4. Service Discovery & Mapping
Spring Boot supports many standard images out of the box:
- **Redis**: `redis`, `redis/redis-stack`
- **Postgres**: `postgres`
- **MySQL**: `mysql`
- **MongoDB**: `mongo`
- **Kafka**: `confluentinc/cp-kafka`, `bitnami/kafka`

---

## 5. Key Configuration Properties
You can control the behavior in `application.properties`:
- `spring.docker.compose.enabled`: Set to `false` to disable.
- `spring.docker.compose.lifecycle-management`: 
    - `start-and-stop`: (Default) Starts on app start, stops on app exit.
    - `start-only`: Useful if you want the DB to keep running after the app stops.
- `spring.docker.compose.profiles.active`: If you have multiple profiles in your compose file.

---

## 6. Docker Compose vs Testcontainers
- **Docker Compose Support**: Best for **Development**. It's fast and persistent during your coding session.
- **Testcontainers**: Best for **Testing**. It provides a clean, isolated environment for every test execution but has more overhead for daily development.

---

## 7. Common Mistakes
1. **Docker Not Running**: The app will fail to start if the Docker daemon is not active.
2. **Port Conflicts**: If you have a manual Postgres running on 5432 and Docker Compose tries to start another one on 5432, it will fail.
3. **Skipping Dependency**: Forgetting to add `spring-boot-docker-compose` to your `pom.xml`.

---

## 8. Masterclass Interview Q&A
**Q: Does Spring Boot Docker Compose work in Production?**  
A: No. It is specifically designed for development and testing. It ignores the `compose.yaml` file if you run the app as an executable JAR (`java -jar`) in a production environment.

**Q: Can I use a custom image?**  
A: Yes, but Spring Boot might not know how to automatically extract connection details from it. In that case, you might need to use `org.springframework.boot.configurationserver.detector.DockerComposeServiceDetector`.

**Q: What happens to my data when I stop the app?**  
A: By default, it runs `docker compose stop` (not `down -v`), so your data in Docker volumes is preserved between restarts.
