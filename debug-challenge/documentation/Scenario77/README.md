# Scenario 77: Spring Boot Actuator & Monitoring

## Overview
**Spring Boot Actuator** provides production-ready features to help you monitor and manage your application. It uses HTTP endpoints to expose operational information about the running application.

## Key Endpoints 🚀

| Endpoint | Description |
| :--- | :--- |
| `/actuator/health` | Shows application health information (e.g., DB connection, Disk space). |
| `/actuator/info` | Displays arbitrary application information. |
| `/actuator/metrics` | Shows metrics like memory usage, CPU, and HTTP requests. |
| `/actuator/loggers` | Allows viewing and **modifying** log levels at runtime. |
| `/actuator/env` | Exposes properties from the Spring `Environment`. |
| `/actuator/beans` | Displays a complete list of all Spring beans in the context. |

## Custom Actuator Endpoints 🛠️
You can create your own endpoints using the `@Endpoint` annotation. 
- **@ReadOperation**: Mapped to HTTP `GET`.
- **@WriteOperation**: Mapped to HTTP `POST`.
- **@DeleteOperation**: Mapped to HTTP `DELETE`.

In this scenario, we created `/actuator/system-status` to expose custom business metrics like "active scenarios" and "app status".

## Security Best Practices 🛡️
1. **Exposure Control**: Never expose all endpoints by default. Use `management.endpoints.web.exposure.include` to select only what you need.
2. **Role-Based Access**: Always secure `/actuator/**` with a role like `ROLE_ADMIN`. Public liveness/readiness probes should only have access to `/actuator/health`.
3. **Sensitive Data**: Be careful with `/actuator/env` and `/actuator/heapdump` as they can contain secrets or sensitive memory data.

## Interview Tips 💡
- **How do you change the base path of actuator?** Use `management.endpoints.web.base-path=/management`.
- **Difference between Liveness and Readiness?** Liveness tells if the app is running; Readiness tells if it's ready to handle traffic (e.g., DB initialized). Spring Boot supports these via `/actuator/health/liveness` and `/actuator/health/readiness`.
- **Can you change log levels without restarting?** Yes, using the `/actuator/loggers` endpoint with a `POST` request.

## Testing the Scenario
1. **Public Health**: `GET /actuator/health`. Should show detailed status.
2. **Custom Status**: `GET /actuator/system-status`.
3. **List All Metric Names**: `GET /actuator/metrics`.
4. **View Specific Metric Data**: 
   - To see CPU usage: `GET /actuator/metrics/system.cpu.usage`
   - To see our **Custom Counter**: `GET /actuator/metrics/app.monitoring.access.count`

## Theory: Micrometer & Custom Metrics 📈
Actuator uses **Micrometer** as its metrics facade. 
- We injected `MeterRegistry` into our custom endpoint.
- We used `meterRegistry.counter("name").increment()` to track activity.
- These metrics are automatically aggregated and exposed via the `/actuator/metrics` endpoint.
