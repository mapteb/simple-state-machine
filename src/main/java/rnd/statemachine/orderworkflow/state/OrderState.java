package rnd.statemachine.orderworkflow.state;

import rnd.statemachine.core.WorkflowState;

/**  
 * DEFAULT        ->  CHECKOUT -> orderProcessor()   -> ORDERCREATED   -> PAYMENTPENDING
 * PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTERROR   -> PAYMENTPENDING
 * PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 */
public enum OrderState implements WorkflowState {
    UNKNOWN,  
    PAYMENTPENDING,
    PAYMENTSUCCESS,
    PAYMENTERROR
}
