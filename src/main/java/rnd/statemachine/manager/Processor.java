package rnd.statemachine.manager;

import rnd.statemachine.manager.exception.ProcessException;

public interface Processor<T extends ProcessData<? extends ProcessEvent>> {

  T process(T data) throws ProcessException;

}
