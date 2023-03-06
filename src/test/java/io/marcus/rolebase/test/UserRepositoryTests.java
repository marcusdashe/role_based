package io.marcus.rolebase.test;

import io.marcus.rolebase.user.User;
import io.marcus.rolebase.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static jdk.dynalink.linker.support.Guards.isNotNull;
import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repo;

    @Test
    public void testCreateUser(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("bullshit1");
        User newUser = new User("mqrksimon@gmail.com", password);
        User savedUser = repo.save(newUser);

//        assertThat(((List) savedUser)).isNotNull();
//        assertThat(savedUser.getId()).isGreaterThan(0);
    }
}
