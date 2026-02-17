package rnd.statemachine.order.controller;

import rnd.statemachine.order.state.OrderData;
import rnd.statemachine.order.state.OrderEvent;
import rnd.statemachine.order.state.OrderStateTransitionsManager;
import rnd.statemachine.order.MockData;
import rnd.statemachine.order.exception.PaymentException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderStateTransitionsManager stateTransitionsManager;

    private UUID testOrderId = MockData.getOrderId();

    @Test
    void withValidCart_whenCheckout_shouldReturnPaymentPending() throws Exception {
        UUID newOrderId = MockData.getOrderId();
        testOrderId = newOrderId; // Update the test order ID for this test case        
        
        OrderData mockResult = new OrderData();
        mockResult.setEvent(OrderEvent.PAYMENTPENDING); // The event after successful submission
        mockResult.setOrderId(newOrderId);

        when(stateTransitionsManager.processEvent(any(OrderData.class)))
                .thenReturn(mockResult);

        String expectedResponse = OrderEvent.PAYMENTPENDING.name() + ", orderId = " + newOrderId;

        mockMvc.perform(post("/api/orders")
                        .contentType("application/json")
                        .content(MockData.createOrderSubmitDataJson())) // Empty JSON body for checkout 
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        // Ensure the manager was called
        verify(stateTransitionsManager).processEvent(any(OrderData.class));
    }    

    @Test
    void withInvalidPayment_whenPay_shouldReturnPaymentError() throws Exception {
        when(stateTransitionsManager.processEvent(any(OrderData.class)))
                .thenThrow(PaymentException.class);

        mockMvc.perform(post("/api/orders/" + testOrderId)
                        .contentType("application/json")
                        .content(MockData.orderWrongPaySubmitDataJson(testOrderId)))       
                .andExpect(status().isInternalServerError());

        // Ensure the manager was called
        verify(stateTransitionsManager).processEvent(any(OrderData.class));
    }

    @Test
    void withValidPayment_whenPay_shouldReturnPaymentSuccess() throws Exception {
        OrderData mockResult = new OrderData();
        mockResult.setEvent(OrderEvent.PAYMENTSUCCESS); // The event returned after successful payment

        when(stateTransitionsManager.processEvent(any(OrderData.class)))
                .thenReturn(mockResult);

        mockMvc.perform(post("/api/orders/" + testOrderId)
                        .contentType("application/json")
                        .content(MockData.orderPaySubmitDataJson(testOrderId)))       
                .andExpect(status().isOk())
                // Verify the response body matches the expected event name
                .andExpect(content().string(OrderEvent.PAYMENTSUCCESS.name()));

        // Ensure the manager was called
        verify(stateTransitionsManager).processEvent(any(OrderData.class));
    } 

    @Test
    void withOrderStatusPaymentSuccess_whenPayForOrder_shouldReturnPaymentError() throws Exception {
        when(stateTransitionsManager.processEvent(any(OrderData.class)))
                .thenThrow(PaymentException.class);

        mockMvc.perform(post("/api/orders/" + testOrderId)
                        .contentType("application/json")
                        .content(MockData.orderPaySubmitDataJson(testOrderId)))       
                .andExpect(status().isInternalServerError());

        // Ensure the manager was called
        verify(stateTransitionsManager).processEvent(any(OrderData.class));
    }    
}
