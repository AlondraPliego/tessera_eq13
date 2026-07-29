package com.TESSERA.Eq13Tessera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Eq13TesseraApplication {

	public static void main(String[] args) {
		SpringApplication.run(Eq13TesseraApplication.class, args);
	}

}