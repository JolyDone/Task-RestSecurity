package ru.itmentor.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SpringBootSecurityDemoApplication.class);
		app.setDefaultProperties(Collections
				.singletonMap("server.port", "8088"));
		app.run(args);
	}

}
