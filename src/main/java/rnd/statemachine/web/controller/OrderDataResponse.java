package rnd.statemachine.web.controller;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderDataResponse {
    private UUID orderId;
    private String message;
    public OrderDataResponse() {}
}
