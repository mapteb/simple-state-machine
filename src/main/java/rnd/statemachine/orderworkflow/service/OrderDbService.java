package rnd.statemachine.orderworkflow.service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

import rnd.statemachine.orderworkflow.state.OrderState;

@Component
public class OrderDbService {
    
    private ConcurrentMap<UUID, OrderState> states = new ConcurrentHashMap<>();

    public ConcurrentMap<UUID, OrderState> getStates() {
        return states;
    }
}
