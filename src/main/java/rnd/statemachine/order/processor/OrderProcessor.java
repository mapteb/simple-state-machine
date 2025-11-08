package rnd.statemachine.order.processor;

import java.util.UUID;

import org.springframework.stereotype.Service;

import rnd.statemachine.core.ProcessData;
import rnd.statemachine.core.ProcessEvent;
import rnd.statemachine.core.ProcessException;
import rnd.statemachine.core.Processor;
import rnd.statemachine.order.state.OrderData;
import rnd.statemachine.order.state.OrderEvent;

@Service
public class OrderProcessor implements Processor {    
    @Override
    public ProcessData process(ProcessData data) throws ProcessException {
        OrderData orderData = ((OrderData) data);
        ProcessEvent processEvent = orderData.getEvent();
        if (processEvent == OrderEvent.submit) {
            UUID orderId = UUID.randomUUID();
            orderData.setOrderId(orderId);
            orderData.setEvent(OrderEvent.orderCreated);
        } else {
            // TODO: implement a new create order error transition
        }
        return orderData;
    }
}
