package com.javorek.lab;

import org.springframework.boot.SpringApplication;

public class TestMultiDatasourceLabApplication {

	public static void main(String[] args) {
		SpringApplication.from(MultiDatasourceLabApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
