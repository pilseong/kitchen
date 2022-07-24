package net.pilseong.demo;

import net.pilseong.demo.entity.Order;

public interface Observer {
  void update(Order order);
}
