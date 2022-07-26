package net.pilseong.demo.courier;

import java.util.UUID;

import org.springframework.stereotype.Component;

import net.pilseong.demo.OrderBoardManager;
import net.pilseong.demo.entity.Order;

@Component
public class MatchedCourierManager implements CourierManager {
  private final OrderBoardManager orderBoardManager;
  
  public MatchedCourierManager(OrderBoardManager orderBoardManager) {
    this.orderBoardManager = orderBoardManager;
  }

  @Override
  public void update(Order order) {
    System.out.println(String.format("%s says order %s has been received", 
      "CourierManager", order.getName()));

    // dispatching order to a courier
    MatchedCourier courier = new MatchedCourier(this, order);

    // register the courier for a specific order
    this.orderBoardManager.setCourier(courier, order);
    courier.start();
  }

  @Override
  public void deleteOrder(UUID uuid) {
    this.orderBoardManager.deleteOrder(uuid);
  }
}
