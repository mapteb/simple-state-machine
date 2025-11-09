package rnd.statemachine.order.state;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rnd.statemachine.core.ProcessState;
import rnd.statemachine.core.Processor;
import rnd.statemachine.order.processor.OrderProcessor;
import rnd.statemachine.order.processor.PaymentProcessor;

@RequiredArgsConstructor
@Service
public class EventProcessorRegistry {

    private final OrderProcessor orderProcessor;
    private final PaymentProcessor paymentProcessor;

    Processor getNextProcessor(OrderEvent preEvent) {
        return switch(preEvent) {
            case submit -> orderProcessor;
            case pay -> paymentProcessor;
            default -> null;
        };
    }

    ProcessState getNextState(OrderEvent postEvent) {
        return switch(postEvent) {
            case orderCreated, paymentError -> OrderState.PaymentPending;
            case paymentSuccess -> OrderState.Completed;
            default -> null;
        };
    }    
}
    

