# Scenario 16: Threading & Long Processes

Demonstrates how Spring Boot handles long-running requests and potential threading issues.

## Concept
Web servers have a limited number of threads (Tomcat default is 200). If many requests take 15 seconds to complete, the server will quickly run out of threads and stop accepting new connections (Thread Starvation).

## Implementation Details
The controller simulates a 15-second "Heavy Task" using `Thread.sleep()`.

### Code:
```java
@GetMapping("/long-process")
public String longProcess() throws InterruptedException {
    Thread.sleep(15000); 
    return "Finished!";
}
```

## Verification Results
- **Test**: Open two browser tabs and hit the URL at once.
- **Observation**: Both tabs will spin for 15 seconds. If you hit it 201 times simultaneously, the 201st user will experience a delay or timeout.

## Interview Theory: Scalability
- **Async Servlets**: For long processes, use `DeferredResult` or `Callable`. This releases the Tomcat thread back to the pool while the task runs in the background.
- **Reactive Stack**: If your app is 90% long-running IO tasks, consider using **Spring WebFlux** (Netty), which uses a non-blocking event loop instead of "one thread per request."
- **Timeouts**: Always configure a `server.tomcat.connection-timeout` to kill orphaned requests.
