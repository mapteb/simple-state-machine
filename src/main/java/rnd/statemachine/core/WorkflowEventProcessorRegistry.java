package rnd.statemachine.core;

import java.util.List;

public interface WorkflowEventProcessorRegistry {
    public List<WorkflowState> requiredWorkflowStates(WorkflowEventType eventType);

    public WorkflowProcessor getNextProcessor(WorkflowEvent event);

    public WorkflowState getNextState(WorkflowEventType eventType);

}
