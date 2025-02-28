package com.stream.app.spring_stream_backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.stream.app.spring_stream_backend.Services.Videoservice;

@SpringBootTest
class SpringStreamBackendApplicationTests {

	@Autowired
	Videoservice videoservice;
	
	@Test
	void contextLoads() {
		videoservice.processVideo("f2e44fc5-8fd6-45f6-b70b-2c31a6dae34e");
	}

}
