package rnd.statemachine.order.processor;

import org.springframework.stereotype.Service;

import rnd.statemachine.core.ProcessData;
import rnd.statemachine.core.ProcessException;
import rnd.statemachine.core.Processor;
import rnd.statemachine.order.exception.PaymentException;
import rnd.statemachine.order.state.OrderData;
import rnd.statemachine.order.state.OrderEvent;

@Service
public class PaymentProcessor implements Processor {
    @Override
    public ProcessData process(ProcessData data) throws ProcessException {
        if(((OrderData)data).getPayment() < 1.00) {
        	((OrderData)data).setEvent(OrderEvent.paymentError);
            throw new PaymentException(OrderEvent.paymentError.name());
        } else {
            ((OrderData)data).setEvent(OrderEvent.paymentSuccess);
        }
        return data;
    }
}
