package rnd.statemachine.config;

import rnd.statemachine.ProcessEvent;
import rnd.statemachine.ProcessState;
import rnd.statemachine.Processor;
import rnd.statemachine.order.OrderProcessor;
import rnd.statemachine.order.OrderState;
import rnd.statemachine.order.PaymentProcessor;

/**  
 * PRODUCTSREADY  -  order  -> orderProcessor()   -> orderSuccess   -> ORDERREADY
 * ORDERREADY     -  pay    -> paymentProcessor() -> paymentError   -> PMTPENDING
 * PMTPENDING     -  pay    -> paymentProcessor() -> paymentSuccess -> ORDERCOMPLETE
 */
public class AppEventsConfig {

    public static Class<? extends Processor> nextStepProcessor(ProcessEvent event) {
        switch (event.toString()) {
            case "order":
                return OrderProcessor.class;
            case "pay":
                return PaymentProcessor.class;
            default:
                return null;
        }
    }

    public static ProcessState nextState(ProcessEvent event) {
        switch (event.toString()) {
            case "orderSuccess":
                return OrderState.ORDERREADY;
            case "paymentError":
                return OrderState.PMTPENDING;
            case "paymentSuccess":
                return OrderState.ORDERCOMPLETE;
            default:
                return null;
        }
    }
}
