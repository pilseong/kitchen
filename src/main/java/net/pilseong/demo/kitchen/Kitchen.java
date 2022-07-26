package net.pilseong.demo.kitchen;

import net.pilseong.demo.entity.Order;

public class Kitchen extends Thread {
  private KitchenManager kitchenManager;
  private Order order;

  private Long waitTime;

  public Kitchen(KitchenManager manager, Order order) {
    this.kitchenManager = manager;
    this.order = order;
    this.waitTime = 0L;    
  }

  @Override
  public void run() {
    System.out.println(
        String.format("[KITCHEN %s] KITCHEN RECEIVED ORDER %s COOKING %d secs",
            currentThread().getName(), order.getName(), order.getPrepTime()));

    try {
      Thread.sleep(order.getPrepTime() * 1000);

      // food is waiting on
      this.waitTime = System.currentTimeMillis();

      System.out.println(
        String.format("[KITCHEN %s] KITCHEN FINISHED COOKING %s",
            currentThread().getName(), order.getName()));

      kitchenManager.updateFromKitchen(order);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public Long getWaitingTime() {
    return System.currentTimeMillis() - this.waitTime;
  }
}
