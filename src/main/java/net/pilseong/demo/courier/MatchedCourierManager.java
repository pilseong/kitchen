package net.pilseong.demo.courier;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import net.pilseong.demo.OrderStatus;
import net.pilseong.demo.entity.Order;

public class MatchedCourierManager implements CourierManager {
  @Autowired
  private Map<UUID, OrderStatus> orderBoard;
  
  // private Map<UUID, Courier> couriers = new HashMap<>();

  public void update(Order order) {
    System.out.println(String.format("%s says order %s has been received", 
      "CourierManager", order.getName()));

    // dispatching order to a courier
    MatchedCourier courier = new MatchedCourier(this, order);
    // FIFOCourier courier = new FIFOCourier(this, order);
    this.orderBoard.get(order.getId()).setCourier(courier);
    courier.start();
  }

  public void deleteOrder(UUID uuid) {
    this.orderBoard.remove(uuid);
  }
}
