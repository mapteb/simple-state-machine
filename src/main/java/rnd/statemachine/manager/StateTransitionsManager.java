package rnd.statemachine.manager;

import rnd.statemachine.manager.exception.ProcessException;

public interface StateTransitionsManager<T extends ProcessData<? extends ProcessEvent>> {

  T processEvent(T data) throws ProcessException;

}
