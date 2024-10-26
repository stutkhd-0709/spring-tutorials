package com.example.consuming_rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConsumingRestApplication {

	// getLogger の引数にはロガーを使用するクラスのClassオブジェクトを渡す
	// これによりログメッセージ上にどこのログなのか表現されるようになる
	private static final Logger log = LoggerFactory.getLogger(ConsumingRestApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ConsumingRestApplication.class, args);
	}

	// 一応 WebClient とも同じような機能だけど若干違うらしい
	// DI用の RestTemplate のコンストラクタを作成してる
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	// Springアプリを起動してすべてのBeanがDIコンテナに格納された後、DIコンテナにCommandLineRunnerのインスタンスがあれば実行される。
	@Bean
	@Profile("!test")
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			Quote quote = restTemplate.getForObject("http://localhost:8080/api/random", Quote.class);
			log.info(quote.toString());
		};
	}
}
