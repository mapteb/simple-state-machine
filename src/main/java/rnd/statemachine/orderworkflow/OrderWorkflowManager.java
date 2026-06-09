package rnd.statemachine.orderworkflow;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rnd.statemachine.core.WorkflowEvent;
import rnd.statemachine.core.WorkflowManager;
import rnd.statemachine.orderworkflow.state.OrderEventProcessorRegistry;
import rnd.statemachine.orderworkflow.state.OrderState;
import rnd.statemachine.orderworkflow.data.OrderWorkflowDB;
import rnd.statemachine.orderworkflow.state.OrderEvent;
import rnd.statemachine.orderworkflow.state.OrderData;

@Service
@RequiredArgsConstructor
public class OrderWorkflowManager implements WorkflowManager {
    private final OrderEventProcessorRegistry eventProcessorRegistry;
    private final OrderWorkflowDB orderWorkflowDB;

    @Override
    public WorkflowEvent process(WorkflowEvent workflowEvent) {
        OrderEvent orderEvent =(OrderEvent) eventProcessorRegistry
            .getNextProcessor(workflowEvent)
            .process(workflowEvent.getWorkflowData());   

        OrderState orderState = (OrderState)eventProcessorRegistry.getNextState(orderEvent.getwWorkflowEventType());
        OrderData orderData = (OrderData)orderEvent.getWorkflowData();
        orderWorkflowDB.setCurrentState(orderData.getOrderId(), orderState);
        return orderEvent;
     }
}
