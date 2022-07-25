package net.pilseong.demo.courier;

import net.pilseong.demo.Observer;
import net.pilseong.demo.entity.Order;

public class MatchedCourier extends Thread implements Observer {
  private CourierManager courierManager;

  private Order order;
  private boolean hasNoti;
  private boolean hasArrived;

  
  public MatchedCourier(CourierManager courierManager, Order order) {
    this.courierManager = courierManager;
    this.order = order;
    this.hasNoti = false;
    this.hasArrived = false;
  }

  @Override
  public void run() {
    int second = (int) ((Math.random() * (18 - 3)) + 3) + 1;

    System.out.println(
      String.format("[COURIER %s] COURIER FETCH %s TAKE %d secs", 
      currentThread().getName() , order.getName(), second));

    try {
      Thread.sleep(second * 1000);
      this.hasArrived = true;

      System.out.println(
        String.format("[COURIER %s] COURIER ARRIVED %s", 
        currentThread().getName(), order.getName()));

    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    while (!this.hasNoti) {
      System.out.println(
        String.format("[COURIER %s] COURIER WAITING %s", 
        currentThread().getName(), order.getName()));      
      try {
        Thread.sleep(100000);
      } catch(InterruptedException ex) {

      }
    }

    System.out.println(
      String.format("[COURIER %s] COURIER ENDED %s", 
      currentThread().getName(), order.getName()));
    
    this.courierManager.deleteOrder(order.getId());
  }

  @Override
  public void update(Order order) {
    this.hasNoti = true;

    System.out.println(
      String.format("[KITCHEN %s] KITCHEN NOTIFIED Ready %s", 
      currentThread().getName(), order.getName()));

    if (this.hasArrived) {
      this.interrupt();
      return;
    }

    
  }
}
