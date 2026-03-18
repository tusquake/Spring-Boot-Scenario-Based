# Scenario 24: Distributed Tracing (Observability)

Demonstrates how to track a single user request as it moves through multiple internal methods and microservices.

## Concept
In microservices, it's hard to find logs for a specific request. **Distributed Tracing** assigns a unique **Trace ID** to every request. Every log line generated for that request includes this ID.

## Implementation Details
Using Spring Cloud Sleuth (or Micrometer Tracing), the ID is automatically injected into the **MDC** (Mapped Diagnostic Context) of the logger.

### Code Snippet:
```java
logger.info("Received request to trace-me endpoint");
doSomeProcessing();
logger.info("Finished processing request");
```

## Verification Results
- **Check Logs**:
  ```text
  [debug-challenge, 5f2e8..., f1a2b...] INFO: Received request...
  [debug-challenge, 5f2e8..., f1a2b...] DEBUG: Starting some internal processing...
  ```
- **Observation**: The ID `5f2e8...` remains identical across all lines, making it easy to `grep` for the entire request lifecycle.

## Interview Theory: Observability
- **Trace ID vs Span ID**: A **Trace** is the whole journey. A **Span** represents one unit of work (like one method call or one DB query).
- **Zipkin/Jaeger**: These are visualization tools where you can send your traces to see a timeline of where time was spent (Gantt charts).
- **Log Correlation**: Mention that tracing is useless unless your log aggregator (ELK/Splunk) can parse and index the Trace ID field.
