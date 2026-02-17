package rnd.statemachine.order.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import rnd.statemachine.order.MockData;
import rnd.statemachine.order.exception.OrderException;
import rnd.statemachine.order.exception.PaymentException;
import rnd.statemachine.order.service.OrderDbService;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class tests the three state transitions
 * 
 * DEFAULT        ->  CHECKOUT -> orderProcessor()   -> ORDERCREATED   -> PAYMENTPENDING
 * PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTERROR   -> PAYMENTPENDING
 * PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
class OrderStateTransitionsManagerTest {
    
    @Autowired
    private OrderDbService dbService;
    
    @Autowired
    private OrderStateTransitionsManager orderStateTransitionsManager;

    UUID testOrderId = MockData.getOrderId();
    
    @BeforeEach
    void setUp() {
        dbService.getStates().clear(); // Clear state between tests
    }
    
    @Test
    void whenCreateOrder_thenAssertPaymentPendingState() {
        OrderData data = MockData.createOrderSubmitData();
        data = (OrderData) orderStateTransitionsManager.processEvent(data);
        testOrderId = data.getOrderId();
        
        assertThat(dbService.getStates()).containsEntry(data.getOrderId(), OrderState.PAYMENTPENDING);
    } 
    
    @Test
    void givenInvalidPaymentAmount_whenPayForOrder_shouldThrowPaymentException() {
        dbService.getStates().put(testOrderId, OrderState.PAYMENTPENDING);
        OrderData data = MockData.orderWrongPaySubmitData(testOrderId);
        
        assertThrows(PaymentException.class, 
            () -> orderStateTransitionsManager.processEvent(data));
        assertThat(dbService.getStates()).containsEntry(data.getOrderId(), OrderState.PAYMENTPENDING);
    }  
    
    @Test
    void givenValidPaymentAmount_whenPayForOrder_thenAssertPaymentSuccessState() {
        dbService.getStates().put(testOrderId, OrderState.PAYMENTPENDING);
        OrderData data = MockData.orderPaySubmitData(testOrderId);
        data = (OrderData) orderStateTransitionsManager.processEvent(data);
        
        assertThat(dbService.getStates()).containsEntry(data.getOrderId(), OrderState.PAYMENTSUCCESS);
    }     
    
    @Test
    void givenPaymentSuccessState_whenPayForOrder_shouldThrowPaymentException() {
        dbService.getStates().put(testOrderId, OrderState.PAYMENTSUCCESS);
        OrderData data = MockData.orderPaySubmitData(testOrderId);
        
        assertThrows(PaymentException.class, 
            () -> orderStateTransitionsManager.processEvent(data));
        // State should remain PAYMENTSUCCESS
        assertThat(dbService.getStates()).containsEntry(data.getOrderId(), OrderState.PAYMENTSUCCESS);
    }    
}