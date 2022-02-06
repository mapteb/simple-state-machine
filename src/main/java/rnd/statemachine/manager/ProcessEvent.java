package rnd.statemachine.manager;

public interface ProcessEvent {

  Class<? extends Processor> nextStepProcessor(ProcessEvent event);

  ProcessState nextState(ProcessEvent event);

}
