package rnd.statemachine.order;

import rnd.statemachine.ProcessState;

/**  
 * PRODUCTSREADY  -  order  -> orderProcessor()   -> orderSuccess   -> ORDERREADY
 * ORDERREADY     -  pay    -> paymentProcessor() -> paymentError   -> PMTPENDING
 * PMTPENDING     -  pay    -> paymentProcessor() -> paymentSuccess -> ORDERCOMPLETE
 */
public enum OrderState implements ProcessState {
    PRODUCTSREADY,
    ORDERREADY,
    PMTPENDING,    
    ORDERCOMPLETE;
}
