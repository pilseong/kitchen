package net.pilseong.demo.kitchen;

import net.pilseong.demo.entity.Order;

public class Kitchen extends Thread {
  private KitchenManager kitchenManager;
  private Order order;

  public Kitchen(KitchenManager manager, Order order) {
    this.kitchenManager = manager;
    this.order = order;
  }

  @Override
  public void run() {
    System.out.println(
        String.format("[Kitchen %s] KITCHEN RECEIVED ORDER %s COOKING %d secs",
            currentThread().getName(), order.getName(), order.getPrepTime()));

    try {
      Thread.sleep(order.getPrepTime() * 1000);

      System.out.println(
        String.format("[Kitchen %s] KITCHEN FINISHED COOKING %s",
            currentThread().getName(), order.getName()));

      kitchenManager.updateFromKitchen(order);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
