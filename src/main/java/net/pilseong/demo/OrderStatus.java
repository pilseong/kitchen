package net.pilseong.demo;

import net.pilseong.demo.courier.Courier;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.Kitchen;

public class OrderStatus implements Subject{
  private Order order;
  private Observer courier;
  private Kitchen kitchen;
  private boolean finished;

  public OrderStatus(Order order, Kitchen kitchen, 
    Courier courier, boolean finished) {
    this.order = order;
    this.kitchen = kitchen;
    this.courier = courier;
    this.finished = finished;
  }

  public OrderStatus(Order order, boolean finished) {
    this.order = order;
    this.finished = finished;
  }

  

  public boolean isFinished() {
    return finished;
  }

  public void setFinished(boolean finished) {
    this.finished = finished;
  }

  public Observer getCourier() {
    return courier;
  }

  public void setCourier(Observer courier) {
    this.courier = courier;
  }

  public Kitchen getKitchen() {
    return kitchen;
  }

  public void setKitchen(Kitchen kitchen) {
    this.kitchen = kitchen;
  }

  @Override
  public String toString() {
    return "OrderStatus [courier=" + courier + ", finished=" + finished + ", kitchen=" + kitchen + ", order=" + order
        + "]";
  }

  @Override
  public void registerObserver(Observer observer) { }

  @Override
  public void notifyObservers(Order order) {
    this.finished = true;
    if (this.courier != null)
      this.courier.update(order);
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }
}
