package rnd.statemachine.order.state;

import rnd.statemachine.core.ProcessState;

/**  
 * DEFAULT        ->  CHECKOUT -> orderProcessor()   -> ORDERCREATED   -> PAYMENTPENDING
 * PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTERROR   -> PAYMENTPENDING
 * PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 */
public enum OrderState implements ProcessState {
    DEFAULT,  
    PAYMENTPENDING,
    PAYMENTSUCCESS
}
