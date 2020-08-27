package com.example.JabaVeans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JabaVeansApplication {

	public static void main(String[] args) {
		SpringApplication.run(JabaVeansApplication.class, args);
	}

}
