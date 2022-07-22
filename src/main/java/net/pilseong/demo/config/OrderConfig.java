package net.pilseong.demo.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.pilseong.demo.Order;
import net.pilseong.demo.OrderController;
import net.pilseong.demo.courier.CourierManager;
import net.pilseong.demo.kitchen.KitchenManager;
import net.pilseong.demo.webserver.WebServer;

@Configuration
public class OrderConfig {
  
  @Bean
  public BlockingQueue<Order> incommingOrderQueue() {
    return new LinkedBlockingQueue<>();
  }

  @Bean
  public KitchenManager kitchenManager() {
    return new KitchenManager(incommingOrderQueue());
  }

  @Bean
  public CourierManager courierManager() {
    return new CourierManager();
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

