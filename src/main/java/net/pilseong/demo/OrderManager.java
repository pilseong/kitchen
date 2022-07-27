package net.pilseong.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import net.pilseong.demo.courier.Courier;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.Kitchen;

@Component
public class OrderBoardManager {
  private final Map<UUID, OrderStatus> orderBoard;
  private final List<OrderStatus> readyFoodQueue;
  
  private List<Observer> waitingCouriers = new ArrayList<>();

  private final List<Long> courierWaitingStat;
  private final List<Long> foodWaitingStat;

  public OrderBoardManager(
    Map<UUID, OrderStatus> orderBoard,
    List<OrderStatus> readyFoodQueue,
    List<Long> courierWaitingStat,
    List<Long> foodWaitingStat) {

      this.orderBoard = orderBoard;
      this.readyFoodQueue = readyFoodQueue;
      this.courierWaitingStat = courierWaitingStat;
      this.foodWaitingStat = foodWaitingStat;
    }
  
  public synchronized void updateFoodStatus(Order order) {
    if (this.orderBoard.containsKey(order.getId())) {
      OrderStatus orderStatus = this.orderBoard.get(order.getId());
      this.readyFoodQueue.add(orderStatus);
      
      if (orderStatus.getCourier() == null && this.waitingCouriers.size() > 0) {
        orderStatus.setCourier(this.waitingCouriers.get(0));
        this.waitingCouriers.remove(0);
        this.readyFoodQueue.remove(orderStatus);
      }
      orderStatus.notifyObservers(order);
    }
  }
  
  public synchronized void setKitchen(Order order, Kitchen kitchen) {
    this.orderBoard.get(order.getId()).setKitchen(kitchen);  
  }

  public synchronized void setCourier(Observer courier, Order order) {
    this.orderBoard.get(order.getId()).setCourier(courier);
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
    Long foodWaitingTime = this.orderBoard.get(uuid).getKitchen().getWaitingTime();
    Long courierWaitingTime = ((Courier)this.orderBoard.get(uuid).getCourier()).getWaitingTime();

    this.foodWaitingStat.add(foodWaitingTime);
    this.courierWaitingStat.add(courierWaitingTime);
    this.orderBoard.remove(uuid);
  }

  public int size() {
    return this.orderBoard.size();
  }
}
