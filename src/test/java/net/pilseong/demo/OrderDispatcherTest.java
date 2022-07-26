package net.pilseong.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import net.pilseong.demo.courier.CourierManager;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.KitchenManager;

@SpringBootTest
public class OrderDispatcherTest {
  
  @MockBean
  private BlockingQueue<Order> incommingOrderQueue;

  @MockBean
  private CourierManager courierManager;

  @MockBean
  private KitchenManager kitchenManager;

  @MockBean
  private OrderBoardManager orderBoardManager;

  @Autowired
  OrderDispatcher orderDisOrderDispatcher;

  @Test
  void testRun() throws InterruptedException {

    when(incommingOrderQueue.take()).thenReturn(getOrder());

    Thread thread = new Thread(this.orderDisOrderDispatcher);
    thread.setName("test");
    thread.start();

    Thread.sleep(1000);
    thread.interrupt();

    assertThat(this.orderBoardManager.size()).isEqualTo(1);
  }

  private Order getOrder() {
    return new Order
    (UUID.fromString("a8cfcb76-7f24-4420-a5ba-d46dd77bdffd"), 
    "Mosquito", 4L);

  }

}
