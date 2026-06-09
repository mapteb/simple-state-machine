package rnd.statemachine.core;

public interface WorkflowEventHandler {
    public WorkflowEvent handle(WorkflowEvent workflowEvent);
}
