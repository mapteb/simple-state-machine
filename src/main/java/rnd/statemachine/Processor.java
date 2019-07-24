package rnd.statemachine;

public interface Processor {
    public ProcessData process(ProcessData data) throws ProcessException;
}
