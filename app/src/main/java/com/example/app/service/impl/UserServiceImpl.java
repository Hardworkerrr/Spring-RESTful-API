package com.example.app.service.impl;

import com.example.app.entity.User;
import com.example.app.exception.NoSuchUserExistsException;
import com.example.app.exception.RequestParamNotValidException;
import com.example.app.exception.UnderageUserException;
import com.example.app.repository.BaseUserRepository;
import com.example.app.service.UserService;
import com.example.app.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {
    private final BaseUserRepository baseUserRepository;
    private final int USER_REGISTRATION_MIN_AGE;


    @Autowired
    public UserServiceImpl(BaseUserRepository baseUserRepository,
                           @Value("${user.registration.min-age}") int userRegistrationMinAge) {
        this.baseUserRepository = baseUserRepository;
        this.USER_REGISTRATION_MIN_AGE = userRegistrationMinAge;
    }

    @Override
    public Page<User> getUsers(String fromBirthDate, String toBirthDate, int pageNumber, int pageSize) {
        LocalDate fromDate = LocalDate.parse(fromBirthDate);
        LocalDate toDate = LocalDate.parse(toBirthDate);
        if (!toDate.isAfter(fromDate)) {
            throw new RequestParamNotValidException();
        }
        return this.baseUserRepository.getUsersByBirthDateBetween(
                fromDate,
                toDate,
                PageRequest.of(pageNumber,pageSize)
        );
    }

    @Override
    public User getUserById(Long id) {
        return this.baseUserRepository.getUserById(id).orElseThrow(NoSuchUserExistsException::new);
    }

    @Override
    public User addUser(User user) {
        LocalDate dateToCompare = user.getBirthdate().plusYears(USER_REGISTRATION_MIN_AGE);
        if (!LocalDate.now().isAfter(dateToCompare)) {
            throw new UnderageUserException();
        }
        return this.baseUserRepository.save(user);
    }

    @Override
    public User updateUser(User user, Long id) {
        User oldUser = this.baseUserRepository.findById(id).orElseThrow(NoSuchUserExistsException::new);
        EntityUtils.updateNonNullFields(user, oldUser);
        return this.baseUserRepository.save(oldUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = this.baseUserRepository.findById(id).orElseThrow(NoSuchUserExistsException::new);
        this.baseUserRepository.delete(user);
    }
}
