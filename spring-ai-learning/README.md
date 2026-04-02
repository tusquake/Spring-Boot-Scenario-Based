# 🚀 Spring AI Learning Journey

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
