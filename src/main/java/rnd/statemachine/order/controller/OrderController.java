package rnd.statemachine.order.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rnd.statemachine.core.ProcessException;
import rnd.statemachine.order.exception.OrderException;
import rnd.statemachine.order.state.OrderData;
import rnd.statemachine.order.state.OrderEvent;
import rnd.statemachine.order.state.OrderStateTransitionsManager;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderStateTransitionsManager stateTransitionsManager;

    @GetMapping("/order/cart")
    public String handleOrderPayment( 
            @RequestParam double payment,
            @RequestParam UUID orderId) throws Exception {

        OrderData data = new OrderData();
    	data.setPayment(payment);
    	data.setOrderId(orderId);
    	data.setEvent(OrderEvent.pay);
    	data = (OrderData) stateTransitionsManager.processEvent(data);
    	
        return ((OrderEvent)data.getEvent()).name();
    }
    
    @ExceptionHandler(value=OrderException.class)
    public String handleOrderException(OrderException e) {
        return e.getMessage();
    }
    
    @GetMapping("/order")
    public String handleOrderSubmit() throws ProcessException {

        OrderData data = new OrderData();
        data.setEvent(OrderEvent.submit);
        data = (OrderData) stateTransitionsManager.processEvent(data);
        
        return ((OrderEvent)data.getEvent()).name() + ", orderId = " + data.getOrderId();
    }
}

