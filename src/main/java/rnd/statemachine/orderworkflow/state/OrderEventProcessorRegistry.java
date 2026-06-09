package rnd.statemachine.orderworkflow.state;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rnd.statemachine.core.WorkflowState;
import rnd.statemachine.core.WorkflowEvent;
import rnd.statemachine.core.WorkflowEventProcessorRegistry;
import rnd.statemachine.core.WorkflowEventType;
import rnd.statemachine.core.WorkflowProcessor;
import rnd.statemachine.orderworkflow.data.OrderWorkflowDB;
// import rnd.statemachine.orderworkflow.exception.PaymentException;
import rnd.statemachine.orderworkflow.processor.CheckoutProcessor;
import rnd.statemachine.orderworkflow.processor.PaymentProcessor;
// import rnd.statemachine.orderworkflow.processor.PaymentProcessor;
import rnd.statemachine.web.error.OrderWorkflowException;

/**
 * DEFAULT -> CHECKOUT -> orderProcessor() -> ORDERCREATED -> PAYMENTPENDING
 * PAYMENTPENDING -> PAY -> paymentProcessor() -> PAYMENTERROR -> PAYMENTPENDING
 * PAYMENTPENDING -> PAY -> paymentProcessor() -> PAYMENTSUCCESS ->
 * PAYMENTSUCCESS
 */
@RequiredArgsConstructor
@Service
public class OrderEventProcessorRegistry implements WorkflowEventProcessorRegistry {

    private final CheckoutProcessor orderProcessor;
    private final PaymentProcessor paymentProcessor;
    private final OrderWorkflowDB orderWorkflowDB;

    @Override
    public List<WorkflowState> requiredWorkflowStates(WorkflowEventType eventType) {
        return switch ((OrderEventType) eventType) {
            case CHECKOUT -> List.of(OrderState.UNKNOWN);
            case PAY -> List.of(OrderState.PAYMENTPENDING, OrderState.PAYMENTERROR);
            default ->
                throw new OrderWorkflowException("Unknown event type: " + ((OrderEventType) eventType).toString());
        };
    }

    @Override
    public WorkflowProcessor getNextProcessor(WorkflowEvent event) {
        if(!requiredWorkflowStates(event.getwWorkflowEventType()).contains(
            orderWorkflowDB.getCurrentState(((OrderData)(event.getWorkflowData())).getOrderId())
            )) {
            throw new OrderWorkflowException("Unknown event state");
        }
        return switch ((OrderEventType)event.getwWorkflowEventType()) {
            case CHECKOUT -> orderProcessor;
            case PAY -> paymentProcessor;        
            default -> null;
        };
    }

    @Override
    public WorkflowState getNextState(WorkflowEventType eventType) {
        return switch ((OrderEventType) eventType) {
            case ORDERCREATED, PAYMENTERROR -> OrderState.PAYMENTPENDING;
            case PAYMENTSUCCESS -> OrderState.PAYMENTSUCCESS;
            default -> null;
        };
    }
}
