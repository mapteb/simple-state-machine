package rnd.statemachine.order.state;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rnd.statemachine.core.AbstractStateTransitionsManager;
import rnd.statemachine.core.ProcessData;
import rnd.statemachine.core.ProcessException;
import rnd.statemachine.core.Processor;
import rnd.statemachine.order.exception.OrderException;
import rnd.statemachine.order.service.OrderDbService;

/**
 * This class manages various state transitions 
 * based on the event
 * The superclass AbstractStateTransitionsManager
 * calls the two methods initializeState and 
 * processStateTransition in that order
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class OrderStateTransitionsManager extends AbstractStateTransitionsManager {

    private final EventProcessorRegistry eventsHandler;
    private final OrderDbService dbService;

    @Override
    protected ProcessData processStateTransition(ProcessData sdata) throws ProcessException {

        OrderData data = (OrderData) sdata;

        try {
            log.info("Pre-event: " + data.getEvent().toString());  
            Processor processor = eventsHandler.getNextProcessor((OrderEvent)data.getEvent());
            data = (OrderData) processor.process(data);
            log.info("Post-event: " + data.getEvent().toString());
            dbService.getStates().put(data.getOrderId(), (OrderState)eventsHandler.getNextState((OrderEvent)data.getEvent()));
            log.info("Final state: " + dbService.getStates().get(data.getOrderId()).name());
            log.info("??*************************************");

        } catch (OrderException e) {
            log.info("Post-event: " + ((OrderEvent) data.getEvent()).name());
            dbService.getStates().put(data.getOrderId(), (OrderState)eventsHandler.getNextState((OrderEvent)data.getEvent()));
            log.info("Final state: " + dbService.getStates().get(data.getOrderId()).name());
            log.info("??*************************************");
            throw new OrderException(((OrderEvent) data.getEvent()).name(), e);
        }
        return data;
    }

    /*
    private OrderData checkStateForReturningCustomers(OrderData data) throws OrderException {
        // returning customers must have a state
        if (data.getOrderId() != null) {
            if (this.dbService.getStates().get(data.getOrderId()) == null) {
                throw new OrderException("No state exists for orderId=" + data.getOrderId());
            } else if (this.dbService.getStates().get(data.getOrderId()) == OrderState.Completed) {
                throw new OrderException("Order is completed for orderId=" + data.getOrderId());
            } else {
                log.info("Initial state: " + dbService.getStates().get(data.getOrderId()).name());
            }
        }
        return data;
    }

     @Override
    protected ProcessData initializeState(ProcessData sdata) throws OrderException {

        OrderData data = (OrderData) sdata;

        if (data.getOrderId() != null) {
            return checkStateForReturningCustomers(data);
        }

        UUID orderId = UUID.randomUUID();
        data.setOrderId(orderId);
        dbService.getStates().put(orderId, (OrderState) OrderState.Default);

        log.info("Initial state: " + dbService.getStates().get(data.getOrderId()).name());
        return data;
    }

    public ConcurrentHashMap<UUID, OrderState> getStates() {
        return dbService.getStates();
    } */
}
