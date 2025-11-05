package rnd.statemachine.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import rnd.statemachine.ProcessData;

import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class OrderStateTransitionsManagerTest {
       
    @Autowired
    private ApplicationContext context;
    
    @MockitoBean
    private OrderProcessor orderProcessor = new OrderProcessor();
    
    @Autowired
    private OrderDbService dbService;
    
    @MockitoBean 
    private PaymentProcessor paymentProcessor = new PaymentProcessor();
    
    private OrderStateTransitionsManager transitionsManager;
     
    // in progress
    @Disabled
    @Test
    public void givenOrderSubmit_whenOrderCreated_thenAssertPaymentPendingState() throws Exception {
        dbService.getStates().put(MockData.getOrderId(), (OrderState) OrderState.Default);
        OrderData data = MockData.SubmitSuccessData(); // OrderData.builder().event(OrderEvent.submit).build();
        System.out.println(">> data: " + data);
        
        transitionsManager = new OrderStateTransitionsManager(context, dbService);
        when(orderProcessor.process(eq(new OrderData()))).thenReturn(data);

        data = (OrderData)transitionsManager.processEvent(data);
        
        assertThat(transitionsManager.getStates().get(data.getOrderId())).isEqualTo(OrderState.PaymentPending);
    } 
    
    // in progress
    @Disabled
    @Test
    public void givenOrderPay_whenPaymentEror_thenAssertPaymentPendingState() throws Exception {
        OrderData data = MockData.paymentErrorData(); /*OrderData.builder()
                .orderId(MockData.orderId)
                .event(OrderEvent.pay).payment(0.00).build(); */
        dbService.getStates().put(MockData.orderId, OrderState.PaymentPending);
        transitionsManager = new OrderStateTransitionsManager(context, dbService);
        when(paymentProcessor.process(any(ProcessData.class))).thenReturn(data);
        data = (OrderData)transitionsManager.processEvent(data);
        
        assertThat(transitionsManager.getStates().get(data.getOrderId())).isEqualTo(OrderState.PaymentPending);
    }

    // in progress
    @Disabled
    @Test
    public void givenOrderPay_whenPaymentSuccess_thenAssertCreatedState() throws Exception {
        OrderData data = MockData.paymentSuccessData(); /* OrderData.builder()
                .orderId(MockData.orderId)
                .event(OrderEvent.pay).payment(1.00).build(); */
        
        dbService.getStates().put(MockData.orderId, OrderState.PaymentPending);
        transitionsManager = new OrderStateTransitionsManager(context, dbService);
        
        when(paymentProcessor.process(any(ProcessData.class))).thenReturn(data);
        data = (OrderData)transitionsManager.processEvent(data);
        
        assertThat(transitionsManager.getStates().get(data.getOrderId())).isEqualTo(OrderState.Completed);
    }
    
    // in progress
    // @Disabled
    @Test
    public void givenOrderPayWithUnknownOrderId_thenAssertOrderExceptionIsThrown() throws Exception {
        OrderData data = OrderData.builder()
                .event(OrderEvent.pay)
                .orderId(MockData.unknownOrderId)
                .payment(0.00)
                .build();

        transitionsManager = new OrderStateTransitionsManager(context, dbService);
        // data = (OrderData)transitionsManager.processEvent(data);    
        assertThrows(OrderException.class, () -> transitionsManager.processEvent(data));
    }    
}
