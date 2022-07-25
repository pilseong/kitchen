package net.pilseong.demo.kitchen;

import org.springframework.beans.factory.annotation.Autowired;

import net.pilseong.demo.OrderBoardManager;
import net.pilseong.demo.entity.Order;

public class KitchenManager {

  @Autowired
  private OrderBoardManager orderBoardManager;

  public Kitchen update(Order order) {
    
    System.out.println(
      String.format("%s says order %s has been received", 
        "KitchenManager", order.getName()));

    // create kitchen to process the order
    Kitchen kitchen = new Kitchen(this, order);
    this.orderBoardManager.setKitchen(order, kitchen);
    
    kitchen.start();
    return kitchen;
  }

  public void updateFromKitchen(Order order) {
    this.orderBoardManager.updateFoodStatus(order);
  }
}
