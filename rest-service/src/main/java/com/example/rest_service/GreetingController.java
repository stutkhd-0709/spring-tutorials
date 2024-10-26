package com.example.rest_service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

// RestController を使う場合は MVC場合と違いhtmlではなくjsonを返すようになっている
// view ではなく、ドメインオブジェクトを返すようになる
// @RestController = @Controller と @ResponseBody の省略形
@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        // Spring のメッセージコンバーターであるJackson2が自動でjsonに変換してくれる
        return new Greeting(counter.incrementAndGet(), String.format(template, name), new Date());
    }

}
