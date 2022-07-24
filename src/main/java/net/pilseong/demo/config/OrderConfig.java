package net.pilseong.demo.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.pilseong.demo.OrderManager;
import net.pilseong.demo.OrderStatus;
import net.pilseong.demo.courier.CourierManager;
import net.pilseong.demo.courier.FIFOCourierManager;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.KitchenManager;
import net.pilseong.demo.webserver.OrderController;
import net.pilseong.demo.webserver.WebServer;

@Configuration
public class OrderConfig {
  
  @Bean
  public BlockingQueue<Order> incommingOrderQueue() {
    return new LinkedBlockingQueue<>();
  }

  @Bean
  public Map<UUID, OrderStatus> orderBoard() {
    return Collections.synchronizedMap(new HashMap<>());
  }

  @Bean
  public List<OrderStatus> readyFoodQueue() {
    return Collections.synchronizedList(new ArrayList<>());
  }


  @Bean
  public OrderManager orderManager() {
    return new OrderManager();
  }

  @Bean
  public KitchenManager kitchenManager() {
    return new KitchenManager();
  }

  // @Bean
  // public CourierManager matchedCourierManager() {
  //   return new MatchedCourierManager();
  // }

  @Bean
  public CourierManager fIFOCourierManager() {
    return new FIFOCourierManager();
  }
  

  @Bean
  public OrderController orderController() {
    return new OrderController();
  }

  @Bean
  public WebServer webServer() {
    WebServer webServer = new WebServer(9900);
    webServer.addHandler("/order", orderController());
    return webServer;
  }
}

