package rnd.statemachine.order.statemachine;

import rnd.statemachine.manager.ProcessEvent;
import rnd.statemachine.manager.ProcessState;
import rnd.statemachine.order.statemachine.processor.OrderProcessor;
import rnd.statemachine.order.statemachine.processor.PaymentProcessor;

/**
 * DEFAULT    -  SUBMIT -> orderProcessor()   -> orderCreated   -> PMTPENDING
 * PMTPENDING -  PAY    -> paymentProcessor() -> paymentError   -> PMTPENDING
 * PMTPENDING -  PAY    -> paymentProcessor() -> paymentSuccess -> COMPLETED
 */
public enum OrderEvent implements ProcessEvent {

  SUBMIT {
    @Override
    public Class<OrderProcessor> nextStepProcessor(ProcessEvent event) {
      return OrderProcessor.class;
    }

    @Override
    public ProcessState nextState(ProcessEvent event) {
      return OrderState.PAYMENT_PENDING;
    }
  },

  ORDER_CREATED {
    /**
     * This event does not trigger any process
     * So return null
     */
    @Override
    public Class<OrderProcessor> nextStepProcessor(ProcessEvent event) {
      return null;
    }

    @Override
    public ProcessState nextState(ProcessEvent event) {
      return OrderState.PAYMENT_PENDING;
    }

  },

  PAY {
    @Override
    public Class<PaymentProcessor> nextStepProcessor(ProcessEvent event) {
      return PaymentProcessor.class;
    }

    /**
     * This event has no effect on state so return current state
     */
    @Override
    public ProcessState nextState(ProcessEvent event) {
      return OrderState.PAYMENT_PENDING;
    }
  },

  PAYMENT_SUCCESS {
    /**
     * This event does not trigger any process
     * So return null
     */
    @Override
    public Class<PaymentProcessor> nextStepProcessor(ProcessEvent event) {
      return null;
    }

    @Override
    public ProcessState nextState(ProcessEvent event) {
      return OrderState.COMPLETED;
    }
  },

  PAYMENT_ERROR {
    /**
     * This event does not trigger any process
     * So return null
     */
    @Override
    public Class<PaymentProcessor> nextStepProcessor(ProcessEvent event) {
      return null;
    }

    @Override
    public ProcessState nextState(ProcessEvent event) {
      return OrderState.PAYMENT_PENDING;
    }
  }

}
