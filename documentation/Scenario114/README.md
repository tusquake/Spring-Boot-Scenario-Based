# Scenario 114: Remember Me Authentication Masterclass 🔐

## Overview
How do you let users stay logged in even after they close their browser, without compromising security?

**Spring Security Remember Me** services provide a way to automatically re-authenticate a user based on a cookie sent from their browser.

---

## 🚀 Key Patterns

### 1. Simple Hash-Based (The "Lazy" Way) 🍪
A cookie is sent containing a hash of the username, expiration, and password.
- **Risk**: If the user changes their password, all cookies become invalid. If the cookie is stolen, the attacker can use it until it expires.

### 2. Persistent Token Approach (The "Secure" Way) 🛡️
We use the **`persistent_logins`** table.
- **How it works**:
    1.  User logs in with "Remember Me" checked.
    2.  Spring generates a **Series** (constant for this browser) and a **Token** (random, refreshes every time).
    3.  Cookie contains `series` and `token`.
- **The "Magic": Token Stealing Detection** 🚨
    -   If an attacker steals your cookie and uses it, Spring verifies the token and generates a **new** one for them.
    -   When **you** next try to auto-login with your **old** token, Spring detects the mismatch! 
    -   **Action**: Spring immediately invalidates **all** sessions for that user, assuming a theft occurred. This is a top-tier senior interview topic.

---

## 🏗️ Technical Details
- **Database**: `persistent_logins` table (Series/Token/LastUsed).
- **Service**: `JdbcTokenRepository` using the standard Spring schema.
- **Context API**: `authentication instanceof RememberMeAuthenticationToken` to detect the login type.

---

## 🧪 How to Test

### 1. Open the Login Form
Navigate to: `http://localhost:8080/debug-application/api/scenario114/show-login`

### 2. Login with Remember Me
- **User**: `user114`
- **Pass**: `password114`
- **Action**: Check the **"Remember Me"** box and click Login.

### 3. Verify the Cookie
Check your browser cookies for a cookie named `remember-me`.

### 4. Test Persistence
1. Close your browser or clear the **`JSESSIONID`** cookie (but keep the `remember-me` cookie!).
2. Navigate directly to: `http://localhost:8080/debug-application/api/scenario114/protected`
3. **Result**: You should be logged in automatically!

---

## Interview Tip 💡

**Q**: *"Why is the Persistent Token approach better than simple hashing?"*  
**A**: *"It provides better security through **Token Refreshing**. Every time a remember-me cookie is used, a new token is generated. If an old token is reused, Spring detects it as a potential theft and invalidates all tokens for that user. It also avoids leaking the user's password hash in the cookie."*

**Q**: *"How do you prevent a Remember-me user from performing sensitive operations (like changing a password)?"*  
**A**: *"You can use the `@PostAuthorize` or a security check to ensure the user is NOT a `RememberMeAuthenticationToken`. Many apps require a 'Full Re-authentication' for sensitive pages even if the user is auto-logged in."*
