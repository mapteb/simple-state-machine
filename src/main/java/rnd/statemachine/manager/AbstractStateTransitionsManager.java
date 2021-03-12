package rnd.statemachine.manager;

import rnd.statemachine.manager.exception.ProcessException;

public abstract class AbstractStateTransitionsManager<T extends ProcessData<? extends ProcessEvent>>
    implements StateTransitionsManager<T> {

  @Override
  public T processEvent(final T data) throws ProcessException {
    final var initializedState = initializeState(data);
    return processStateTransition(initializedState);
  }

  protected abstract T initializeState(T data) throws ProcessException;

  protected abstract T processStateTransition(T data) throws ProcessException;
}
