package rnd.statemachine.core;

// marker
public interface WorkflowEvent { 
    public void setWorkflowEventType(WorkflowEventType workflowEventType);
    public WorkflowEventType getwWorkflowEventType();
    public void setWorkflowData(WorkflowData workflowData);
    public WorkflowData getWorkflowData(); 
}
