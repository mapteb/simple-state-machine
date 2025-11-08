package rnd.statemachine.core;

public interface StateTransitionsManager {
    public ProcessData processEvent(ProcessData data) throws ProcessException;
}
