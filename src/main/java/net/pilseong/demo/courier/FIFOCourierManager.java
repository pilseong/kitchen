package net.pilseong.demo.courier;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import net.pilseong.demo.entity.Order;
import net.pilseong.demo.order.OrderManager;
import net.pilseong.demo.utils.Observer;


// implements first-in first-out strategy
@Component
@Profile("fifo")
public class FIFOCourierManager implements CourierManager {
  private final OrderManager orderBoardManager;
  
  @Value("${order.time.minreach}")
  private int minTimeToReachInSec;
  
  @Value("${order.time.maxreach}")
  private int maxTimeToReachInSec;

  public FIFOCourierManager(OrderManager orderBoardManager) {
    this.orderBoardManager = orderBoardManager;
  }

  @Override
  public void update(Order order) {
    System.out.println(String.format("%s SAYS order %s has been received", 
      "[COURIER MANAGER]", order.getName()));

    // dispatching order to a courier
    FIFOCourier courier = new FIFOCourier(this, 
        minTimeToReachInSec, maxTimeToReachInSec);
    courier.start();
  }

  @Override
  public synchronized void deleteOrder(UUID uuid) {
    this.orderBoardManager.updateStats(uuid);
    this.orderBoardManager.deleteOrder(uuid);
  }

  // without pre-allocation. when the courier arrived at the spot, the courier 
  // asked the manager which food to get a food (delegation)
  // FIFO implementation
  public Order retrieveWaitingOrder(Observer courier) {
    return this.orderBoardManager.retrieveWaitingOrder(courier);
  }
}
