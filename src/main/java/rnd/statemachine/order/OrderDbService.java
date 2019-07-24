package rnd.statemachine.order;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class OrderDbService {
    
    private final ConcurrentHashMap<UUID, OrderState> states;
    
    public OrderDbService() {
        this.states = new ConcurrentHashMap<UUID, OrderState>();
    }

    public ConcurrentHashMap<UUID, OrderState> getStates() {
        return states;
    }
}
