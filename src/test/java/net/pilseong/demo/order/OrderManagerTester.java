package net.pilseong.demo.order;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.pilseong.demo.courier.FIFOCourier;
import net.pilseong.demo.utils.Observer;

public class OrderManagerTester extends OrderManager {
  
  public OrderManagerTester() {
    super();
  }

  // for testing purpose
  public Map<UUID, OrderStatus> getOrderBoard() {
    return super.orderBoard;
  }

  public List<OrderStatus> getReadyFoodQueue() {
    return super.readyFoodQueue;
  }

  public List<Observer> getWaitingList() {
    return super.waitingCouriers;
  }

  public void addCourier() {
    super.waitingCouriers.add(
      new FIFOCourier(null, 0, 0));
  }

  public void addWaitingFood(OrderStatus orderStatus) {
    super.readyFoodQueue.add(orderStatus);
  }

}
