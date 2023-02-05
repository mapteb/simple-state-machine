package rnd.statemachine.order;

import org.springframework.stereotype.Service;

import rnd.statemachine.ProcessData;
import rnd.statemachine.ProcessException;
import rnd.statemachine.Processor;

@Service
public class PaymentProcessor implements Processor {
    @Override
    public ProcessData process(ProcessData data) throws ProcessException {
        if(((OrderData)data).getPayment() < 1.00) {
        	((OrderData)data).setEvent(OrderEvent.paymentError);
            // throw new PaymentException(OrderEvent.paymentError.name());
        } else {
            ((OrderData)data).setEvent(OrderEvent.paymentSuccess);
        }
        return data;
    }
}
