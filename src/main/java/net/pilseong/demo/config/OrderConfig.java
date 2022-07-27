package net.pilseong.demo.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.pilseong.demo.entity.Order;

@Configuration
public class OrderConfig {
  
  // memory sharing for multi threads
  @Bean
  public BlockingQueue<Order> incommingOrderQueue() {
    return new LinkedBlockingQueue<>();
  }

  // @Bean
  // public Map<UUID, OrderStatus> orderBoard() {
  //   return Collections.synchronizedMap(new HashMap<>());
  // }

  // @Bean
  // public List<OrderStatus> readyFoodQueue() {
  //   return Collections.synchronizedList(new ArrayList<>());
  // }

  // @Bean
  // public List<Long> courierWaitingStat() {
  //   return Collections.synchronizedList(new ArrayList<>());
  // }

  // @Bean
  // public List<Long> foodWaitingStat() {
  //   return Collections.synchronizedList(new ArrayList<>());
  // }
}

