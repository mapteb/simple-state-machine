package rnd.statemachine.order.state;

import rnd.statemachine.core.ProcessEvent;

/**  
 * DEFAULT    -  submit -> orderProcessor()   -> orderCreated   -> PMTPENDING
 * PMTPENDING -  pay    -> paymentProcessor() -> paymentError   -> PMTPENDING
 * PMTPENDING -  pay    -> paymentProcessor() -> paymentSuccess -> COMPLETED 
 */
public enum OrderEvent implements ProcessEvent {
    submit,
    orderCreated,
    pay,
    paymentSuccess,
    paymentError;
}


