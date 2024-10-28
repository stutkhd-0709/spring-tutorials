package com.example.messaging_redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/*
* Spring Data Redis を使った上で我々が定義する必要があるもの
* - connection factory -> template と message listener containerを動かして、Redisサーバーに接続させる
* - message listener container -> Reciver を登録することでメッセージを受信可能になる
* - redis template -> メッセージを送信できる
* */

@SpringBootApplication
public class MessagingRedisApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessagingRedisApplication.class);

	// Redisサーバーと接続して、メッセージリスナーを設定するためのコンテナ
	// Subscribe するための設定
	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		// chat というtopicに対してメッセージをリッスンする
		container.addMessageListener(listenerAdapter, new PatternTopic("chat"));

		return container;
	}

	// メッセージを受信するためのアダプター
	@Bean
	MessageListenerAdapter listenerAdapter(Receiver receiver) {
		// ReceiverクラスのreceiveMessageメソッドを実行する
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}

	// 実際にメッセージを処理するクラス
	// Subscriber
	@Bean
	Receiver receiver() {
		return new Receiver();
	}

	// Redis にメッセージを送信するためのテンプレート
	// Publisherが使うやつt
	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}

	public static void main(String[] args) throws InterruptedException {

		ApplicationContext ctx = SpringApplication.run(MessagingRedisApplication.class, args);

		StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
		Receiver receiver = ctx.getBean(Receiver.class);

		while (receiver.getCount() == 0) {
			LOGGER.info("Sending message...");
			// Publisher
			template.convertAndSend("chat", "Hello World!");
			Thread.sleep(500L);
		}

		System.exit(0);
	}

}
