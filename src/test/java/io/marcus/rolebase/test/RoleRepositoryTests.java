package io.marcus.rolebase.test;


import io.marcus.rolebase.user.Role;
import io.marcus.rolebase.user.RoleRepository;
import io.marcus.rolebase.user.User;
import io.marcus.rolebase.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
    @Autowired
    RoleRepository repo;

    @Autowired
    UserRepository userRepo;

    @Test
    public void testCreatedRole() {
        Role admin = new Role("ROLE_ADMIN");
        Role editor = new Role("ROLE_EDITOR");
        Role customer = new Role("ROLE_CUSTOMER");

        repo.saveAll(List.of(admin, editor, customer));

        long count = repo.count();
        assertEquals(3, count);
    }

    @Test
    public void testAssignRoleToUser(){
        Integer userId = 4;
        Integer roleId = 3;

        User user = userRepo.findById(userId).get();
        user.addRole(new Role(roleId));

        User updatedUser = userRepo.save(user);
//        assertThat(updatedUser.getRoles()).hasSize(1);
    }

    @Test
    public void testAssignRoleToUser1() {
        Integer userId = 2;
        User user = userRepo.findById(userId).get();
        user.addRole(new Role(1));
        user.addRole(new Role(2));


        User updatedUser = userRepo.save(user);
//        assertThat(updatedUser.getRoles()).hasSize(2);

    }
}
