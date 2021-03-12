package rnd.statemachine.order.statemachine;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import rnd.statemachine.manager.AbstractStateTransitionsManager;
import rnd.statemachine.manager.Processor;
import rnd.statemachine.manager.exception.ProcessException;
import rnd.statemachine.order.OrderData;
import rnd.statemachine.order.OrderDbService;
import rnd.statemachine.order.exception.OrderException;

/**
 * This class manages various state transitions
 * based on the event
 * The superclass AbstractStateTransitionsManager
 * calls the two methods initializeState and
 * processStateTransition in that order
 */
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Service
public class OrderStateTransitionsManager extends AbstractStateTransitionsManager<OrderData> {

  private final ApplicationContext context;
  private final OrderDbService dbService;

  @Override
  protected OrderData processStateTransition(final OrderData originalData) throws ProcessException {
    log.info("Pre-event: " + originalData.getEvent());

    var processedData = originalData;

    try {
      final var processor = getOrderProcessor(originalData);
      processedData = processor.process(originalData);
    } catch (final OrderException e) {
      throw new OrderException(originalData.getEvent().name(), e);
    } finally {
      log.info("Post-event: " + processedData.getEvent());

      final var nextEvent = processedData.getEvent().nextState(processedData.getEvent());
      dbService.saveState(processedData.getOrderId(), nextEvent);

      log.info("Final state: " + dbService.getState(originalData.getOrderId()).name());
      log.info("*************************************");
    }

    return processedData;
  }

  @SuppressWarnings("unchecked")
  private Processor<OrderData> getOrderProcessor(final OrderData data) {
    return this.context.getBean(data.getEvent().nextStepProcessor(data.getEvent()));
  }

  @Override
  protected OrderData initializeState(final OrderData input) throws OrderException {
    if (input.getOrderId() != null) {
      return checkStateForReturningCustomers(input);
    }

    final var data = OrderData.builder()
        .orderId(UUID.randomUUID())
        .payment(input.getPayment())
        .event(input.getEvent())
        .build();

    dbService.saveState(data.getOrderId(), OrderState.DEFAULT);

    log.info("Initial state: " + dbService.getState(data.getOrderId()).name());

    return data;
  }

  private OrderData checkStateForReturningCustomers(OrderData data) throws OrderException {
    // returning customers must have a state
    if (data.getOrderId() != null) {
      if (this.dbService.getState(data.getOrderId()) == null) {
        throw new OrderException("No state exists for orderId=" + data.getOrderId());
      } else if (this.dbService.getState(data.getOrderId()) == OrderState.COMPLETED) {
        throw new OrderException("Order is completed for orderId=" + data.getOrderId());
      } else {
        log.info("Initial state: " + dbService.getState(data.getOrderId()).name());
      }
    }
    return data;
  }
}
