package rnd.statemachine.order;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rnd.statemachine.manager.ProcessData;
import rnd.statemachine.order.statemachine.OrderEvent;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class OrderData implements ProcessData<OrderEvent> {

  private double payment;
  private UUID orderId;

  @Getter(onMethod = @__(@Override))
  private OrderEvent event;

}
