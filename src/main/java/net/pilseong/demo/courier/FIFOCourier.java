package net.pilseong.demo.courier;

import net.pilseong.demo.Observer;
import net.pilseong.demo.entity.Order;

public class FIFOCourier extends Thread implements Observer {
  private FIFOCourierManager courierManager;
  private Order order;
  
  public FIFOCourier(FIFOCourierManager courierManager) {
    this.courierManager = courierManager;
    this.order = null;
  }

  @Override
  public void run() {
    int second = (int) ((Math.random() * (18 - 3)) + 3) + 1;

    System.out.println(
      String.format("[Courier %s] COURIER FETCHD AND TAKE %d secs", 
      currentThread().getName(), second));

    try {
      Thread.sleep(second * 1000);

      System.out.println(
        String.format("[Courier %s] COURIER ARRIVED AT THE KITCHEN", 
        currentThread().getName()));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // find the waiting food at the kitchen
    this.order = this.courierManager.retrieveWaitingOrder(this);

    while (order == null) {
      System.out.println(
        String.format("[Courier %s] COURIER WAITING FOR FOOD", 
        currentThread().getName()));      
      try {
        Thread.sleep(1000000);
      } catch(InterruptedException ex) {}
    }

    System.out.println(
      String.format("[Courier %s] COURIER ENDED %s", 
      currentThread().getName(), order.getName()));
    
    this.courierManager.deleteOrder(order.getId());
  }

  // update by kitchen thread
  @Override
  public void update(Order order) {
    System.out.println(
      String.format("[KITCHEN %s] KITCHEN HANDOVER %s", 
      currentThread().getName(), order.getName()));
      this.order = order;
      this.interrupt();
      return;
  }
}
