package rnd.statemachine.orderworkflow.state;

import rnd.statemachine.core.WorkflowEventType;

public enum OrderEventType implements WorkflowEventType {
    CHECKOUT,
    ORDERCREATED,
    PAY,
    PAYMENTERROR,
    PAYMENTSUCCESS, PAYMENTPENDING;
}
