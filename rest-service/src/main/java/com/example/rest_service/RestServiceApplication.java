package com.example.rest_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Conditional;

/*
* @SpringBootApplicationは以下の全てを追加する
* @Configuration:
* @EnableAutoConfiguration -> AutoConfigurationを有効化する
* @ComponentScan
* */
@SpringBootApplication
public class RestServiceApplication {

	public static void main(String[] args) {
		/*
		* 第一引数にはDIコンテナに生成する際に使用するコンフィギュレーションクラスを渡す(@Configuration)
		* SpringBootApplication は @Configurationクラスが内部で付与されてる
		* @SpringBootApplication -> @SpringBootConfiguration -> @Configuration
		* */
		SpringApplication.run(RestServiceApplication.class, args);
	}

}
