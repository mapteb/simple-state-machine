package rnd.statemachine.order.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rnd.statemachine.order.state.OrderData;
import rnd.statemachine.order.state.OrderEvent;
import rnd.statemachine.order.state.OrderStateTransitionsManager;

@RequiredArgsConstructor
@RequestMapping("/api/orders")
@RestController
public class OrderController {
    private final OrderStateTransitionsManager stateTransitionsManager;
        
    // Creates an order and returns the orderId. The order is created in PAYMENTPENDING state.
    // For this demo the cart content is not included. 
    @PostMapping
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
    @PostMapping("/{orderId}")
    public String payForOrder(@PathVariable UUID orderId, @RequestBody OrderData orderData) {

    	orderData.setEvent(OrderEvent.PAY);
    	orderData = (OrderData) stateTransitionsManager.processEvent(orderData);
    	
        return ((OrderEvent)orderData.getEvent()).name();
    }    
}

