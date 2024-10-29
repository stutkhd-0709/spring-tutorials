package com.example.messaging_rabbitmq;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class Receiver {

  private CountDownLatch latch = new CountDownLatch(1);

  public void receiveMessage(String message) {
    System.out.println("Received message: " + message);
    // new CountDownLatch(1) の値を1つ減らす
    latch.countDown();
  }

  public CountDownLatch getLatch() {
    return latch;
  }
}
