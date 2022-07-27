package net.pilseong.demo.order;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.pilseong.demo.courier.FIFOCourier;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.utils.Observer;

public class OrderManagerTest {
  private OrderManagerTester orderManager;

  @BeforeEach
  void setUp() {

    this.orderManager =   new OrderManagerTester();
  }

  
  @Test
  void testRegisterOrder() {

    // execution
    orderManager.registerOrder(getOrder());

    assertEquals(orderManager.getOrderBoard().size(), 1);
    assertEquals(orderManager.getOrderBoard().get(getOrder().getId()).getOrder().getId(), 
      getOrder().getId());
  }

  @Test
  void updateFoodStatusWithNoWaitingCourier() {
    orderManager.registerOrder(getOrder());

    // execution
    orderManager.updateFoodStatus(getOrder());
    
    assertEquals(orderManager.getReadyFoodQueue().size(), 1);
    assertEquals(orderManager.getReadyFoodQueue().get(0).isFinished(), true);
  }

  @Test
  void updateFoodStatusWithWaitingCourier() {
    orderManager.registerOrder(getOrder());
    orderManager.addCourier();

    assertEquals(orderManager.getWaitingList().size(), 1);

    // execution
    orderManager.updateFoodStatus(getOrder());
    
    assertEquals(orderManager.getReadyFoodQueue().size(), 0);
    assertEquals(orderManager.getWaitingList().size(), 0);
  }

  @Test
  void testDeleteOrder() {
    orderManager.registerOrder(getOrder());

    // execution
    orderManager.deleteOrder(getOrder().getId());

    assertEquals(orderManager.getOrderBoard().size(), 0);
  }

  @Test
  void retrieveWaitingOrderWithOneFood() {
    orderManager.addWaitingFood(getOrderStatus());
    
    // execution
    Order retrievedOrder = orderManager.retrieveWaitingOrder(getCourier());

    assertEquals(orderManager.getReadyFoodQueue().size(), 0);
    assertEquals(retrievedOrder.getId(), getOrder().getId());
  }

  @Test
  void retrieveWaitingOrderWithNoWaitingFood() {
    
    // execution
    Order retrievedOrder = orderManager.retrieveWaitingOrder(getCourier());

    assertEquals(orderManager.getReadyFoodQueue().size(), 0);
    assertEquals(orderManager.getWaitingList().size(), 1);
    assertEquals(retrievedOrder, null);
  }

  private Observer getCourier() {
    return new FIFOCourier(null, 0, 0);
  }

  private OrderStatus getOrderStatus() {
    return new OrderStatus(getOrder(), null, null, false);
  }

  private Order getOrder() {
    return new Order
    (UUID.fromString("a8cfcb76-7f24-4420-a5ba-d46dd77bdffd"),
    "Mosquito", 4L);
  }



}
