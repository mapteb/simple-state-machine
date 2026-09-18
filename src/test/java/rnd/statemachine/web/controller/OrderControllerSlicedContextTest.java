package rnd.statemachine.web.controller;

import rnd.statemachine.core.WorkflowEvent;
import rnd.statemachine.orderworkflow.OrderWorkflowManager;
import rnd.statemachine.orderworkflow.state.OrderData;
import rnd.statemachine.orderworkflow.state.OrderEvent;
import rnd.statemachine.orderworkflow.state.OrderEventType;
import rnd.statemachine.web.controller.MockData;
import rnd.statemachine.web.error.OrderWorkflowException;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(OrderController.class)
class OrderControllerSlicedContextTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private OrderWorkflowManager stateTransitionsManager;

    @Test
    void withValidCart_whenCheckout_shouldReturnOrderCreated() throws Exception {
        UUID newOrderId = MockData.getOrderId();
        OrderData newOrderData = new OrderData();
        newOrderData.setOrderId(newOrderId);
        
        WorkflowEvent mockResult = new OrderEvent();
        mockResult.setWorkflowData(newOrderData);
        mockResult.setWorkflowEventType(OrderEventType.ORDERCREATED); // The event returned after checkout

        when(stateTransitionsManager.process(any(WorkflowEvent.class)))
                .thenReturn(mockResult);

        MvcTestResult result = mockMvc.post().uri("/api/orders")
                .contentType("application/json")
                .content(MockData.createOrderSubmitDataJson())
                .exchange();              

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(result.getResponse().getContentAsString()).contains(OrderEventType.ORDERCREATED.name());
    }    
    
    @Test
    void withInvalidPayment_whenPay_shouldReturnPaymentError() throws Exception {
        UUID newOrderId = MockData.getOrderId();
        OrderData newOrderData = new OrderData();
        newOrderData.setOrderId(newOrderId);   
        
        WorkflowEvent mockResult = new OrderEvent();
        mockResult.setWorkflowData(newOrderData);
        mockResult.setWorkflowEventType(OrderEventType.PAYMENTERROR); // The event returned after invalid payment

        when(stateTransitionsManager.process(any(WorkflowEvent.class)))
                .thenThrow(new OrderWorkflowException("Payment error - cannot be less than 1.0"));

        MvcTestResult result = mockMvc.put().uri("/api/orders/" + newOrderId)
                .contentType("application/json")
                .content(MockData.orderWrongPaySubmitDataJson(newOrderId))
                .exchange();              

        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        assertThat(result.getResponse().getContentAsString())
                .contains("Payment error - cannot be less than 1.0");
    }
    
    @Test
    void withValidPayment_whenPay_shouldReturnPaymentSuccess() throws Exception {
        UUID newOrderId = MockData.getOrderId();
        OrderData newOrderData = new OrderData();
        newOrderData.setOrderId(newOrderId);   
        
        WorkflowEvent mockResult = new OrderEvent();
        mockResult.setWorkflowData(newOrderData);
        mockResult.setWorkflowEventType(OrderEventType.PAYMENTSUCCESS); // The event returned after invalid payment

        when(stateTransitionsManager.process(any(WorkflowEvent.class)))
                .thenReturn(mockResult);

        MvcTestResult result = mockMvc.put().uri("/api/orders/" + newOrderId)
                .contentType("application/json")
                .content(MockData.orderPaySubmitDataJson(newOrderId))
                .exchange();              

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString())
                .contains("PAYMENTSUCCESS");
    } 

    
    @Test
    void withOrderStatusPaymentSuccess_whenPayForOrder_shouldReturnPaymentError() throws Exception {
        UUID newOrderId = MockData.getOrderId();
        OrderData newOrderData = new OrderData();
        newOrderData.setOrderId(newOrderId);   
        
        WorkflowEvent mockResult = new OrderEvent();
        mockResult.setWorkflowData(newOrderData);
        mockResult.setWorkflowEventType(OrderEventType.PAYMENTERROR); // The event returned after invalid payment

        when(stateTransitionsManager.process(any(WorkflowEvent.class)))
                .thenThrow(new OrderWorkflowException("Unknown event state"));

        MvcTestResult result = mockMvc.put().uri("/api/orders/" + newOrderId)
                .contentType("application/json")
                .content(MockData.orderWrongPaySubmitDataJson(newOrderId))
                .exchange();              

        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        assertThat(result.getResponse().getContentAsString())
                .contains("Unknown event state");
    }    
 
}
