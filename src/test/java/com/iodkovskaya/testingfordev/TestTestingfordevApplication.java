package com.iodkovskaya.testingfordev;

import org.springframework.boot.SpringApplication;

public class TestTestingfordevApplication {

	public static void main(String[] args) {
		SpringApplication.from(TestingfordevApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
