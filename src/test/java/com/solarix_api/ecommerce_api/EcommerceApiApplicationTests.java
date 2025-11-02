package com.solarix_api.ecommerce_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {"spring.main.web-application-type=none"})
@ActiveProfiles("test")
class EcommerceApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
