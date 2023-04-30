package com.lldj.gram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GramApplication {

	public static void main(String[] args) {
		SpringApplication.run(GramApplication.class, args);
	}

}
