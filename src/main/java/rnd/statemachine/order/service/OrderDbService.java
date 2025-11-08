package rnd.statemachine.order.service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import rnd.statemachine.order.state.OrderState;

@Component
public class OrderDbService {
    
    // for this demo we are using a Map store
    private ConcurrentHashMap<UUID, OrderState> states = new ConcurrentHashMap<UUID, OrderState>();

    public ConcurrentHashMap<UUID, OrderState> getStates() {
        return states;
    }
}
