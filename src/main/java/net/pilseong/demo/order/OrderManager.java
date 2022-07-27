package net.pilseong.demo.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import net.pilseong.demo.courier.Courier;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.Kitchen;
import net.pilseong.demo.utils.Observer;

// manages order status like adding, removing, update orders and mapping orders to couriers
@Component
public class OrderManager {
  // datas tructures for managing kitchens and couriers
  protected final Map<UUID, OrderStatus> orderBoard;
  protected final List<OrderStatus> readyFoodQueue;
  protected final List<Observer> waitingCouriers;

  // statictics
  protected final List<Long> courierWaitingStat;
  protected final List<Long> foodWaitingStat;

  public OrderManager() {

      this.orderBoard = Collections.synchronizedMap(new HashMap<>());
      this.readyFoodQueue = Collections.synchronizedList(new ArrayList<>());
      this.waitingCouriers = new ArrayList<>();

      this.courierWaitingStat = Collections.synchronizedList(new ArrayList<>());
      this.foodWaitingStat = Collections.synchronizedList(new ArrayList<>());
    }
  
  public synchronized void updateFoodStatus(Order order) {
    if (this.orderBoard.containsKey(order.getId())) {
      OrderStatus orderStatus = this.orderBoard.get(order.getId());
      this.readyFoodQueue.add(orderStatus);
      
      // calling the waiting courier
      if (orderStatus.getCourier() == null && this.waitingCouriers.size() > 0) {
        orderStatus.setCourier(this.waitingCouriers.get(0));
        this.waitingCouriers.remove(0);
        this.readyFoodQueue.remove(orderStatus);
      }
      orderStatus.notifyObservers(order);
    }
  }
  
  // mapping functionalities
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
  
  // couier delete order status and udpate stats
  public synchronized void deleteOrder(UUID uuid) {
    this.orderBoard.remove(uuid);
  }

  public synchronized void updateStats(UUID uuid) {
    Long foodWaitingTime = this.orderBoard.get(uuid).getKitchen().getWaitingTime();
    Long courierWaitingTime = ((Courier)this.orderBoard.get(uuid).getCourier()).getWaitingTime();

    this.foodWaitingStat.add(foodWaitingTime);
    this.courierWaitingStat.add(courierWaitingTime);
  }
  

  // only used for FIFO strategy. it allocate ready food to couriers
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

  // print the order stats so far
  // only used by OrderDispatcher. so just for now it's not synchronuse mehtod
  public synchronized void printStats() {
    // below is for showing statistics
    // after certain amount of time, it shows the stats
    System.out.println("\n-----------------------------------------------------");
    System.out.println("The Number of the PENDING orders  is " + this.orderBoard.size());
    Long total = 0L;
    int size = this.courierWaitingStat.size();
    for (int i=0; i < this.courierWaitingStat.size(); i++) {
      total += this.courierWaitingStat.get(i);
    }
    
    // uncomment to see the time each thread consumed for waiting
    // System.out.println(this.courierWaitingStat.toString());
    System.out.println(String.format("\nThe average waiting time of couriers for %d orders is %f secs", 
        size,  size == 0 ? 0 : total/size/1000.0));

    total = 0L;
    size = this.foodWaitingStat.size();
    for (int i=0; i < this.foodWaitingStat.size(); i++) {
      total += this.foodWaitingStat.get(i);
    }
    
    // uncomment to see the time each thread consumed for waiting
    // System.out.println(this.foodWaitingStat.toString());
    System.out.println(String.format("The average waiting time of foods for %d orders is %f secs", 
        size,  size == 0 ? 0 : total/size/1000.0));
    System.out.println("\n");
  }
}
