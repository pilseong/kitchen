package net.pilseong.demo.utils;

import net.pilseong.demo.entity.Order;

public interface Subject {
  void registerObserver(Observer observer);
  void notifyObservers(Order order);
  
}
