package rnd.statemachine;

public interface StateTransitionsManager {
    public ProcessData processEvent(ProcessData data) throws ProcessException;
}
