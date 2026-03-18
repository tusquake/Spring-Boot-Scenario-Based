# Scenario 26: Reactive Programming (Mono & Flux)

Demonstrates non-blocking, event-driven APIs using Project Reactor.

## Concept
Instead of the "One Thread per Request" model, Reactive systems use an event loop. This allows much higher scalability for IO-bound tasks by not waiting (blocking) for data to arrive.

## Implementation Details
- **Mono**: Returns 0 or 1 result.
- **Flux**: Returns 0 to N results (a stream).
- **SSE (Server-Sent Events)**: Allows the server to push data to the client as it becomes available.

### Code Snippet:
```java
@GetMapping(value = "/flux-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<Integer> getStream() {
    return Flux.range(1, 10).delayElements(Duration.ofSeconds(1));
}
```

## Verification Results
- **Mono Test**: `/api/scenario26/mono` -> Returns after 500ms delay.
- **Flux Test**: `/api/scenario26/flux-stream`
- **Result**: The browser doesn't wait 10 seconds. You see one number appear every second, proving the data is being "streamed" in real-time.

## Interview Theory: Reactive Stack
- **WebFlux vs MVC**: WebFlux uses Netty (event-based), MVC uses Tomcat (thread-based).
- **Backpressure**: The ability for a subscriber to tell the producer to "slow down" if it can't handle the data volume.
- **When NOT to use**: Don't use Reactive for simple CRUD apps with blocking JDBC drivers (JPA). It only makes the code more complex without any performance gain.
