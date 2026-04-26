package com.interview.debug.controller;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@RestController
@RequestMapping("/api/scenario41")
public class Scenario41Controller {

    private final DSLContext dsl;

    public Scenario41Controller(DSLContext dsl) {
        this.dsl = dsl;
    }

    /**
     * Demonstrates dynamic SQL building with jOOQ.
     * Note: In a production app, we would use generated classes like EMPLOYEES.NAME.
     * Here we use the Plain SQL API to avoid requiring the code-gen build step.
     */
    @GetMapping("/search")
    public List<Map<String, Object>> search(
            @RequestParam(required = false) String dept,
            @RequestParam(required = false) Double minSalary) {

        // 1. Start with an empty condition
        Condition condition = DSL.noCondition();

        // 2. Build conditions dynamically
        if (dept != null) {
            condition = condition.and(field("department").eq(dept));
        }

        if (minSalary != null) {
            condition = condition.and(field("salary").greaterOrEqual(minSalary));
        }

        // 3. Execute the query
        // Under the hood, this generates: SELECT * FROM scenario21_employees WHERE ...
        return dsl.select()
                .from(table("scenario21_employees"))
                .where(condition)
                .fetchMaps();
    }
}
