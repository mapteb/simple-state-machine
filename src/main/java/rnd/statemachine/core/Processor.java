package rnd.statemachine.core;

public interface Processor {
    public ProcessData process(ProcessData data) throws ProcessException;
}
