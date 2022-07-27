package net.pilseong.demo.order;

import net.pilseong.demo.courier.MatchedCourier;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.Kitchen;
import net.pilseong.demo.utils.Observer;
import net.pilseong.demo.utils.Subject;

// is datastructure for order management.
// update and modification is done by OrderManager
// every operation needs a lock but I had synchronised methods of Order manager 
// it takes convenience for the sake of the performance.
public class OrderStatus implements Subject{
  private Order order;
  private Observer courier;
  private Kitchen kitchen;
  private boolean finished;

  public OrderStatus(Order order, Kitchen kitchen, 
    MatchedCourier courier, boolean finished) {
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

  // reuse of Subject interface
  // let the courier related to this order that the food is ready
  @Override
  public void notifyObservers(Order order) {
    this.finished = true;
    if (this.courier != null) {
      this.courier.update(order);
    }
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }
}
