package com.example.app;

import com.example.app.entity.LinkContainer;
import com.example.app.entity.Pagination;
import com.example.app.entity.User;
import com.example.app.enums.RestApiError;
import com.example.app.repository.BaseUserRepository;
import com.example.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("integration-test")
@TestPropertySource(locations = "classpath:application.properties")
public class RestIntegrationTests extends ApplicationTests {
    @Autowired
    UserService userService;
    @Autowired
    BaseUserRepository userRepository;
    int defaultPageNumber = 0;
    int defaultPageSize = 5;

    @Value("${search.min-accepted-date}")
    String defaultMinDate;

    @Value("${search.max-accepted-date}")
    String defaultMaxDate;

    @Test
    void getAllUsers() throws Exception {
//        Page<User> page = userService.getUsers(defaultMinDate, defaultMaxDate, defaultPageNumber, defaultPageSize);
//        ResponseEntity<Map<String, List<User>>> responseEntity = ResponseEntity.ok().body(Map.of("body",page.getContent()));
//        System.out.println(ResponseEntity.ok().body(Map.of("body",page.getContent()).get("body")).getBody());
//        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(result -> assertThat(new JSONObject(result
//                        .getResponse()
//                        .getContentAsString())
//                        .get("data")
//                )
//                        .isEqualTo(jsonOutput));
    }

    @Test
    void getAllUsersByInvalidBirthDate_expectException() throws Exception {
        mvc.perform(get("/users?fromBirthDate=2005-01-05&toBirthDate=2000-01-05").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.errorMsg").value(RestApiError.REQUEST_PARAM_VALIDATION_FAILED.getErrorMsgText().value()))
                .andExpect(jsonPath("$.errors.errorCode").value(RestApiError.REQUEST_PARAM_VALIDATION_FAILED.getErrorCode().value()));
    }

    @Test
    void getUserByIdThatDoesntExist_expectException() throws Exception {
        mvc.perform(get("/users/200000").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors.errorMsg").value(RestApiError.USER_NOT_FOUND.getErrorMsgText().value()))
                .andExpect(jsonPath("$.errors.errorCode").value(RestApiError.USER_NOT_FOUND.getErrorCode().value()));
    }

    @Test
    void addUserWithNullRequiredParameters_expectException() throws Exception {
        User testUser = new User(
                null,
                "Johnson",
                "johnson@example.com",
                null,
                "789 Center Rd",
                "9876543210"
        );
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(testUser);
        String resultString = "birthDate: must not be null, and firstName: must not be blank";
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.errorMsg").value(resultString))
                .andExpect(jsonPath("$.errors.errorCode").value(RestApiError.USER_DATA_VALIDATION_FAILED.getErrorCode().value()));
    }

    @Test
    void addUserWithNotValidParameters_expectException() throws Exception {
        User testUser = new User(
                "John",
                "Johnson",
                "johnsonexamplecom",
                LocalDate.parse("1993-01-01"),
                "789 Center Rd",
                "9876543210"
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonString = mapper.writeValueAsString(testUser);
        String resultString = "email: Unknown email format";
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.errors.errorMsg").value(resultString))
                .andExpect(jsonPath("$.errors.errorCode").value(RestApiError.USER_DATA_VALIDATION_FAILED.getErrorCode().value()));
    }

    @Test
    void addUnderAgeUser_expectException() throws Exception {
        User testUser = new User(
                "John",
                "Johnson",
                "johnson@example.com",
                LocalDate.parse("2009-01-01"),
                "789 Center Rd",
                "9876543210"
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonString = mapper.writeValueAsString(testUser);
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errors.errorMsg").value(RestApiError.USER_UNDERAGE.getErrorMsgText().value()))
                .andExpect(jsonPath("$.errors.errorCode").value(RestApiError.USER_UNDERAGE.getErrorCode().value()));
    }

    @Test
    void addUserViaController() throws Exception {
        User testUser = new User(
                "John",
                "Johnson",
                "johnson@example.com",
                LocalDate.parse("2003-01-01"),
                "789 Center Rd",
                "9876543210"
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonString = mapper.writeValueAsString(testUser);
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(testUser.getLastName()))
                .andExpect(jsonPath("$.data.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.data.birthDate").value(testUser.getBirthdate().toString()));
    }

    @Test
    void getUserByIdViaController() throws Exception {
        User testUser = addTestUserInDb();
        mvc.perform(get("/users/{id}", testUser.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(testUser.getLastName()))
                .andExpect(jsonPath("$.data.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.data.birthDate").value(testUser.getBirthdate().toString()));
    }

    @Test
    void deleteUserViaController() throws Exception {
        User testUser = addTestUserInDb();
        mvc.perform(delete("/users/{id}", testUser.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateUserViaController() throws Exception {
        User testUser = addTestUserInDb();
        testUser.setFirstName("Nicola");
        testUser.setLastName("Kaspersky");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonString = mapper.writeValueAsString(testUser);
        mvc.perform(patch("/users/{id}", testUser.getId()).contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName").value(testUser.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(testUser.getLastName()));
    }

    @Test
    void checkLinkCreation() throws Exception {
        String defSelfLink = "http://localhost/users?toBirthDate=2024-05-05&pageSize=5&fromBirthDate=1900-01-01&pageNumber=0";
        String defNextLink = "http://localhost/users?toBirthDate=2024-05-05&pageSize=5&fromBirthDate=1900-01-01&pageNumber=1";
        Map<String, String> links = new LinkContainer().getLinks();
        links.put("self",defSelfLink);
        links.put("next",defNextLink);
        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links.self").value(links.get("self")))
                .andExpect(jsonPath("$.links.next").value(links.get("next")));
    }

    @Test
    void checkPaginationCreation() throws Exception {
        Page<User> page = userService.getUsers(defaultMinDate,defaultMaxDate,defaultPageNumber,defaultPageSize);
        Pagination pagination = new Pagination(page.getTotalElements(),defaultPageNumber,defaultPageSize);
        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pagination.totalElements").value(pagination.getTotalElements()))
                .andExpect(jsonPath("$.pagination.pageNumber").value(pagination.getPageNumber()))
                .andExpect(jsonPath("$.pagination.pageSize").value(pagination.getPageSize()));
    }


    User addTestUserInDb() {
        User testUser = new User(
                "John",
                "Johnson",
                "johnson@example.com",
                LocalDate.parse("2003-01-01"),
                "789 Center Rd",
                "9876543210"
        );
        return userService.addUser(testUser);
    }
}
