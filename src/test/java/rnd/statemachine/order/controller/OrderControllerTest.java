package rnd.statemachine.order.controller;

import rnd.statemachine.order.state.OrderData;
import rnd.statemachine.order.state.OrderEvent;
import rnd.statemachine.order.state.OrderStateTransitionsManager;
import rnd.statemachine.order.MockData;
import rnd.statemachine.order.exception.OrderException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderStateTransitionsManager stateTransitionsManager;

    private final UUID TEST_ORDER_ID = MockData.getOrderId();

    @Test
    void handleOrderPayment_Success() throws Exception {
        OrderData mockResult = new OrderData();
        mockResult.setEvent(OrderEvent.paymentSuccess); // The event returned after successful payment

        when(stateTransitionsManager.processEvent(any(OrderData.class)))
                .thenReturn(mockResult);

        mockMvc.perform(get("/order/cart")
                        .param("payment", "100.50")
                        .param("orderId", TEST_ORDER_ID.toString()))
                .andExpect(status().isOk())
                // Verify the response body matches the expected event name
                .andExpect(content().string(OrderEvent.paymentSuccess.name()));

        // Ensure the manager was called
        verify(stateTransitionsManager).processEvent(any(OrderData.class));
    }

    @Test
    void handleOrderPayment_OrderException() throws Exception {
        final String ERROR_MESSAGE = "Cannot pay an order in SHIPPED state";

        when(stateTransitionsManager.processEvent(any(OrderData.class)))
                .thenThrow(new OrderException(ERROR_MESSAGE));

        mockMvc.perform(get("/order/cart")
                        .param("payment", "10.00")
                        .param("orderId", TEST_ORDER_ID.toString()))
                // @ExceptionHandler returns 200 OK by default, with the message in the body
                .andExpect(status().isOk())
                .andExpect(content().string(ERROR_MESSAGE));
    }

    @Test
    void handleOrderPayment_MissingOrderId() throws Exception {
        mockMvc.perform(get("/order/cart")
                        .param("payment", "100.00"))
                // Missing required parameter results in a 400 Bad Request
                .andExpect(status().isBadRequest());
    }

    @Test
    void handleOrderSubmit_Success() throws Exception {
        final UUID NEW_ORDER_ID = MockData.getOrderId();
        
        OrderData mockResult = new OrderData();
        mockResult.setEvent(OrderEvent.orderCreated); // The event after successful submission
        mockResult.setOrderId(NEW_ORDER_ID);

        when(stateTransitionsManager.processEvent(any(OrderData.class)))
                .thenReturn(mockResult);

        String expectedResponse = OrderEvent.orderCreated.name() + ", orderId = " + NEW_ORDER_ID;

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        // Ensure the manager was called
        verify(stateTransitionsManager).processEvent(any(OrderData.class));
    }
}
