package net.pilseong.demo.courier;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import net.pilseong.demo.Observer;
import net.pilseong.demo.OrderStatus;
import net.pilseong.demo.entity.Order;

public class FIFOCourierManager implements CourierManager {
  @Autowired
  private Map<UUID, OrderStatus> orderBoard;

  @Autowired
  private List<OrderStatus> readyFoodQueue;
  
  // private Map<UUID, Courier> couriers = new HashMap<>();

  public void update(Order order) {
    System.out.println(String.format("%s says order %s has been received", 
      "CourierManager", order.getName()));

    // dispatching order to a courier
    FIFOCourier courier = new FIFOCourier(this);
    courier.start();
  }

  public void deleteOrder(UUID uuid) {
    this.orderBoard.remove(uuid);
  }

  // FIFO implementation
  public synchronized Order retrieveWaitingOrder(Observer courier) {
    if (this.readyFoodQueue.size() == 1) {
      Order order =  this.readyFoodQueue.get(0).getOrder();
      this.readyFoodQueue.get(0).setCourier(courier);
      this.readyFoodQueue.remove(0);
      return order;
    } else if (this.readyFoodQueue.size() > 1) {
      int index = (int) Math.random() * this.readyFoodQueue.size();
      Order order = this.readyFoodQueue.get(index).getOrder();
      this.readyFoodQueue.get(index).setCourier(courier);
      this.readyFoodQueue.remove(index);
      return order;
    } else {
      return null;
    }
  }
}
