package rnd.statemachine.orderworkflow.service;

import org.springframework.stereotype.Service;

import rnd.statemachine.core.WorkflowEvent;
import rnd.statemachine.core.WorkflowEventHandler;

@Service
public class OrderService implements WorkflowEventHandler {

    @Override
    public WorkflowEvent handle(WorkflowEvent workflowEvent) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
    }
    
}
