# SSRF (Server-Side Request Forgery) — Complete Interview Reference

## Table of Contents
1. [What is SSRF?](#1-what-is-ssrf)
2. [How the Attack Works](#2-how-it-works)
3. [Internal vs External SSRF](#3-internal-vs-external)
4. [The Impact: Cloud Metadata Exploitation](#4-cloud-metadata)
5. [The Classic Interview Trap: 127.0.0.1 Bypasses](#5-the-classic-interview-trap-bypasses)
6. [Whitelisting vs Blacklisting URLs](#6-whitelisting)
7. [Validating Hostnames and IP Addresses](#7-validating-hostnames)
8. [SSRF in Microservices (Sidecar protection)](#8-microservices)
9. [Restricting Network Access (Egress Filtering)](#9-egress-filtering)
10. [Safe Alternatives to Fetching Remote URLs](#10-alternatives)
11. [Common Mistakes](#11-common-mistakes)
12. [Quick-Fire Interview Q&A](#12-quick-fire-interview-qa)

---

## 1. What is SSRF?
SSRF is a vulnerability where an attacker can trick a server-side application into making requests to an unintended location. This could be an internal service, a loopback address, or an external third-party site.

---

## 2. How SSRF Works
1. An application provides a feature to "Fetch a URL" (e.g., to generate a link preview or import a profile picture).
2. The user provides the URL: `http://localhost:8080/admin/deleteUser?id=1`.
3. The server, which has access to its own localhost, fetches the URL and executes the command.

---

## 3. Internal vs External SSRF
- **Internal**: Targeting services on the same network that are not exposed to the internet (e.g., Redis, internal databases, Jenkins).
- **External**: Using the server as a proxy to launch attacks on other websites, hiding the attacker's true IP.

---

## 4. Cloud Metadata Exploitation
This is the most dangerous form of SSRF. In AWS, GCP, or Azure, a special metadata service is available at `http://169.254.169.254`. An attacker can use SSRF to fetch this URL and steal **Secret Keys**, **IAM Roles**, and other sensitive credentials.

---

## 5. The Classic Interview Trap: IP Bypasses
**The Trap**: You block the string `127.0.0.1` and `localhost`.
**The Problem**: Attackers can use decimal notation (`2130706433`), hex notation (`0x7f000001`), or create a custom domain (e.g., `spoof.com`) that resolves to `127.0.0.1`.
**The Fix**: Resolve the hostname to an IP address in code and verify that the IP is not in a private or reserved range (like `10.x.x.x` or `192.168.x.x`).

---

## 6. Whitelisting vs Blacklisting
- **Blacklisting**: Trying to block "bad" URLs. (Always fails due to bypasses).
- **Whitelisting**: Only allowing specific, trusted domains (e.g., `https://trusted-images.com`). This is the **only secure way** to handle URL fetching.

---

## 7. Validating Hostnames
When validating a URL, you must check the **Protocol** (only allow `http` and `https`, block `file://`, `gopher://`, `ftp://`), the **Port** (only allow 80 and 443), and the **Host**.

---

## 8. SSRF in Microservices
Service meshes like **Istio** or **Linkerd** can provide egress policies that prevent a microservice from talking to any IP address or domain that hasn't been explicitly authorized.

---

## 9. Egress Filtering
A network-level defense. The server's firewall should be configured to block all outgoing traffic except to known, necessary destinations.

---

## 10. Safe Alternatives
If you need to fetch images, consider using a dedicated "Image Proxy" service that is isolated from your main application and has no access to internal networks.

---

## 11. Common Mistakes
1. Trusting user-provided URLs without validation.
2. Only blocking `localhost` and `127.0.0.1`.
3. Not disabling support for "exotic" protocols like `file:///` in your HTTP client library.

---

## 12. Quick-Fire Interview Q&A
**Q: What is the metadata IP address for AWS?**  
A: `169.254.169.254`.  
**Q: How do you prevent DNS Rebinding in SSRF?**  
A: By resolving the hostname once, validating the IP, and then using that specific IP address for the actual request (preventing the attacker from changing the IP after validation).
