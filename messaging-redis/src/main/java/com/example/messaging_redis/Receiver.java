package com.example.messaging_redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

// Recieverをメッセージリスナーとして登録すると、メッセージ処理メソッドに任意の名前をつけられるらしい
public class Receiver {
  private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

  private AtomicInteger counter = new AtomicInteger();

  public void receiveMessage(String message) {
    LOGGER.info("Received message: {}", message);
    counter.incrementAndGet();
  }

  public int getCount() {
    return counter.get();
  }
}
