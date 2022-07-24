package net.pilseong.demo.courier;

import java.util.UUID;

import net.pilseong.demo.entity.Order;

public interface CourierManager {
  void deleteOrder(UUID id);
  void update(Order order);
}
