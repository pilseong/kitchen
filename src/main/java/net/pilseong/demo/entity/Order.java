package net.pilseong.demo.entity;

import java.util.UUID;

public class Order {
  private UUID id;
  private String name;
  private Long prepTime;

  public Order() {
  }

  public Order(UUID id, String name, Long prepTime) {
    this.id = id;
    this.name = name;
    this.prepTime = prepTime;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getPrepTime() {
    return prepTime;
  }

  public void setPrepTime(Long prepTime) {
    this.prepTime = prepTime;
  }

  @Override
  public String toString() {
    return "Order [id=" + id + ", name=" + name + ", prepTime=" + prepTime + "]";
  }

}
