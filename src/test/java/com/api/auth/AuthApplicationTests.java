package com.api.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(SpringExtension.class)
//@SpringBootTest
@ActiveProfiles("test")
class AuthApplicationTests {

	@MockBean
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

}
