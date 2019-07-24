package rnd.statemachine.order;

import rnd.statemachine.ProcessState;
import rnd.statemachine.ProcessEvent;
import rnd.statemachine.Processor;

public enum OrderEvent implements ProcessEvent {

    submit {
        @Override
        public Class<? extends Processor> nextStepProcessor(ProcessEvent event) {
                return OrderProcessor.class;
        }
        
        @Override
        public ProcessState nextState(ProcessEvent event) {
                return null;
        }

    },
    orderCreated {
        @Override
        public Class<? extends Processor> nextStepProcessor(ProcessEvent event) {
            return null;
        }
        
        @Override
        public ProcessState nextState(ProcessEvent event) {
                return OrderState.PaymentPending;
        }

    },
    pay {
        @Override
        public Class<? extends Processor> nextStepProcessor(ProcessEvent event) {
                return PaymentProcessor.class;
        }
        
        @Override
        public ProcessState nextState(ProcessEvent event) {
                return null;
        }
    },
    paymentSuccess {
        @Override
        public Class<? extends Processor> nextStepProcessor(ProcessEvent event) {
            return null;
        }
        @Override
        public ProcessState nextState(ProcessEvent event) {
            // TODO Auto-generated method stub
                return OrderState.Completed;
        }
    },
    paymentError {
        @Override
        public Class<? extends Processor> nextStepProcessor(ProcessEvent event) {
            return null;
        }
        
        @Override
        public ProcessState nextState(ProcessEvent event) {
                return OrderState.PaymentPending;
        }
    };
}
