package rnd.statemachine.order;

import org.springframework.stereotype.Service;

import rnd.statemachine.ProcessData;
import rnd.statemachine.Processor;
import rnd.statemachine.ProcessException;

@Service
public class OrderProcessor implements Processor {
    @Override
    public ProcessData process(ProcessData data) throws ProcessException{   
        ((OrderData)data).setEvent(OrderEvent.orderCreated); 
        return data;
    }
}
