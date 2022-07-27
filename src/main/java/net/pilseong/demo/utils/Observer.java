package net.pilseong.demo.utils;

import net.pilseong.demo.entity.Order;

public interface Observer {
  void update(Order order);
}
