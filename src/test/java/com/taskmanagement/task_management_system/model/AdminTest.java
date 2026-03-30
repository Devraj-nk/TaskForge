package com.taskmanagement.task_management_system.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class AdminTest {

    @Test
    void manageUsersShouldAddAndRemoveUsers() {
        Admin admin = new Admin(1, "Admin", "admin@mail.com", "pw");
        TeamMember member = new TeamMember(2, "Alex", "alex@mail.com", "pw");

        admin.manageUsers("ADD", member);
        assertEquals(1, admin.getSystemUsers().size());
        assertTrue(admin.getSystemUsers().contains(member));

        admin.manageUsers("ADD", member);
        assertEquals(1, admin.getSystemUsers().size());

        admin.manageUsers("REMOVE", member);
        assertEquals(0, admin.getSystemUsers().size());
    }

    @Test
    void systemUsersListShouldBeUnmodifiable() {
        Admin admin = new Admin();
        assertThrows(UnsupportedOperationException.class,
                () -> admin.getSystemUsers().add(new TeamMember()));
    }

    @Test
    void manageRolesShouldHandleKnownUserTypes() {
        Admin admin = new Admin();
        assertDoesNotThrow(() -> admin.manageRoles(new ProjectManager()));
        assertDoesNotThrow(() -> admin.manageRoles(new TeamMember()));
        assertDoesNotThrow(() -> admin.manageRoles(new Admin()));
    }
}