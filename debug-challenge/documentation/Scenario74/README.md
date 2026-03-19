# Scenario 74: Server-Side Request Forgery (SSRF) Protection

## Overview
**Server-Side Request Forgery (SSRF)** is a vulnerability where an attacker can influence the server to make HTTP requests to an arbitrary domain. 

This is particularly dangerous when the server has access to internal resources that are not reachable from the outside world, such as:
- Internal admin panels.
- Databases or internal APIs.
- Cloud metadata services (e.g., `169.254.169.254`).

## The Vulnerability
If an application takes a URL from a user and fetches it without validation, the attacker can provide a local IP or a internal hostname.

```java
// VULNERABLE CODE
String data = restTemplate.getForObject(new URI(userProvidedUrl), String.class);
```

## The Defense: Strict Validation & Allowlisting
The best defense is to **allowlist** specific domains. If that's not possible, you must validate the resolved IP address.

In this scenario, we use `SsrfValidator` which:
1.  **Validates Scheme**: Only `http` and `https` are allowed (no `file://`, `gopher://`, or `ftp://`).
2.  **Resolves Hostname**: Converts the domain to an IP address to prevent "DNS Rebinding" and bypasses.
3.  **Blocks Internal IPs**: Uses `address.isLoopbackAddress()`, `isSiteLocalAddress()`, etc., to block `127.0.0.1`, `192.168.x.x`, and others.

## Interview Tips 💡
- **What is the difference between Blind and Non-Blind SSRF?** In Non-Blind SSRF, the attacker can see the response from the server. In Blind SSRF, they can only trigger the request and must infer the result (e.g., via timing or out-of-band monitoring).
- **Why is blocking `localhost` not enough?** Because an attacker can use equivalent IPs like `127.1`, `0.0.0.0`, or even a custom domain that resolves to `127.0.0.1`.
- **What is the significance of `169.254.169.254`?** This is the magic IP for cloud metadata services (AWS, Azure, GCP). It often contains sensitive tokens and IAM role credentials.

## Testing the Scenario
1. **Safe Fetch**: `GET /api/scenario74/secure/fetch?url=https://www.google.com`. Should succeed.
2. **Internal Blocked**: `GET /api/scenario74/secure/fetch?url=http://localhost:8080/actuator/health`. Should be blocked.
3. **Internal Vulnerable**: `GET /api/scenario74/vulnerable/fetch?url=http://localhost:8080/actuator/health`. Should succeed (demonstrating the bypass).
