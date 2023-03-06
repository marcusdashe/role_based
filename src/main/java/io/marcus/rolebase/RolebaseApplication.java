package io.marcus.rolebase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class RolebaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(RolebaseApplication.class, args);
	}

}
