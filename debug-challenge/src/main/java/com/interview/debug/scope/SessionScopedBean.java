package com.interview.debug.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.io.Serializable;
import java.util.UUID;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionScopedBean implements Serializable {
    private final String id = UUID.randomUUID().toString();
    private int counter = 0;

    public String getId() {
        return id;
    }

    public void increment() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}
