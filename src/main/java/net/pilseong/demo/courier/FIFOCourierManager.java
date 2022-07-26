package net.pilseong.demo.courier;

import java.util.UUID;

import org.springframework.stereotype.Component;

import net.pilseong.demo.Observer;
import net.pilseong.demo.OrderBoardManager;
import net.pilseong.demo.entity.Order;


// @Component
public class FIFOCourierManager implements CourierManager {
  private final OrderBoardManager orderBoardManager;

  public FIFOCourierManager(OrderBoardManager orderBoardManager) {
    this.orderBoardManager = orderBoardManager;
  }

  @Override
  public void update(Order order) {
    System.out.println(String.format("%s says order %s has been received", 
      "CourierManager", order.getName()));

    // dispatching order to a courier
    FIFOCourier courier = new FIFOCourier(this);
    courier.start();
  }

  @Override
  public synchronized void deleteOrder(UUID uuid) {
    this.orderBoardManager.deleteOrder(uuid);
  }

  // FIFO implementation
  public Order retrieveWaitingOrder(Observer courier) {
    return this.orderBoardManager.retrieveWaitingOrder(courier);
  }
}
