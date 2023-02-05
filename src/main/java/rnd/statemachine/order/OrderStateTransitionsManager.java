package rnd.statemachine.order;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rnd.statemachine.AbstractStateTransitionsManager;
import rnd.statemachine.ProcessData;
import rnd.statemachine.ProcessException;
import rnd.statemachine.ProcessState;
import rnd.statemachine.config.AppEventsConfig;

/**
 * This class manages various state transitions defined in the AppEventsConfig
 * class
 * 
 * The superclass AbstractStateTransitionsManager
 * calls the two methods initializeState and
 * processStateTransition in that order
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class OrderStateTransitionsManager extends AbstractStateTransitionsManager {

    private final ApplicationContext context;
    private final OrderDbService dbService;

    /**
     * This method calls the configured processor for a given pre-event
     * Also saves the final state of each transition to the db.
     * Optionally, can validate the begin state
     * 
     * @param processData ProcessData
     * @return ProcessData
     * @throws ProcessException
     */
    @Override
    protected ProcessData processStateTransition(ProcessData processData) throws ProcessException {

        OrderData data = (OrderData) processData;

        // validate bigin state
        final List<ProcessState> processStates = AppEventsConfig.beginStates(data.getEvent());
        if (!this.dbService.getStates().values().stream().filter(s -> processStates.contains(s)).findAny()
                .isPresent()) {
            throw new ProcessException("Begin State not found");
        }

        // call the configured processor
        log.info("Begin State: " + AppEventsConfig.beginStates(data.getEvent()).toString());
        log.info("Pre-event: " + data.getEvent().toString());
        data = (OrderData) this.context.getBean(AppEventsConfig.nextStepProcessor(data.getEvent())).process(data);
        log.info("Post-event: " + data.getEvent().toString());

        // persist the End State to the DB
        dbService.getStates().put(data.getOrderId(), (OrderState) AppEventsConfig.nextState(data.getEvent()));
        log.info("Final state: " + AppEventsConfig.nextState(data.getEvent()).toString());
        log.info("*************************************");

        return data;
    }

    private OrderData checkStateForReturningCustomers(OrderData data) throws OrderException {
        // returning customers must have a state
        if (data.getOrderId() != null) {
            if (this.dbService.getStates().get(data.getOrderId()) == null) {
                throw new OrderException("No state exists for orderId=" + data.getOrderId());
            } else if (this.dbService.getStates().get(data.getOrderId()) == OrderState.ORDERCOMPLETE) {
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
        dbService.getStates().put(orderId, (OrderState) OrderState.PRODUCTSREADY);

        log.info("Initial state: " + dbService.getStates().get(data.getOrderId()).name());
        return data;
    }

    public ConcurrentHashMap<UUID, OrderState> getStates() {
        return dbService.getStates();
    }
}
