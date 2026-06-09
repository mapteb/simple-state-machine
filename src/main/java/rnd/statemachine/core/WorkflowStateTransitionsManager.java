package rnd.statemachine.core;

public interface WorkflowStateTransitionsManager {
    public WorkflowEvent processEvent(WorkflowEvent workflowEvent);
}
