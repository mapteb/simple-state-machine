package rnd.statemachine.order.controller;

import java.util.UUID;

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
import rnd.statemachine.order.state.OrderData;
import rnd.statemachine.order.state.OrderEvent;
import rnd.statemachine.order.state.OrderStateTransitionsManager;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderController {
    private final OrderStateTransitionsManager stateTransitionsManager;
        
    // Creates an order and returns the orderId. The order is created in PAYMENTPENDING state.
    // For this demo the cart content is not included. 
    @Operation(summary = "Creates an order and returns the orderId", tags = {"Create an order"})
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createOrder(@RequestBody(required = false) OrderData orderData) {
        if (orderData == null) {
        	orderData = new OrderData();
        }       
        orderData.setEvent(OrderEvent.CHECKOUT);
        orderData = (OrderData) stateTransitionsManager.processEvent(orderData);
        
        return ((OrderEvent)orderData.getEvent()).name() + ", orderId = " + orderData.getOrderId();
    }
    
    // Pays for an order. The orderId is passed as a path variable and the payment amount 
    // is passed in the request body.
    @Operation(summary = "Pay for the orderId", tags = {"Pay for an order"})
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Updates order payment"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })    
    @PutMapping("/{orderId}")
    public String payForOrder(@PathVariable UUID orderId, @RequestBody OrderData orderData) {

    	orderData.setEvent(OrderEvent.PAY);
    	orderData = (OrderData) stateTransitionsManager.processEvent(orderData);
    	
        return ((OrderEvent)orderData.getEvent()).name();
    }    
}

