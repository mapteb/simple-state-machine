package rnd.statemachine.order;

import rnd.statemachine.ProcessState;
import rnd.statemachine.ProcessEvent;
import rnd.statemachine.Processor;

/**  
 * PRODUCTSREADY  -  order  -> orderProcessor()   -> orderSuccess   -> ORDERREADY
 * ORDERREADY     -  pay    -> paymentProcessor() -> paymentError   -> PMTPENDING
 * PMTPENDING     -  pay    -> paymentProcessor() -> paymentSuccess -> ORDERCOMPLETE
 */
public enum OrderEvent implements ProcessEvent {
    order, pay, orderSuccess, paymentError, paymentSuccess;
}
