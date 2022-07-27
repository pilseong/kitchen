package net.pilseong.demo.courier;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import net.pilseong.demo.entity.Order;
import net.pilseong.demo.order.OrderManager;

@Component
@Profile("matched")
public class MatchedCourierManager implements CourierManager {
  private final OrderManager orderBoardManager;

  // parameterlized the time consumed to go to kitchen
  @Value("${order.time.minreach}")
  private int minTimeToReachInSec;
  
  @Value("${order.time.maxreach}")
  private int maxTimeToReachInSec;
  
  public MatchedCourierManager(OrderManager orderBoardManager) {
    this.orderBoardManager = orderBoardManager;
  }

  @Override
  public void update(Order order) {
    System.out.println(String.format("%s SAYS order %s has been received", 
      "[COURIER MANAGER]", order.getName()));

    // dispatching order to a courier
    MatchedCourier courier = new MatchedCourier(this, order,
        minTimeToReachInSec, maxTimeToReachInSec);

    // register the courier for a specific order
    this.orderBoardManager.setCourier(courier, order);
    courier.start();
  }

  @Override
  public void deleteOrder(UUID uuid) {
    this.orderBoardManager.updateStats(uuid);
    this.orderBoardManager.deleteOrder(uuid);
  }
}
