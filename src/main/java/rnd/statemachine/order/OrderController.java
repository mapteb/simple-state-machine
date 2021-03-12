package rnd.statemachine.order;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rnd.statemachine.manager.exception.ProcessException;
import rnd.statemachine.order.exception.OrderException;
import rnd.statemachine.order.statemachine.OrderEvent;
import rnd.statemachine.order.statemachine.OrderStateTransitionsManager;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class OrderController {

  private final OrderStateTransitionsManager orderStateManager;

  /**
   * Responsible to create a new Order.
   *
   * @return - Returns the String concat with Event name and order id
   * @throws ProcessException - When happened some error in process a new order
   */
  @GetMapping("/order")
  public String submitOrder() throws ProcessException {

    final var processedEvent = orderStateManager.processEvent(
        OrderData.builder()
            .event(OrderEvent.SUBMIT)
            .build()
    );

    return processedEvent.getEvent().name() + ", orderId = " + processedEvent.getOrderId();
  }

  /**
   * Responsible to pay an Order
   *
   * @param payment - Payment value
   * @param orderId - Order id for this payment
   * @return Returns the name of Event
   * @throws ProcessException - An exception when some error occurrence in the process step
   */
  @GetMapping("/order/cart")
  public String handleOrderPayment(
      @RequestParam double payment,
      @RequestParam UUID orderId) throws ProcessException {

    final var processedData = orderStateManager.processEvent(
        OrderData.builder()
            .payment(payment)
            .orderId(orderId)
            .event(OrderEvent.PAY)
            .build()
    );

    return processedData.getEvent().name();
  }

  @ExceptionHandler(value = OrderException.class)
  public String handleOrderException(OrderException e) {
    return e.getMessage();
  }

}


