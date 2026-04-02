# 🤖 Spring AI Learning Scenarios

> **A hands-on journey through building intelligent Java applications with Spring AI and Google Gemini.**
> Each scenario is a self-contained lesson. Start from 102 and work your way through — each one builds on the last.

---

## 🗺️ How to Use This Guide

Think of this guide like **learning to cook in a professional kitchen**. You start by learning how to turn on the stove (basic integration), then progress to plating full dishes (RAG pipelines, function calling, observability). Every scenario is a new technique. By Scenario 108, you're running the whole kitchen.

---

## Scenario 102 — Spring AI: First Handshake

**Topics:** `ChatClient`, `Gemini Integration`, `Streaming`

### 🌍 Real-World Analogy
Imagine **calling a brilliant consultant on the phone for the first time**. You dial the number (configure the API key), say hello (send a prompt), and listen to their response (receive a reply). Streaming is like the consultant thinking out loud as they speak — you hear the answer being built in real time, word by word, instead of waiting for them to finish before saying anything.

### 📖 What You'll Learn
- How to wire up `ChatClient` in a Spring Boot application
- How to send a prompt to Google Gemini and receive a response
- The difference between a **blocking call** (wait for full response) and **streaming** (receive tokens as they arrive)
- How to expose a REST endpoint that returns an AI-generated answer

### 🔑 Key Concepts

| Concept | Description |
|---|---|
| `ChatClient` | The main Spring AI class for talking to LLMs — think of it as your AI phone handset |
| `Gemini Integration` | Google's LLM wired in via `spring-ai-starter-model-google-genai` |
| `Streaming` | Returns a `Flux<String>` — a reactive stream of tokens as Gemini generates them |

### 💡 When Would You Use This?
Any time you need to add basic AI chat capability to a Spring Boot app — a customer support bot, a code assistant, or an internal Q&A tool.

---

## Scenario 103 — Structured Outputs (DTOs)

**Topics:** `BeanOutputParser`, `Type-safe AI responses`

### 🌍 Real-World Analogy
Imagine asking a contractor **"Give me a quote for the job."** A bad contractor hands you a napkin with scribbled text. A good contractor fills out a **standardised form** with fields for labour cost, materials, timeline, and total. Structured outputs are that form — you tell the AI exactly which fields to fill in, and it hands you back a clean Java object instead of a blob of text.

### 📖 What You'll Learn
- How to make the AI return structured JSON that maps directly to a Java DTO
- How `BeanOutputParser` automatically injects formatting instructions into the prompt
- How to handle AI responses in a type-safe way without manual string parsing
- Why unstructured text responses break downstream systems and how to avoid it

### 🔑 Key Concepts

| Concept | Description |
|---|---|
| `BeanOutputParser` | Tells the AI to respond in JSON matching your Java class structure |
| Type-safe responses | AI output is deserialized directly into a POJO — no regex, no manual parsing |
| Format instructions | Spring AI appends instructions to the prompt automatically to guide output format |

### 💡 When Would You Use This?
When your app needs to process AI output programmatically — e.g., extract product details from a description, generate a structured report, or classify user input into typed categories.

---

## Scenario 104 — Prompt Templates & Roles

**Topics:** `.st files`, `SystemMessage`, `Personas`

### 🌍 Real-World Analogy
Think of a **call centre that trains agents with a script**. Before the agent picks up the phone, they're briefed: *"You are a polite banking assistant. Never discuss competitor products. Always greet the customer by name."* That briefing is the **system message**. The customer's question is the **user message**. The `.st` template files are the scripts stored in a folder — reusable, version-controlled, and easy to update.

### 📖 What You'll Learn
- How to separate AI instructions from user input using `SystemMessage` and `UserMessage`
- How to use StringTemplate (`.st`) files to manage prompts as external, reusable resources
- How to inject dynamic values into prompt templates (e.g., `{name}`, `{topic}`)
- How to create different AI **personas** (a formal lawyer, a friendly tutor, a terse engineer)

### 🔑 Key Concepts

| Concept | Description |
|---|---|
| `SystemMessage` | Sets the AI's role, tone, and constraints — invisible to the end user |
| `UserMessage` | The actual input from the person using your app |
| `.st` template files | External text files with placeholders, loaded as Spring `Resource` objects |
| Personas | Different system prompts that radically change AI behaviour and tone |

### 💡 When Would You Use This?
When you need reusable, maintainable prompts across a team — marketing copy generator, legal document drafter, multi-persona chatbot, or a tutoring assistant with different teaching styles.

---

## Scenario 105 — Conversational Memory

**Topics:** `ChatMemory`, `Session-based history`

### 🌍 Real-World Analogy
Imagine talking to a **doctor who forgets everything between visits** — every appointment, you'd have to re-explain your entire medical history from scratch. That's a stateless AI. Now imagine a doctor with **a patient file** — they remember your allergies, your previous visits, and pick up exactly where you left off. `ChatMemory` is that patient file: it stores the conversation history and includes it in every subsequent prompt so the AI maintains context.

### 📖 What You'll Learn
- Why LLMs are stateless by default and how to work around it
- How `MessageWindowChatMemory` keeps a sliding window of the last N messages per session
- How to use `chatId` to maintain separate memory per user/session
- How to clear memory when a session ends

### 🔑 Key Concepts

| Concept | Description |
|---|---|
| `ChatMemory` | Interface for storing and retrieving conversation history |
| `InMemoryChatMemoryRepository` | Stores messages in RAM — fast, but lost on restart |
| `MessageWindowChatMemory` | Keeps only the last N messages to avoid exceeding token limits |
| `MessageChatMemoryAdvisor` | Automatically injects conversation history into every prompt |
| `chatId` | A unique key (e.g., `"user-123"`) that scopes memory to one conversation |

### 💡 When Would You Use This?
Any multi-turn chat application — customer support bots, interview assistants, AI tutors, or any scenario where the AI needs to remember what was said earlier in the conversation.

---

## Scenario 106 — RAG: Knowledge Retrieval

**Topics:** `Vector Stores`, `Embeddings`, `Document ETL`

### 🌍 Real-World Analogy
Imagine a **new employee at a company**. They're brilliant but know nothing about your internal policies. You could paste the entire 500-page employee handbook into every email you send them — but that's impractical. Instead, you give them access to a **searchable knowledge base**. When they have a question, they search for relevant pages, read just those sections, and answer accordingly. RAG (Retrieval-Augmented Generation) works exactly the same way: the AI searches your private documents, retrieves the most relevant chunks, and uses them to answer the question — without hallucinating facts it doesn't know.

### 📖 What You'll Learn
- What RAG is and why it's the most important pattern in enterprise AI
- How to load and chunk documents with `TextReader`
- How embeddings turn text into numbers so similarity search is possible
- How `SimpleVectorStore` stores and searches those embeddings
- How `QuestionAnswerAdvisor` automatically retrieves context and injects it into prompts

### 🔑 Key Concepts

| Concept | Description |
|---|---|
| Embeddings | Numerical representations of text that capture semantic meaning |
| `SimpleVectorStore` | In-memory vector database — great for development and learning |
| `TextReader` | Loads a text file from the classpath and splits it into `Document` objects |
| `QuestionAnswerAdvisor` | Intercepts the prompt, searches the vector store, and appends relevant context |
| Document ETL | The Extract → Transform → Load pipeline for getting your data into the vector store |

### 💡 When Would You Use This?
Whenever the AI needs to answer questions about **your private data** — internal policy documents, product manuals, legal contracts, customer records, or any knowledge base the model wasn't trained on.

---

## Scenario 107 — AI Function Calling

**Topics:** `Tools`, `@Description`, `java.util.Function`

### 🌍 Real-World Analogy
Imagine a **personal assistant who can do research but can't make phone calls themselves**. When you ask *"Book me a table at 7pm"*, they know they need to call the restaurant — so they hand the task off to you with exact instructions: *"Call this number, say this."* AI function calling works the same way. The model recognises when it needs **real-world data or actions** (current weather, a database lookup, an API call), then calls a Java function you've registered, and incorporates the result into its final answer.

### 📖 What You'll Learn
- How to register Java functions as tools the AI can invoke
- How `@Description` annotations help the AI understand when and why to use a tool
- How the AI decides which function to call based on the user's question
- How to implement real integrations: weather APIs, databases, calendar systems

### 🔑 Key Concepts

| Concept | Description |
|---|---|
| Function Calling / Tools | Registered Java functions the AI can invoke during a conversation |
| `@Description` | Documents what a function does so the AI can choose it intelligently |
| `java.util.Function<I, O>` | The standard Java interface used to define a tool |
| Tool resolution | Spring AI handles the back-and-forth: AI requests the tool → Spring calls it → result fed back to AI |

### 💡 When Would You Use This?
When the AI needs **live data or real actions** — checking stock prices, querying your database, sending emails, booking appointments, or integrating with any external API.

---

## Scenario 108 — AI Observability

**Topics:** `Token Tracking`, `Actuator Metrics`, `Redaction`

### 🌍 Real-World Analogy
Imagine running a **café without a cash register or inventory system**. You have no idea which drinks sell most, how much coffee you're using, or whether staff are giving away free drinks. AI Observability is your **management dashboard for AI** — it tells you how many tokens were consumed (your "coffee beans"), how long each request took, which prompts were called most, and ensures sensitive data like customer names never appear in your logs.

### 📖 What You'll Learn
- How to track **token usage** (prompt tokens, completion tokens, total cost indicators)
- How to expose AI metrics via **Spring Boot Actuator** (`/actuator/metrics`)
- How to implement **content redaction** so PII never leaks into logs or traces
- How to use Micrometer observations to monitor AI performance in production

### 🔑 Key Concepts

| Concept | Description |
|---|---|
| Token Tracking | Counting how many tokens each AI call consumes — critical for cost management |
| Spring Boot Actuator | Exposes `/actuator/metrics` with AI-specific counters and timers |
| Micrometer | The metrics library Spring AI integrates with for observations and tracing |
| Redaction | Automatically scrubs sensitive values (emails, IDs, names) from logs and traces |
| `GenAiObservationConvention` | Spring AI's hook for customising what gets observed and how |

### 💡 When Would You Use This?
In any **production AI application** — you need to know your costs, detect slow prompts, debug failures, and ensure compliance with data privacy regulations (GDPR, HIPAA) by keeping PII out of logs.

---

## 🧭 Learning Path Summary

```
102 → Talk to the AI                    (Hello World)
103 → Get structured data back          (Type Safety)
104 → Control the AI's personality      (Prompts & Roles)
105 → Give the AI a memory              (Conversations)
106 → Give the AI your private data     (RAG)
107 → Let the AI take real actions      (Function Calling)
108 → Watch the AI in production        (Observability)
```

Each scenario is a stepping stone. By the end, you'll have built every major pattern used in real-world enterprise AI applications with Spring Boot.

---

## ⚙️ Prerequisites

- Java 17+
- Spring Boot 3.4.5+
- Spring AI 1.1.4+
- A Google Gemini API key (`spring.ai.google.genai.api-key`)

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/springai/learning/
│   │   ├── controller/        # One controller per scenario
│   │   ├── config/            # One config class per scenario
│   │   └── dto/               # DTOs for structured output (Scenario 103)
│   └── resources/
│       ├── prompts/           # .st template files (Scenario 104)
│       └── knowledge/         # Text files for RAG (Scenario 106)
```

---

> 💬 **Pro Tip:** Every scenario has its own endpoint prefix (`/api/scenario102`, `/api/scenario103`, etc.) so all scenarios run simultaneously in one application. You can test them independently without interference.

Welcome to your dedicated workspace for mastering **Spring AI**. This project is designed as a "Clean State" foundation, allowing you to explore modern AI integrations without the noise of legacy configurations.

## 🛰️ Current Implementation: Scenario 102
### "The First Handshake"
Connecting a Spring Boot application to **Google Gemini 2.5 Flash** using the powerful `ChatClient` abstraction.

### 🛠️ Tech Stack
- **Framework**: Spring Boot 3.2.2
- **AI Engine**: [Spring AI](https://spring.io/projects/spring-ai) (v1.1.4 GA)
- **Model**: Google Gemini 1.5/2.5 Flash
- **Language**: Java 17

---

## 🚦 How to Get Started

### 1. Requirements
Ensure you have an API Key from [Google AI Studio](https://aistudio.google.com/).

### 2. Run the App
```bash
mvn spring-boot:run
```

### 3. Test the Endpoint
Open your browser or Postman and hit:
`GET http://localhost:8081/spring-ai/api/scenario102/ask?prompt=Hello`

---

## 📂 Project Structure
- `/src/main/java`: Minimalistic setup with `Scenario102Controller`.
- `/documentation`: Deep-dive explainers for every concept covered.

> [!TIP]
> This project runs on **Port 8081** to avoid conflicts with your original `debug-challenge` project.

