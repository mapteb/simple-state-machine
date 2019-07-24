package rnd.statemachine;

public abstract class AbstractStateTransitionsManager implements StateTransitionsManager {
    protected abstract ProcessData initializeState(ProcessData data) throws ProcessException;
    protected abstract ProcessData processStateTransition(ProcessData data) throws ProcessException;

    @Override
    public ProcessData processEvent(ProcessData data) throws ProcessException {
    	initializeState(data);
        return processStateTransition(data);
    }
}
