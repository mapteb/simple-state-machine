package rnd.statemachine.core;

public abstract class AbstractStateTransitionsManager implements StateTransitionsManager {

    protected abstract ProcessData processStateTransition(ProcessData data) throws ProcessException;

    @Override
    public ProcessData processEvent(ProcessData data) throws ProcessException {
        return processStateTransition(data);
    }
}
