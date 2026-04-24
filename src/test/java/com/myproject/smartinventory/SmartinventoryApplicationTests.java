package com.myproject.smartinventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SmartinventoryApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testPasswordHash() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
		System.out.println("ADMIN HASH: " + encoder.encode("admin123"));
		System.out.println("MANAGER HASH: " + encoder.encode("manager123"));
	}

}
