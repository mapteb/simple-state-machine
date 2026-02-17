package rnd.statemachine.order.state;

import rnd.statemachine.core.ProcessEvent;

/**  
 * DEFAULT        ->  CHECKOUT -> orderProcessor()   -> ORDERCREATED   -> PAYMENTPENDING
 * PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTERROR   -> PAYMENTPENDING
 * PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 */
public enum OrderEvent implements ProcessEvent {
    CHECKOUT,
    ORDERCREATED,
    PAY,
    PAYMENTERROR,
    PAYMENTSUCCESS, PAYMENTPENDING
}


