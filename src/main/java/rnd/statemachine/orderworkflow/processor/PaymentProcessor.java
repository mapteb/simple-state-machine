package rnd.statemachine.orderworkflow.processor;

import org.springframework.stereotype.Service;

import rnd.statemachine.core.WorkflowData;
import rnd.statemachine.core.WorkflowEvent;
import rnd.statemachine.core.WorkflowProcessor;
import rnd.statemachine.orderworkflow.state.OrderData;
import rnd.statemachine.orderworkflow.state.OrderEvent;
import rnd.statemachine.orderworkflow.state.OrderEventType;
import rnd.statemachine.web.error.OrderWorkflowException;

@Service
public class PaymentProcessor implements WorkflowProcessor {
    @Override
    public WorkflowEvent process(WorkflowData data) {
        OrderData orderData = (OrderData)data;
        OrderEvent orderEvent = new OrderEvent();
        if(orderData.getPayment() < 1.00) { 
            throw new OrderWorkflowException("Payment error - cannot be less than 1.0");
        } else {
        	orderEvent.setWorkflowEventType(OrderEventType.PAYMENTSUCCESS);
            orderEvent.setWorkflowData(orderData);  
        }
        return orderEvent;
    }
}
