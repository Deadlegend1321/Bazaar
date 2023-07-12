package com.mudit.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.mudit.common.entity", "com.mudit.admin.user"})
public class BazaarBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BazaarBackendApplication.class, args);
	}

}
