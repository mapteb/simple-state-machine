package rnd.statemachine.manager;

public interface ProcessData<T extends ProcessEvent> {

  T getEvent();

}
