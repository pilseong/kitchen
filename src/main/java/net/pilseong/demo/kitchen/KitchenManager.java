package net.pilseong.demo.kitchen;

public class KitchenManager implements Runnable {

  public KitchenManager() {
  }

  @Override
  public void run() {
    System.out.println("KitchenManager thread is up and running");
  }
}
