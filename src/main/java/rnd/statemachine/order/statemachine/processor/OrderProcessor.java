package rnd.statemachine.order.statemachine.processor;

import org.springframework.stereotype.Service;
import rnd.statemachine.manager.Processor;
import rnd.statemachine.order.OrderData;
import rnd.statemachine.order.statemachine.OrderEvent;

@Service
public class OrderProcessor implements Processor<OrderData> {

  @Override
  public OrderData process(final OrderData data) {
    return OrderData.builder()
        .orderId(data.getOrderId())
        .payment(data.getPayment())
        .event(OrderEvent.ORDER_CREATED)
        .build();
  }

}
