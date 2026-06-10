package rnd.statemachine.orderworkflow.state;

import rnd.statemachine.core.WorkflowData;
import rnd.statemachine.core.WorkflowEvent;
import rnd.statemachine.core.WorkflowEventType;

/**
 * DEFAULT -> CHECKOUT -> orderProcessor() -> ORDERCREATED -> PAYMENTPENDING
 * PAYMENTPENDING -> PAY -> paymentProcessor() -> PAYMENTERROR -> PAYMENTPENDING
 * PAYMENTPENDING -> PAY -> paymentProcessor() -> PAYMENTSUCCESS ->
 * PAYMENTSUCCESS
 */
public class OrderEvent implements WorkflowEvent {
    private OrderEventType orderEventType;
    private OrderData orderData;

    @Override
    public void setWorkflowEventType(WorkflowEventType workflowEventType) {
        this.orderEventType = (OrderEventType)workflowEventType;
    }
    @Override
    public WorkflowEventType getwWorkflowEventType() {
        return this.orderEventType;
    }
    
    @Override
    public void setWorkflowData(WorkflowData workflowData) {
        this.orderData = (OrderData)workflowData;
    }

    @Override
    public WorkflowData getWorkflowData() {
        return this.orderData;
    }
}
