package com.interview.debug.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_records")
public class IdempotencyRecord {

    @Id
    private String idempotencyKey;

    @Column(length = 2000)
    private String responseBody;

    private int responseStatus;

    private LocalDateTime createdAt;

    public IdempotencyRecord() {}

    public IdempotencyRecord(String idempotencyKey, String responseBody, int responseStatus) {
        this.idempotencyKey = idempotencyKey;
        this.responseBody = responseBody;
        this.responseStatus = responseStatus;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
    public int getResponseStatus() { return responseStatus; }
    public void setResponseStatus(int responseStatus) { this.responseStatus = responseStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
