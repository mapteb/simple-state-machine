package rnd.statemachine.order.state;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rnd.statemachine.core.ProcessState;
import rnd.statemachine.core.Processor;
import rnd.statemachine.order.exception.PaymentException;
import rnd.statemachine.order.processor.OrderProcessor;
import rnd.statemachine.order.processor.PaymentProcessor;

/**
 * DEFAULT -> CHECKOUT -> orderProcessor() -> ORDERCREATED -> PAYMENTPENDING
 * PAYMENTPENDING -> PAY -> paymentProcessor() -> PAYMENTERROR -> PAYMENTPENDING
 * PAYMENTPENDING -> PAY -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 */
@RequiredArgsConstructor
@Service
public class EventProcessorRegistry {

    private final OrderProcessor orderProcessor;
    private final PaymentProcessor paymentProcessor;

    Processor getNextProcessor(OrderState orderState, OrderEvent preEvent) {
        return switch (preEvent) {
            case CHECKOUT -> orderProcessor;
            case PAY -> {
                if (orderState != null && orderState == OrderState.PAYMENTSUCCESS) {
                    throw new PaymentException("Cannot pay an order in PAYMENTSUCCESS state");
                }
                yield paymentProcessor;
            }
            default -> null;
        };
    }

    ProcessState getNextState(OrderEvent postEvent) {
        return switch (postEvent) {
            case ORDERCREATED, PAYMENTERROR -> OrderState.PAYMENTPENDING;
            case PAYMENTSUCCESS -> OrderState.PAYMENTSUCCESS;
            default -> null;
        };
    }
}
