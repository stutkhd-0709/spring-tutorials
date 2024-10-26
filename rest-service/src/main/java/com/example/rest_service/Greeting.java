package com.example.rest_service;

import java.util.Date;

// idなどのデータを表現するためのレコードクラス
public record Greeting(long id, String content, Date date) {

}