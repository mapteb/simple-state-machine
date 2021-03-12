package rnd.statemachine.order;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.context.ApplicationContext;
import rnd.statemachine.order.exception.OrderException;
import rnd.statemachine.order.statemachine.OrderEvent;
import rnd.statemachine.order.statemachine.OrderState;
import rnd.statemachine.order.statemachine.OrderStateTransitionsManager;
import rnd.statemachine.order.statemachine.processor.OrderProcessor;
import rnd.statemachine.order.statemachine.processor.PaymentProcessor;

class OrderStateTransitionsManagerTest {

  @Mock
  private ApplicationContext context;

  @Mock
  private OrderProcessor orderProcessor;

  @Mock
  private PaymentProcessor paymentProcessor;

  private OrderStateTransitionsManager transitionsManager;

  private final OrderDbService dbService = new OrderDbService();

  @BeforeEach
  void setUp() {
    initMocks(this);
    transitionsManager = new OrderStateTransitionsManager(context, dbService);
  }

  @Test
  void givenOrderSubmit_whenOrderCreated_thenAssertPaymentPendingState() throws Exception {
    final var data = OrderData.builder().event(OrderEvent.SUBMIT).build();

    when(context.getBean(data.getEvent().nextStepProcessor(data.getEvent()))).then(invocation -> orderProcessor);
    when(orderProcessor.process(any())).thenReturn(MockData.SubmitSuccessData());
    final var processed = transitionsManager.processEvent(data);

    assertTrue(dbService.getState(processed.getOrderId()).name().equalsIgnoreCase(OrderState.PAYMENT_PENDING.name()));
  }

  @Test
  void givenOrderPay_whenPaymentEror_thenAssertPaymentPendingState() throws Exception {
    final var data = OrderData.builder()
        .orderId(MockData.orderId)
        .event(OrderEvent.PAY).payment(0.00).build();

    dbService.saveState(MockData.orderId, OrderState.PAYMENT_PENDING);

    when(context.getBean(data.getEvent().nextStepProcessor(data.getEvent()))).then(invocation -> paymentProcessor);
    when(paymentProcessor.process(any())).thenReturn(MockData.paymentErrorData());

    final var processed = transitionsManager.processEvent(data);

    assertTrue(dbService.getState(processed.getOrderId()).name().equalsIgnoreCase(OrderState.PAYMENT_PENDING.name()));
  }

  @Test
  void givenOrderPay_whenPaymentSuccess_thenAssertCreatedState() throws Exception {
    final var data = OrderData.builder()
        .orderId(MockData.orderId)
        .event(OrderEvent.PAY).payment(1.00).build();

    dbService.saveState(MockData.orderId, OrderState.PAYMENT_PENDING);

    when(context.getBean(data.getEvent().nextStepProcessor(data.getEvent()))).then(invocation -> paymentProcessor);
    when(paymentProcessor.process(any())).thenReturn(MockData.paymentSuccessData());
    final var processed = transitionsManager.processEvent(data);

    assertTrue(dbService.getState(processed.getOrderId()).name().equalsIgnoreCase(OrderState.COMPLETED.name()));
  }

  @Test
  void givenOrderPayWithUnknownOrderId_thenAssertOrderExceptionIsThrown() throws Exception {
    final var data = OrderData.builder()
        .event(OrderEvent.PAY)
        .orderId(MockData.unknownOrderId)
        .payment(0.00)
        .build();

    when(context.getBean(data.getEvent().nextStepProcessor(data.getEvent()))).then(invocation -> paymentProcessor);

    assertThrows(OrderException.class, () -> transitionsManager.processEvent(data));
  }
}
