package rnd.statemachine.order.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import rnd.statemachine.order.MockData;
import rnd.statemachine.order.exception.OrderException;
import rnd.statemachine.order.service.OrderDbService;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class tests the three state transitions
 * DEFAULT    ->	submit ->	orderProcessor()   ->	orderCreated   ->	PMTPENDING
 * PMTPENDING ->	pay    ->	paymentProcessor() ->	paymentError   ->	PMTPENDING
 * PMTPENDING ->	pay    ->	paymentProcessor() ->	paymentSuccess ->	COMPLETED
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class OrderStateTransitionsManagerTest {
    
    @Autowired
    private OrderDbService dbService;
    
    @Autowired
    private OrderStateTransitionsManager orderStateTransitionsManager;
    
    @BeforeEach
    void setUp() {
        dbService.getStates().clear(); // Clear state between tests
    }
    
    @Test
    public void givenCreateOrderSubmit_thenAssertPaymentPendingState() throws Exception {
        OrderData data = MockData.CreateOrderSubmitData();
        data = (OrderData) orderStateTransitionsManager.processEvent(data);
        
        assertThat(dbService.getStates().get(data.getOrderId()))
            .isEqualTo(OrderState.PaymentPending);
    } 
    
    @Test
    public void givenOrderPaySubmitAndWrongPay_thenAssertPaymentPendingState() {
        dbService.getStates().put(MockData.getOrderId(), OrderState.PaymentPending);
        OrderData data = MockData.OrderWrongPaySubmitData();
        
        assertThrows(OrderException.class, 
            () -> orderStateTransitionsManager.processEvent(data));
        assertThat(dbService.getStates().get(data.getOrderId()))
            .isEqualTo(OrderState.PaymentPending);
    }  
    
    @Test
    public void givenOrderPaySubmit_thenAssertCompletedState() throws Exception {
        dbService.getStates().put(MockData.getOrderId(), OrderState.PaymentPending);
        OrderData data = MockData.OrderPaySubmitData();
        data = (OrderData) orderStateTransitionsManager.processEvent(data);
        
        assertThat(dbService.getStates().get(data.getOrderId()))
            .isEqualTo(OrderState.Completed);
    }         
}