package com.example.app.controller;

import com.example.app.entity.LinkContainer;
import com.example.app.entity.Pagination;
import com.example.app.entity.User;
import com.example.app.service.UserService;
import com.example.app.util.LinkUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
@RequestMapping("users")
public class UserResourceController {
    private final UserService userService;

    @Autowired
    public UserResourceController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(defaultValue = "0") int pageNumber,
                                      @RequestParam(defaultValue = "5") int pageSize,
                                      @RequestParam(defaultValue = "${search.min-accepted-date}") String fromBirthDate,
                                      @RequestParam(defaultValue = "${search.max-accepted-date}") String toBirthDate) {
        Page<User> page = this.userService.getUsers(
                fromBirthDate,
                toBirthDate,
                pageNumber,
                pageSize
        );
        Map<String, Object> responseBody = new LinkedHashMap<>();
        Map<String, String> links = new LinkContainer().getLinks();
        Pagination pagination = new Pagination(page.getTotalElements(), pageNumber, pageSize);
        Map<String, Object> params = Map.of(
                "pageNumber", pageNumber,
                "pageSize", pageSize,
                "fromBirthDate", fromBirthDate,
                "toBirthDate", toBirthDate);
        links.put("self", LinkUtils.createLink("self", params));
        if (page.hasNext()) {
            links.put("next", LinkUtils.createLink("next", params));
        }
        if (page.hasPrevious()) {
            links.put("prev", LinkUtils.createLink("prev", params));
        }
        responseBody.put("data", page.getContent());
        responseBody.put("pagination", pagination);
        responseBody.put("links", links);
        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        User user = this.userService.getUserById(id);
        return ResponseEntity.ok().body(Map.of("data", user));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> addUser(@Valid @RequestBody User user) {
        User savedUser = this.userService.addUser(user);
        LinkContainer linkContainer = new LinkContainer();
        Map<String, String> links = linkContainer.getLinks();
        Map<String, Object> responseBody = new LinkedHashMap<>();
        URI recourseLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        links.put("self", recourseLocation.toString());
        responseBody.put("data", savedUser);
        responseBody.put("links", linkContainer.getLinks());
        return ResponseEntity.created(recourseLocation).body(responseBody);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User newUser) {
        User updatedUser = this.userService.updateUser(newUser, id);
        return ResponseEntity.ok().body(Map.of("data", updatedUser));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}