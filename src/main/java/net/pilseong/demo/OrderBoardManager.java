package net.pilseong.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.Kitchen;

public class OrderBoardManager {
  @Autowired
  private Map<UUID, OrderStatus> orderBoard;

  @Autowired
  private List<OrderStatus> readyFoodQueue;
  
  private List<Observer> waitingCouriers = new ArrayList<>();
  
  public synchronized void updateFoodStatus(Order order) {
    if (this.orderBoard.containsKey(order.getId())) {
      OrderStatus orderStatus = this.orderBoard.get(order.getId());
      this.readyFoodQueue.add(orderStatus);
      
      if (orderStatus.getCourier() == null && this.waitingCouriers.size() > 0) {
        orderStatus.setCourier(this.waitingCouriers.get(0));
        this.waitingCouriers.remove(0);
      }
      orderStatus.notifyObservers(order);
    }
  }
  
  public synchronized void setKitchen(Order order, Kitchen kitchen) {
    this.orderBoard.get(order.getId()).setKitchen(kitchen);  
  }

  public synchronized void registerOrder(Order order) {
    this.orderBoard.put(order.getId(), 
      new OrderStatus(order, false));
  }
  

  // only used for FIFO strategy
  public synchronized Order retrieveWaitingOrder(Observer courier) {
    
    // there is only one order ready to be delivered
    if (this.readyFoodQueue.size() == 1) {
      Order order =  this.readyFoodQueue.get(0).getOrder();
      this.readyFoodQueue.get(0).setCourier(courier);
      this.readyFoodQueue.remove(0);
      return order;
    // more than one orders waiting for couriers
    } else if (this.readyFoodQueue.size() > 1) {
      int index = (int) Math.random() * this.readyFoodQueue.size();
      Order order = this.readyFoodQueue.get(index).getOrder();
      this.readyFoodQueue.get(index).setCourier(courier);
      this.readyFoodQueue.remove(index);
      return order;
    // courier arrived too early, 
    // then just added to the waiting courier queue for subscription
    } else {
      this.waitingCouriers.add(courier);
      return null;
    }
  }
  
  // couier delette order status
  public synchronized void deleteOrder(UUID uuid) {
    this.orderBoard.remove(uuid);
  }

  public int size() {
    return this.orderBoard.size();
  }
}
