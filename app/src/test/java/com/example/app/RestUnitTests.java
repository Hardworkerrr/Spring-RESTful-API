package com.example.app;

import com.example.app.entity.User;
import com.example.app.exception.NoSuchUserExistsException;
import com.example.app.repository.BaseUserRepository;
import com.example.app.service.UserService;
import com.example.app.util.EntityUtils;
import com.example.app.util.LinkUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("unit-test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class RestUnitTests extends ApplicationTests {

    @Autowired
    UserService userService;
    @Autowired
    BaseUserRepository userRepository;

    @Test
    void addNewUser_after_getThisUserById() {
        User savedUser = addTestUserInDb();
        User savedUserFromDb = userService.getUserById(savedUser.getId());
        assertThat(savedUser).isEqualTo(savedUserFromDb);
    }

    @Test
    void updateUserFields_after_getThisUserById() {
        User oldUser = addTestUserInDb();
        oldUser.setFirstName("Nicola");
        oldUser.setLastName("Kaspersky");
        userService.updateUser(oldUser, oldUser.getId());
        User updatedUser = userService.getUserById(oldUser.getId());
        assertThat(updatedUser.getId()).isEqualTo(oldUser.getId());
        assertThat(updatedUser.getFirstName()).isEqualTo(oldUser.getFirstName());
        assertThat(updatedUser.getLastName()).isEqualTo(oldUser.getLastName());
    }

    @Test
    void addUser_after_deleteUser_expectException() {
        User savedUser = addTestUserInDb();
        userService.deleteUser(savedUser.getId());
        assertThatThrownBy(() -> userService.getUserById(savedUser.getId()))
                .isInstanceOf(NoSuchUserExistsException.class);
    }

    @Test
    void updateNonNullUserFields_util_check() {
        User newUser = new User(
                null,
                null,
                "new_email@gmail.com",
                LocalDate.parse("2005-11-05"),
                "new address v2",
                "380930553211"
        );
        User savedUser = addTestUserInDb();
        EntityUtils.updateNonNullFields(savedUser, newUser);
        assertThat(savedUser.getFirstName()).isNotEqualTo(null).isEqualTo("Bob");
        assertThat(savedUser.getLastName()).isNotEqualTo(null).isEqualTo("Robson");
        assertThat(savedUser.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(savedUser.getAddress()).isEqualTo(newUser.getAddress());
        assertThat(savedUser.getPhoneNumber()).isEqualTo(newUser.getPhoneNumber());
        assertThat(savedUser.getBirthdate()).isEqualTo(newUser.getBirthdate());
    }

    @Test
    void createLink_util_check() {
        String selfPaginationLinkNoParams = "http://localhost";
        String selfPaginationLinkWithParams = "http://localhost?pageSize=10&pageNumber=5";
        String nextPaginationLink = "http://localhost?pageSize=10&pageNumber=6";
        String prevPaginationLink = "http://localhost?pageSize=10&pageNumber=4";
        Map<String, Object> params = Map.of(
                "pageNumber", 5,
                "pageSize", 10
        );
        assertThat(selfPaginationLinkNoParams).isEqualTo(LinkUtils.createLink("self", Collections.emptyMap()));
        assertThat(selfPaginationLinkWithParams).isEqualTo(LinkUtils.createLink("self", params));
        assertThat(nextPaginationLink).isEqualTo(LinkUtils.createLink("next", params));
        assertThat(prevPaginationLink).isEqualTo(LinkUtils.createLink("prev", params));
    }

    User addTestUserInDb() {
        User testUser = new User(
                "Bob",
                "Robson",
                "bob_robson2@gmail.com",
                LocalDate.parse("1993-01-01"),
                "Test address",
                "380930553212"
        );
        return userService.addUser(testUser);
    }
}
