# 🧑‍🔬 Scenario 102: The First Handshake

The goal of this scenario is to establish a successful connection between your **Spring Boot** application and an **LLM (Large Language Model)**—in this case, **Google Gemini 2.5 Flash**.

---

## 🏗️ Core Concept: `ChatClient`

The `ChatClient` is the primary abstraction in Spring AI for interacting with chat-based models. It provides a **Fluent API** that allows you to chain commands together to build a prompt and receive a response.

### Why use `ChatClient`?
Instead of writing complex HTTP clients to talk to Gemini's REST API, `ChatClient` handles:
1.  **Authentication**: Automatically uses the API key from your properties.
2.  **Marshalling**: Converts your Java strings into the JSON format Gemini expects.
3.  **Unmarshalling**: Parses the JSON response back into a simple Java `String`.

---

## 🛠️ How it works under the hood

When the application starts, the **Spring AI Starter** performs these steps:

1.  **Detects API Key**: Scans `application.properties` for `spring.ai.google.genai.api-key`.
2.  **Configures Model**: Sets up the model (e.g., `gemini-2.5-flash`).
3.  **Injects Builder**: Provides a `ChatClient.Builder` bean that you can inject into any controller or service.

### The Implementation
```java
public Scenario102Controller(ChatClient.Builder builder) {
    // We build the actual client from the pre-configured builder
    this.chatClient = builder.build(); 
}

public String ask(String prompt) {
    return chatClient.prompt()
            .user(prompt)    // Sets the user's message
            .call()          // Synchronously calls the Gemini API
            .content();      // Extracts only the text from the response
}
```

---

## 🧪 Testing the Integration

### 1. The Success Flow
When you call `/api/scenario102/ask?prompt=Test`, the request flows like this:
`Controller` ➔ `ChatClient` ➔ `Spring AI Adapter` ➔ `Google Gemini API` ➔ `Model Output`

### 2. Common Issues (What could go wrong?)
> [!WARNING]
> **401 Unauthorized**: Your API Key is invalid or not found in environment variables.
> **429 Too Many Requests**: You have exceeded the free-tier rate limits for Gemini.
> **503 Service Unavailable**: The Gemini API service is currently down or overloaded.

---

## 🌟 Concept Mastery: Prompting
At its simplest, **Prompting** is just sending a string of text. In later scenarios, we will move from "Dynamic Strings" to **Prompt Templates**, where we can structure the AI's personality and instructions.

> [!NOTE]
> This is a **Sync** call. The application waits while the AI generates a response. In advanced scenarios, we can use **Streaming** for a real-time experience!
