package com.example.relational_data_access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class RelationalDataAccessApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(RelationalDataAccessApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RelationalDataAccessApplication.class, args);
	}

	// spring-boot は自動でJbdcTemplateを作成する
	// 作成されたJdbcTemplate は@AutowiredによりDIされる
	@Autowired
	JdbcTemplate jdbcTemplate;


	// CommandLineRunner を実装してることにより、アプリケーションコンテキストが実行された後 run() が呼ばれる
	@Override
	public void run(String... args) throws Exception {

		log.info("Creating Tables");

		jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE customers(" +
				"id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

		// 苗字と名前に分割する
		List<Object[]> splitUpNames = Stream.of("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long")
				.map(name -> name.split(" "))
				.collect(Collectors.toList());

		splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?, ?)", splitUpNames);

		log.info("Querying for customer records where first_name = 'Josh':");
		jdbcTemplate.query("SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
				// RowMapperによりモデルにマッピングされる
				(rs, rowNum) -> new Customer(
						rs.getLong("id"),
						String.format("FIRSTNAME: %s", rs.getString("first_name")),
						rs.getString("last_name")
				), "Josh"
		).forEach(customer -> log.info(customer.toString()));


	}
}
