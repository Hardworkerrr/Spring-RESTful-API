package com.example.app.service;

import com.example.app.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<User> getUsers(String fromBirthDate, String toBirthDate, int pageNumber, int pageSize);

    User getUserById(Long id);

    User addUser(User user);

    User updateUser(User user, Long id);

    void deleteUser(Long id);

}
