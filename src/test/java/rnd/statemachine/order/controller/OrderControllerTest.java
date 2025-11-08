package rnd.statemachine.order.controller;

import rnd.statemachine.order.state.MockData;
import rnd.statemachine.order.state.OrderData;
import rnd.statemachine.order.state.OrderEvent;
import rnd.statemachine.order.state.OrderStateTransitionsManager;
import rnd.statemachine.order.exception.OrderException;
import org.junit.jupiter.api.BeforeEach;
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

// Use WebMvcTest to test only the controller layer
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    // MockMvc is autowired for performing HTTP requests
    @Autowired
    private MockMvc mockMvc;

    // The dependency is mocked to control its behavior
    @MockitoBean
    private OrderStateTransitionsManager stateTransitionsManager;

    private final UUID TEST_ORDER_ID = MockData.getOrderId();

    @BeforeEach
    void setup() {
        // Reset mocks if needed, though WebMvcTest usually handles this per test
    }

    // --- Test Cases for /order/cart ---

    /**
     * P1.1: Successful Payment Transition (Happy Path)
     */
    @Test
    void handleOrderPayment_Success() throws Exception {
        // ARRANGE: Mock the successful state transition
        OrderData mockResult = new OrderData();
        mockResult.setEvent(OrderEvent.paymentSuccess); // The event returned after successful payment

        when(stateTransitionsManager.processEvent(any(OrderData.class)))
                .thenReturn(mockResult);

        // ACT & ASSERT
        mockMvc.perform(get("/order/cart")
                        .param("payment", "100.50")
                        .param("orderId", TEST_ORDER_ID.toString()))
                .andExpect(status().isOk())
                // Verify the response body matches the expected event name
                .andExpect(content().string(OrderEvent.paymentSuccess.name()));

        // VERIFY: Ensure the manager was called
        verify(stateTransitionsManager).processEvent(any(OrderData.class));
    }

    /**
     * E1.1: Invalid State Transition handled by @ExceptionHandler
     */
    @Test
    void handleOrderPayment_OrderException() throws Exception {
        final String ERROR_MESSAGE = "Cannot pay an order in SHIPPED state";

        // ARRANGE: Mock the state manager to throw the expected exception
        when(stateTransitionsManager.processEvent(any(OrderData.class)))
                .thenThrow(new OrderException(ERROR_MESSAGE));

        // ACT & ASSERT
        mockMvc.perform(get("/order/cart")
                        .param("payment", "10.00")
                        .param("orderId", TEST_ORDER_ID.toString()))
                // @ExceptionHandler returns 200 OK by default, with the message in the body
                .andExpect(status().isOk())
                .andExpect(content().string(ERROR_MESSAGE));
    }

    /**
     * I1.2: Invalid Input - Missing orderId parameter
     */
    @Test
    void handleOrderPayment_MissingOrderId() throws Exception {
        // ACT & ASSERT
        mockMvc.perform(get("/order/cart")
                        .param("payment", "100.00"))
                // Missing required parameter results in a 400 Bad Request
                .andExpect(status().isBadRequest());
    }

    // --- Test Cases for /order ---

    /**
     * P2.1: Successful Submission Transition (Happy Path)
     */
    @Test
    void handleOrderSubmit_Success() throws Exception {
        final UUID NEW_ORDER_ID = MockData.getOrderId();
        
        // ARRANGE: Mock the successful state transition, returning a new order ID
        OrderData mockResult = new OrderData();
        mockResult.setEvent(OrderEvent.orderCreated); // The event after successful submission
        mockResult.setOrderId(NEW_ORDER_ID);

        when(stateTransitionsManager.processEvent(any(OrderData.class)))
                .thenReturn(mockResult);

        // ACT & ASSERT
        String expectedResponse = OrderEvent.orderCreated.name() + ", orderId = " + NEW_ORDER_ID;

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        // VERIFY: Ensure the manager was called
        verify(stateTransitionsManager).processEvent(any(OrderData.class));
    }
}
