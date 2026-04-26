package com.interview.debug.repository;

import com.interview.debug.dto.Scenario41EmployeeDTO;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Repository
public class Scenario41EmployeeDao {

    private final DSLContext dsl;

    public Scenario41EmployeeDao(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<Scenario41EmployeeDTO> findEmployeesByCriteria(String dept, Double minSalary) {
        Condition condition = DSL.noCondition();

        if (dept != null) {
            condition = condition.and(field("department").eq(dept));
        }

        if (minSalary != null) {
            condition = condition.and(field("salary").greaterOrEqual(minSalary));
        }

        return dsl.select(
                field("id"),
                field("name"),
                field("department"),
                field("salary")
        )
        .from(table("scenario21_employees"))
        .where(condition)
        .fetchInto(Scenario41EmployeeDTO.class);
    }
}
