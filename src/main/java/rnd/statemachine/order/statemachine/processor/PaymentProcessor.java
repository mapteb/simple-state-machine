package rnd.statemachine.order.statemachine.processor;

import org.springframework.stereotype.Service;
import rnd.statemachine.manager.Processor;
import rnd.statemachine.order.OrderData;
import rnd.statemachine.order.statemachine.OrderEvent;

@Service
public class PaymentProcessor implements Processor<OrderData> {

  @Override
  public OrderData process(final OrderData data) {
    final var processedData = OrderData.builder()
        .orderId(data.getOrderId())
        .payment(data.getPayment());

    if (data.getPayment() < 1.00) {
      processedData.event(OrderEvent.PAYMENT_ERROR);
    } else {
      processedData.event(OrderEvent.PAYMENT_SUCCESS);
    }
    return processedData.build();
  }

}
