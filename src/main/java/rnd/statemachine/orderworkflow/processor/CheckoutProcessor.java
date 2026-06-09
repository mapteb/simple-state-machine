package rnd.statemachine.orderworkflow.processor;

import java.util.UUID;

import org.springframework.stereotype.Service;

import rnd.statemachine.core.WorkflowData;
import rnd.statemachine.core.WorkflowEvent;
import rnd.statemachine.core.WorkflowProcessor;
import rnd.statemachine.orderworkflow.state.OrderData;
import rnd.statemachine.orderworkflow.state.OrderEvent;
import rnd.statemachine.orderworkflow.state.OrderEventType;

@Service
public class CheckoutProcessor implements WorkflowProcessor {        

    @Override
    public WorkflowEvent process(WorkflowData data) {
        OrderData orderData = (OrderData)data;
            UUID orderId = UUID.randomUUID();
            orderData.setOrderId(orderId);
            WorkflowEvent orderEvent = new OrderEvent();
            orderEvent.setWorkflowEventType(OrderEventType.ORDERCREATED);
            orderEvent.setWorkflowData(orderData);  
            return orderEvent;    
    }
}
