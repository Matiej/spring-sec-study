package com.matiej.springsecstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringSecStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecStudyApplication.class, args);
	}

}
