package rnd.statemachine.order;

import java.util.UUID;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rnd.statemachine.ProcessException;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderStateTransitionsManager stateTrasitionsManager;

    @GetMapping("/order/pay")
    public String handleTurnstilePay( 
            @RequestParam double payment,
            @RequestParam UUID orderId) throws Exception {

        OrderData data = new OrderData();
    	data.setPayment(payment);
    	data.setOrderId(orderId);
    	data.setEvent(OrderEvent.pay);
    	data = (OrderData)stateTrasitionsManager.processEvent(data);
    	
        return ((OrderEvent)data.getEvent()).name();
    }
    
    @ExceptionHandler(value=OrderException.class)
    public String handleOrderException(OrderException e) {
        return e.getMessage();
    }
    
    @GetMapping("/order/submit")
    public String handleTurnstileTurn() throws ProcessException {

        OrderData data = new OrderData();
        data.setEvent(OrderEvent.submit);
        data = (OrderData)stateTrasitionsManager.processEvent(data);
        
        return ((OrderEvent)data.getEvent()).name() + ", orderId = " + data.getOrderId();
    }
}

