package rnd.statemachine.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class OrderWorkflowException extends ResponseStatusException {

    public OrderWorkflowException(String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public OrderWorkflowException(HttpStatus status, String message) {
        super(status, message);
    }
    
}
