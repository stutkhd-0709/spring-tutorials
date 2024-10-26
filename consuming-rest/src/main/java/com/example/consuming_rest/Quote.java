package com.example.consuming_rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// JsonIgnoreProperties は Quoteに存在しないフィールドでjsonのデシリアライズが発生したら、そのフィールドを除外してデシリアライズする
// 今回だと type, value 以外のjson fieldがあったらそれは除外される
@JsonIgnoreProperties(ignoreUnknown = true)
public record Quote(String type, Value value) { }