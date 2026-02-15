package com.gabriel.permissions;

import com.gabriel.permissions.application.service.PermissionServiceTest;
import com.gabriel.permissions.domain.model.AuthorityTest;
import com.gabriel.permissions.domain.model.RoleTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

/**
 * JUnit 5 suite that runs all Kotlin unit test classes under Bazel.
 */
@Suite
@SelectClasses({
    PermissionServiceTest.class,
    RoleTest.class,
    AuthorityTest.class,
})
public class AllUnitTests {
}
