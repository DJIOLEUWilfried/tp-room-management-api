package com.iuc.tpiuc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync  // Activons l’asynchrone
public class TpiucApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpiucApplication.class, args);
	}

}
