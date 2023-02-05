package rnd.statemachine.config;

import java.util.Arrays;
import java.util.List;

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

    /**
     * Returns valid begin states for a given pre-event
     * @param event ProcessEvent - should be a pre-event
     * @return List<ProcessState>
     */
    public static List<ProcessState> beginStates(ProcessEvent preEvent) {
        switch (preEvent.toString()) {
            case "order":
                return Arrays.asList(OrderState.PRODUCTSREADY);
            case "pay":
                return Arrays.asList(OrderState.ORDERREADY, OrderState.PMTPENDING);
            default:
                return null;
        }
    }

    /**
     * Returns the processor's Class object for a given trigger event 
     * @param event ProcessEvent
     * @return Class<? extends Processor>
     */
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

    /**
     * Returns valid next state for a given post-event
     * @param event ProcessEvent - should be a post-event
     * @return ProcessState
     */
    public static ProcessState nextState(ProcessEvent postEvent) {
        switch (postEvent.toString()) {
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
