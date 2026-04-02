# Scenario 108: AI Observability 🔎📊

## 🎯 Goal
In production, you can't just ship an AI and hope for the best. You need to manage **costs**, monitor **performance**, and protect **data privacy**.

This scenario teaches you how to:
1.  **Token Counting**: Extract prompt, completion, and total tokens from every response.
2.  **Actuator Metrics**: Expose AI performance metrics via `/actuator/metrics`.
3.  **Data Redaction**: Automatically scrub PII (emails, IDs) before they reach logs or the model.

---

## 🎭 The Analogy: The Management Dashboard 📈

Imagine running a high-end restaurant:
- **Scenario 102** was just learning how to cook.
- **Scenario 108** is the **Management Dashboard**. It's the system that tracks exactly how much flour you used (**tokens**), how long the chef took to plate (**latency**), and audits whether the waiters are leaking customer credit card info (**PII redaction**).

---

## 🏗️ Implementation Details

### 1. Token Tracking
We extract token usage from the `ChatResponse` metadata.

```java
ChatResponse response = chatClient.prompt().user(message).call().chatResponse();
Usage usage = response.getMetadata().getUsage();
// usage.getPromptTokens()  -> Cost of your question
// usage.getCompletionTokens() -> Cost of the AI's answer
```

### 2. Metrics (Spring Boot Actuator)
By adding `spring-boot-starter-actuator`, Spring AI automatically reports data to Micrometer.
- **`spring.ai.chat.client.calls`**: Total number of AI requests.
- **`spring.ai.chat.client.duration`**: How long the AI took to respond.

To see these, expose them in `application.properties`:
```properties
management.endpoints.web.exposure.include=metrics,health
```

### 3. Redaction (The Bouncer)
We use a **`ChatClient` Advisor** to intercept the prompt and replace sensitive info (like emails) with `[REDACTED]`.

---

## 🧪 How to Test

### 1. Call the tracked chat
```bash
curl "http://localhost:8081/spring-ai/api/scenario108/chat?message=Tell me a short joke"
```
**Expected Result**: You'll get a JSON response showing the answer AND the token breakdown.

### 2. Check the Actuator Dashboard
```bash
curl "http://localhost:8081/spring-ai/actuator/metrics/spring.ai.chat.client.calls"
```
**Expected Result**: A JSON showing the count of calls made.

### 3. Test Redaction
```bash
curl "http://localhost:8081/spring-ai/api/scenario108/redact?message=My email is secret@gmail.com"
```
**Expected Result**: The response should confirm the email was redacted before the AI saw it (or in the logs).

---

## 💡 Production Tip
- **Cost Alerting**: Use these token metrics to build a "Circuit Breaker" that stops AI calls if a user exceeds a daily budget.
- **Privacy First**: Always redact PII *before* it leaves your infrastructure. Most AI providers (like Google/OpenAI) have their own safety filters, but you should never rely solely on them for data privacy compliance.
