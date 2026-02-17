package rnd.statemachine.core;

public abstract class AbstractStateTransitionsManager implements StateTransitionsManager {

    protected abstract ProcessData processStateTransition(ProcessData data);

    @Override
    public ProcessData processEvent(ProcessData data) {
        return processStateTransition(data);
    }
}
