package rnd.statemachine.manager.exception;

public class ProcessException extends Exception {

  private static final long serialVersionUID = 5285827328563829630L;

  public ProcessException(final String message) {
    super(message);
  }

  public ProcessException(final String message,
                          final Throwable e) {
    super(message, e);
  }
}
