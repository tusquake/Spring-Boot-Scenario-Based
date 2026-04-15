package com.interview.debug.scenario131;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class ArchitectureTest {

    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("com.interview.debug");

    @Test
    public void controllersShouldNotDependOnRepositories() {
        ArchRule rule = noClasses()
                .that().haveSimpleNameEndingWith("Controller")
                .should().dependOnClassesThat().haveSimpleNameEndingWith("Repository")
                .because("Controllers should go through a Service layer for business logic and data access.");

        rule.check(importedClasses);
    }

    @Test
    public void servicesShouldBeInServicePackage() {
        ArchRule rule = classes()
                .that().haveSimpleNameEndingWith("ServiceImpl")
                .or().haveSimpleNameEndingWith("Service")
                .should().resideInAPackage("..service..")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }
}
