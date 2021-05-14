package com.medicai.pillpal;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.medicai.pillpal");

        noClasses()
            .that()
            .resideInAnyPackage("com.medicai.pillpal.service..")
            .or()
            .resideInAnyPackage("com.medicai.pillpal.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.medicai.pillpal.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
