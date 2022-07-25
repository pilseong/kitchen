package net.pilseong.demo.courier;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import net.pilseong.demo.Observer;
import net.pilseong.demo.OrderBoardManager;
import net.pilseong.demo.entity.Order;

public class FIFOCourierManager implements CourierManager {
  @Autowired
  private OrderBoardManager orderBoardManager;
  
  // private Map<UUID, Courier> couriers = new HashMap<>();

  public void update(Order order) {
    System.out.println(String.format("%s says order %s has been received", 
      "CourierManager", order.getName()));

    // dispatching order to a courier
    FIFOCourier courier = new FIFOCourier(this);
    courier.start();
  }

  public void deleteOrder(UUID uuid) {
    this.orderBoardManager.deleteOrder(uuid);
  }

  // FIFO implementation
  public Order retrieveWaitingOrder(Observer courier) {
    return this.orderBoardManager.retrieveWaitingOrder(courier);
  }
}
