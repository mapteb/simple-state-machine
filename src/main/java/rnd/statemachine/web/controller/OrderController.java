package rnd.statemachine.web.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rnd.statemachine.core.WorkflowEvent;
import rnd.statemachine.orderworkflow.OrderWorkflowManager;
import rnd.statemachine.orderworkflow.state.OrderData;
import rnd.statemachine.orderworkflow.state.OrderEvent;
import rnd.statemachine.orderworkflow.state.OrderEventType;
import rnd.statemachine.web.error.OrderWorkflowException;

import java.util.UUID;

import org.springframework.http.HttpStatus;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderController {
    // private final OrderStateTransitionsManager stateTransitionsManager;
    private final OrderWorkflowManager orderWorkflowManager;

    // Creates an order and returns the orderId. The order is created in
    // PAYMENTPENDING state.
    // For this demo the cart content is not included.
    @Operation(summary = "Creates an order and returns the orderId", tags = { "Create an order" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderWorkflowResponse createOrder(@RequestBody OrderData orderData) {
        WorkflowEvent orderEvent = new OrderEvent();
        orderEvent.setWorkflowEventType(OrderEventType.CHECKOUT);
        orderEvent.setWorkflowData(orderData);

        orderEvent = orderWorkflowManager.process(orderEvent);
        orderData = (OrderData)orderEvent.getWorkflowData();
        return new OrderWorkflowResponse(orderData.getUserId(), orderData.getOrderId(), (((OrderEvent)orderEvent).getwWorkflowEventType()).toString());
    }

    // Pays for an order. The orderId is passed as a path variable and the payment
    // amount
    // is passed in the request body.
    @Operation(summary = "Pay for the orderId", tags = { "Pay for an order" })
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Updates order payment"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    
    @PutMapping("/{orderId}")
    public OrderWorkflowResponse payForOrder(@PathVariable("orderId") UUID orderId, @RequestBody OrderData orderData) {
        if(orderData.getUserId().equals(0L)) {
            log.info(">> user not found: {}", orderData.getUserId());
            throw new OrderWorkflowException(">> user not found " + orderData.getUserId());
        }
        WorkflowEvent orderEvent = new OrderEvent();
        orderEvent.setWorkflowEventType(OrderEventType.PAY);
        orderEvent.setWorkflowData(orderData);

        orderEvent = orderWorkflowManager.process(orderEvent);
        orderData = (OrderData)orderEvent.getWorkflowData();
        return new OrderWorkflowResponse(orderData.getUserId(), orderData.getOrderId(), (((OrderEvent)orderEvent).getwWorkflowEventType()).toString());
    }
}
