package rnd.statemachine.order;

import java.util.UUID;

import org.springframework.stereotype.Service;

import rnd.statemachine.ProcessData;
import rnd.statemachine.Processor;
import rnd.statemachine.ProcessException;

@Service
public class OrderProcessor implements Processor {
    private final OrderDbService dbService;

    public OrderProcessor(OrderDbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public ProcessData process(ProcessData data) throws ProcessException{   
        ((OrderData)data).setEvent(OrderEvent.orderSuccess);
        ((OrderData)data).setOrderId(UUID.randomUUID());
        System.out.println(">> states: " + this.dbService.getStates());
        return data;
    }
}
