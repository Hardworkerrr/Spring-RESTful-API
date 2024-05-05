package com.example.app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.MOCK,
		classes = Application.class)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class ApplicationTests {

	@Autowired
	protected MockMvc mvc;

	@Test
	void contextLoads() {

	}

}
