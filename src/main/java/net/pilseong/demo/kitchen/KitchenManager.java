package net.pilseong.demo.kitchen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.pilseong.demo.entity.Order;
import net.pilseong.demo.order.OrderManager;

// recevies orders from Dispatcher and allocate orders to kitchens
// and acts as the delegate of all the kitchens. Because 
// each kitchen thread is not Managed by Spring Framework
@Component
public class KitchenManager {

  @Autowired
  private OrderManager orderBoardManager;

  public Kitchen update(Order order) {
    
    System.out.println(
      String.format("%s SAYS order %s has been received", 
        "[KITCHEN MANAGER]", order.getName()));

    // create kitchen to process the order
    Kitchen kitchen = new Kitchen(this, order);
    this.orderBoardManager.setKitchen(order, kitchen);
    
    kitchen.start();
    return kitchen;
  }

  // request to the OrderManager to update food is ready
  // it's called by kitchen threads
  public void updateFromKitchen(Order order) {
    this.orderBoardManager.updateFoodStatus(order);
  }
}
