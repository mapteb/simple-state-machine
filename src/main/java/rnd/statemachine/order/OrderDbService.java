package rnd.statemachine.order;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class OrderDbService {
    
    private ConcurrentHashMap<UUID, OrderState> states = new ConcurrentHashMap<UUID, OrderState>();

    public ConcurrentHashMap<UUID, OrderState> getStates() {
        return states;
    }
}
