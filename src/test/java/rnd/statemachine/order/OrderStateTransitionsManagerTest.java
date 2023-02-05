package rnd.statemachine.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import rnd.statemachine.config.AppEventsConfig;

@RunWith(SpringRunner.class)
public class OrderStateTransitionsManagerTest {
       
    @Autowired
    private ApplicationContext context;
    
    @MockBean
    private OrderProcessor orderProcessor;
    
    private OrderDbService dbService = new OrderDbService();
    
    @MockBean 
    PaymentProcessor paymentProcessor;
    
    private OrderStateTransitionsManager transitionsManager;

    @Before
    public void initOrderStateTransitionsManager() {
        transitionsManager = new OrderStateTransitionsManager(context, dbService);
    }
        
    @Test
    public void givenOrderSubmit_whenOrderCreated_thenAssertPaymentPendingState() throws Exception {
        OrderData data = OrderData.builder().event(OrderEvent.order).build();
        
        // transitionsManager = new OrderStateTransitionsManager(context, dbService);
        when(orderProcessor.process(any())).thenReturn(MockData.SubmitSuccessData());
        data = (OrderData)transitionsManager.processEvent(data);
        
        assertThat(AppEventsConfig.nextState(data.getEvent())).isEqualTo(OrderState.ORDERREADY);
    } 
    
    @Test
    public void givenOrderPay_whenPaymentEror_thenAssertPaymentPendingState() throws Exception {
        OrderData data = OrderData.builder()
                .orderId(MockData.orderId)
                .event(OrderEvent.pay).payment(0.00).build();
        dbService.getStates().put(MockData.orderId, OrderState.PMTPENDING);
        // transitionsManager = new OrderStateTransitionsManager(context, dbService);
        when(paymentProcessor.process(any())).thenReturn(MockData.paymentErrorData());
        data = (OrderData)transitionsManager.processEvent(data);
        System.out.println(">> nextState: "+AppEventsConfig.nextState(data.getEvent()).toString());
        assertThat(AppEventsConfig.nextState(data.getEvent())).isEqualTo(OrderState.PMTPENDING);
    }
    
    @Test
    public void givenOrderPay_whenPaymentSuccess_thenAssertCreatedState() throws Exception {
        OrderData data = OrderData.builder()
                .orderId(MockData.orderId)
                .event(OrderEvent.pay).payment(1.00).build();
        
        dbService.getStates().put(MockData.orderId, OrderState.PMTPENDING);
        // transitionsManager = new OrderStateTransitionsManager(context, dbService);
        
        when(paymentProcessor.process(any())).thenReturn(MockData.paymentSuccessData());
        data = (OrderData)transitionsManager.processEvent(data);
        
        assertThat(AppEventsConfig.nextState(data.getEvent())).isEqualTo(OrderState.ORDERCOMPLETE);
    }    
}
