package com.interview.debug.controller;

import com.interview.debug.scope.PrototypeBean;
import com.interview.debug.scope.RequestScopedBean;
import com.interview.debug.scope.SessionScopedBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario82")
public class Scenario82Controller {

    private final RequestScopedBean requestBean;
    private final SessionScopedBean sessionBean;
    private final org.springframework.beans.factory.ObjectProvider<PrototypeBean> prototypeBeanProvider;

    public Scenario82Controller(RequestScopedBean requestBean, 
                                SessionScopedBean sessionBean, 
                                org.springframework.beans.factory.ObjectProvider<PrototypeBean> prototypeBeanProvider) {
        this.requestBean = requestBean;
        this.sessionBean = sessionBean;
        this.prototypeBeanProvider = prototypeBeanProvider;
    }

    @GetMapping("/scopes")
    public Map<String, Object> getScopeInfo() {
        sessionBean.increment();
        PrototypeBean prototypeBean = prototypeBeanProvider.getObject();

        Map<String, Object> response = new HashMap<>();
        response.put("request_bean_id", requestBean.getId());
        response.put("session_bean_id", sessionBean.getId());
        response.put("session_visit_count", sessionBean.getCounter());
        response.put("prototype_bean_id", prototypeBean.getId());
        
        response.put("explanation", "Refresh to see request_id change. Session_id stays same per browser.");
        return response;
    }
}
