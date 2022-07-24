package net.pilseong.demo.kitchen;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import net.pilseong.demo.OrderStatus;
import net.pilseong.demo.entity.Order;

public class KitchenManager {

  @Autowired
  private List<OrderStatus> readyFoodQueue;

  @Autowired
  private Map<UUID, OrderStatus> orderBoard;

  public Kitchen update(Order order) {
    
    System.out.println(
      String.format("%s says order %s has been received", 
        "KitchenManager", order.getName()));

    // create kitchen to process the order
    Kitchen kitchen = new Kitchen(this, order);
    this.orderBoard.get(order.getId()).setKitchen(kitchen);
    
    kitchen.start();
    return kitchen;
  }

  public synchronized void updateFromKitchen(Order order) {
    if (this.orderBoard.containsKey(order.getId())) {
      OrderStatus orderStatus = this.orderBoard.get(order.getId());
      this.readyFoodQueue.add(orderStatus);

      orderStatus.notifyObservers(order);
    }
  }
}
