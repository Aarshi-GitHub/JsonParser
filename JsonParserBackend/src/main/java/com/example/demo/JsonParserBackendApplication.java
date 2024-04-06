package com.example.demo;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SpringBootApplication
public class JsonParserBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(JsonParserBackendApplication.class, args);
	}
	

}
