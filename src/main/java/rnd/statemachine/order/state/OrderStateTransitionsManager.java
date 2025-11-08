package rnd.statemachine.order.state;

import java.util.Objects;
import org.springframework.context.ApplicationContext;
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
 * calls processStateTransition
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class OrderStateTransitionsManager extends AbstractStateTransitionsManager {

    private final ApplicationContext context;
    private final OrderDbService dbService;

    @Override
    protected ProcessData processStateTransition(ProcessData sdata) throws ProcessException {

        OrderData data = (OrderData) sdata;

        try {
            log.info("Pre-event: " + data.getEvent().toString());  
            Processor processor = this.context.getBean(Objects.requireNonNull(data.getEvent().nextStepProcessor(data.getEvent())));         
            data = (OrderData) processor.process(data);
            log.info("Post-event: " + data.getEvent().toString());
            dbService.getStates().put(data.getOrderId(), (OrderState)data.getEvent().nextState(data.getEvent()));
            log.info("Final state: " + dbService.getStates().get(data.getOrderId()).name());
            log.info("??*************************************");

        } catch (OrderException e) {
            log.info("Post-event: " + ((OrderEvent) data.getEvent()).name());
            dbService.getStates().put(data.getOrderId(), (OrderState)data.getEvent().nextState(data.getEvent()));
            log.info("Final state: " + dbService.getStates().get(data.getOrderId()).name());
            log.info("??*************************************");
            throw new OrderException(((OrderEvent) data.getEvent()).name(), e);
        }
        return data;
    }
}
