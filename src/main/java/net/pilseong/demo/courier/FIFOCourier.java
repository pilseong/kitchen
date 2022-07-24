package net.pilseong.demo.courier;

import net.pilseong.demo.Observer;
import net.pilseong.demo.entity.Order;

public class FIFOCourier extends Thread implements Observer {
  private FIFOCourierManager courierManager;

  private boolean hasNoti;
  private boolean hasArrived;

  
  public FIFOCourier(FIFOCourierManager courierManager) {
    this.courierManager = courierManager;
    this.hasNoti = false;
    this.hasArrived = false;
  }

  @Override
  public void run() {
    int second = (int) ((Math.random() * (18 - 3)) + 3) + 1;

    System.out.println(
      String.format("[Courier %s] COURIER FETCHD AND TAKE %d secs", 
      currentThread().getName(), second));

    try {
      Thread.sleep(second * 1000);
      this.hasArrived = true;

      System.out.println(
        String.format("[Courier %s] COURIER ARRIVED AT THE KITCHEN", 
        currentThread().getName()));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // find the waiting food at the kitchen
    Order order = this.courierManager.retrieveWaitingOrder(this);

    while (order == null) {
      System.out.println(
        String.format("[Courier %s] COURIER WAITING FOR FOOD", 
        currentThread().getName()));      
      try {
        Thread.sleep(100000);
      } catch(InterruptedException ex) {}
    }

    System.out.println(
      String.format("[Courier %s] COURIER ENDED %s", 
      currentThread().getName(), order.getName()));
    
    this.courierManager.deleteOrder(order.getId());
  }

  @Override
  public void update(Order order) {
    this.hasNoti = true;

    System.out.println(
      String.format("[Courier %s] COURIER NOTIFIED Ready %s", 
      currentThread().getName(), order.getName()));

    if (this.hasArrived) {
      this.interrupt();
      return;
    }
  }
}
