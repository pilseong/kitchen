package net.pilseong.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import net.pilseong.demo.courier.CourierManager;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.KitchenManager;
import net.pilseong.demo.order.OrderDispatcher;
import net.pilseong.demo.order.OrderManager;

@ExtendWith(MockitoExtension.class)
public class OrderDispatcherTest {

  private BlockingQueue<Order> incommingOrderQueue;

  @Mock
  private CourierManager courierManager;

  @Mock
  private KitchenManager kitchenManager;

  @Mock
  private OrderManager orderManager;

  @Captor
  ArgumentCaptor<Order> orderCaptor;

  OrderDispatcher orderDispatcher;

  @BeforeEach
  void setup() {
    incommingOrderQueue = new LinkedBlockingQueue<>();


    orderDispatcher = new OrderDispatcher(
        incommingOrderQueue,
        courierManager,
        kitchenManager,
        orderManager);
  };

  @Test
  void testRun() throws InterruptedException {

    this.incommingOrderQueue.add(getOrder());
    this.incommingOrderQueue.add(getOrder());

    Thread thread = new Thread(this.orderDispatcher);
    thread.setName("test");
    thread.start();

    Thread.sleep(500);

    thread.interrupt();

    System.out.println("TEst");

    verify(courierManager, times(2)).update(any(Order.class));
    verify(kitchenManager, times(2)).update(any(Order.class));

    assertThat(incommingOrderQueue.size()).isEqualTo(0);
  }

  private Order getOrder() {
    return new Order
    (UUID.fromString("a8cfcb76-7f24-4420-a5ba-d46dd77bdffd"),
    "Mosquito", 4L);
  }
}