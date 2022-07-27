package net.pilseong.demo.courier;

import net.pilseong.demo.Observer;
import net.pilseong.demo.entity.Order;

public class FIFOCourier extends Thread implements Observer, Courier {
  private FIFOCourierManager courierManager;
  private Order order;

  private Long waitTime;
  
  // can't inject from property directly. it's not managed bean
  private int minTimeToReachInSec;
  private int maxTimeToReachInSec;
  
  public FIFOCourier(FIFOCourierManager courierManager, 
      int minTimeToReachInSec, int maxTimeToReachInSec) {
    this.courierManager = courierManager;
    this.order = null;
    this.waitTime = 0L;
    
    this.minTimeToReachInSec = minTimeToReachInSec;
    this.maxTimeToReachInSec = maxTimeToReachInSec;
  }

  @Override
  public void run() {

    // courier takes random number of seconds to get the kitchen (3 to 18 secs default)
    int second = (int) ((Math.random() * 
        (maxTimeToReachInSec - minTimeToReachInSec)) + minTimeToReachInSec) + 1;

    System.out.println(
      String.format("[COURIER %s] COURIER FETCHD AND TAKE %d secs", 
      currentThread().getName(), second));

    try {
      Thread.sleep(second * 1000);

      // arrived at the kitchen
      this.waitTime = System.currentTimeMillis();

      System.out.println(
        String.format("[COURIER %s] COURIER ARRIVED AT THE KITCHEN", 
        currentThread().getName()));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // find the waiting food at the kitchen
    this.order = this.courierManager.retrieveWaitingOrder(this);

    // if there is no food then have to wait
    while (order == null) {
      System.out.println(
        String.format("[COURIER %s] COURIER WAITING FOR FOOD", 
        currentThread().getName()));      
      try {
        Thread.sleep(1000000);
      } catch(InterruptedException ex) {}
    }

    // food ready event wakes up the courier thread
    System.out.println(
      String.format("[COURIER %s] COURIER ENDED %s", 
      currentThread().getName(), order.getName()));
    
    this.courierManager.deleteOrder(order.getId());
  }

  // update by kitchen thread that food is ready
  @Override
  public void update(Order order) {
    System.out.println(
      String.format("[KITCHEN %s] KITCHEN HANDOVER %s", 
      currentThread().getName(), order.getName()));
      this.order = order;
      this.interrupt();
      return;
  }

  @Override
  public Long getWaitingTime() {
    return System.currentTimeMillis() - this.waitTime;
  }
}
