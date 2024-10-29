package com.example.validating_form_input;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PersonForm {

  @NotNull
  @Size(min = 2, max = 50)
  private String name;

  @NotNull
  @Min(18)
  private int age;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return this.age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String toString() {
    return "Person(Name: " + this.name + ", Age: " + this.age + ")";
  }
}
