package rnd.statemachine.orderworkflow.data;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import rnd.statemachine.orderworkflow.state.OrderState;

@Component
public class OrderWorkflowDB {
    private ConcurrentHashMap<UUID, OrderState> currentState = new ConcurrentHashMap<>();

    public OrderState getCurrentState(UUID uuid) {
        if (uuid == null || currentState.get(uuid) == null) {
            return OrderState.UNKNOWN;
        }
        return currentState.get(uuid);
    }

    public void setCurrentState(UUID uuid, OrderState newCurrentState) {
        // if (uuid == null || currentState.get(uuid) == null) {
        //     currentState.put(UUID.fromString("000"), newCurrentState);
        // }
        // else {
            this.currentState.merge(uuid, newCurrentState, (oldState, newState) -> newState);
        // }
    }
}
