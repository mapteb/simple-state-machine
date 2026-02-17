package rnd.statemachine.order.state;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rnd.statemachine.core.AbstractStateTransitionsManager;
import rnd.statemachine.core.ProcessData;
import rnd.statemachine.core.Processor;
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
    protected ProcessData processStateTransition(ProcessData sdata) {

        OrderData data = (OrderData) sdata;

        log.info("Pre-event: " + data.getEvent().toString());
        OrderState preState;
        if (data.getOrderId() == null) {
            preState = null;
        } else {
            preState = dbService.getStates().get(data.getOrderId());
        }

        Processor processor = eventsHandler.getNextProcessor(preState, (OrderEvent) data.getEvent());
        data = (OrderData) processor.process(data);
        log.info("Post-event: " + data.getEvent().toString());
        dbService.getStates().put(data.getOrderId(),
                (OrderState) eventsHandler.getNextState((OrderEvent) data.getEvent()));
        log.info("Final state: " + dbService.getStates().get(data.getOrderId()).name());
        log.info("??*************************************");

        return data;
    }
}
