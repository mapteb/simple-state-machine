package rnd.statemachine.orderworkflow.state;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import rnd.statemachine.orderworkflow.OrderWorkflowManager;
import rnd.statemachine.orderworkflow.data.OrderWorkflowDB;
import rnd.statemachine.web.controller.MockData;
import rnd.statemachine.web.error.OrderWorkflowException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * These tests confirm that the state changes are saved per the requirements specifications
 */
@SpringBootTest
public class OrderWorkflowManagerTest {

    @Autowired
    private OrderWorkflowManager orderWorkflowManager;

    @Autowired
    private OrderWorkflowDB orderWorkflowDB;
    
    @Test
    void withInitStateUnknown_whenCheckout_thenAssertFinalStateIsPaymentPending() {
        OrderData data = MockData.createOrderSubmitData();
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setWorkflowEventType(OrderEventType.CHECKOUT);
        orderEvent.setWorkflowData(data);
        orderWorkflowManager.process(orderEvent);        
        assertThat(orderWorkflowDB.getCurrentState(data.getOrderId())).isEqualTo(OrderState.PAYMENTPENDING);
    }

    @Test
    void withInitStatePaymentPending_whenInvalidPayment_thenAssertFinalStateIsPaymentPending() {
        OrderData data = MockData.createOrderSubmitData();
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setWorkflowEventType(OrderEventType.CHECKOUT);
        orderEvent.setWorkflowData(data);   
        data = (OrderData)((OrderEvent)orderWorkflowManager.process(orderEvent)).getWorkflowData();

        orderEvent.setWorkflowEventType(OrderEventType.PAY);
        data.setPayment(0.0d);
        orderEvent.setWorkflowData(data);
        assertThatThrownBy(() -> orderWorkflowManager.process(orderEvent))
                .isInstanceOf(OrderWorkflowException.class);
        assertThat(orderWorkflowDB.getCurrentState(data.getOrderId())).isEqualTo(OrderState.PAYMENTPENDING);
    }  
    
    @Test
    void withInitStatePaymentPending_whenValidPayment_thenAssertFinalStateIsPaymentSuccess() {
        OrderData data = MockData.createOrderSubmitData();
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setWorkflowEventType(OrderEventType.CHECKOUT);
        orderEvent.setWorkflowData(data);   
        data = (OrderData)((OrderEvent)orderWorkflowManager.process(orderEvent)).getWorkflowData();

        orderEvent.setWorkflowEventType(OrderEventType.PAY);
        data.setPayment(2.0d);
        orderEvent.setWorkflowData(data);
        orderWorkflowManager.process(orderEvent);
        assertThat(orderWorkflowDB.getCurrentState(data.getOrderId())).isEqualTo(OrderState.PAYMENTSUCCESS);
    }     
}
