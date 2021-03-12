package rnd.statemachine.order;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import rnd.statemachine.manager.ProcessState;

@Service
public class OrderDbService {

  private final ConcurrentHashMap<UUID, ProcessState> states;

  public OrderDbService() {
    this.states = new ConcurrentHashMap<>();
  }

  public void saveState(final UUID uuid, final ProcessState state) {
    states.put(uuid, state);
  }

  public ProcessState getState(final UUID uuid) {
    return states.get(uuid);
  }
}
