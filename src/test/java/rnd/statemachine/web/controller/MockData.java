package rnd.statemachine.web.controller;

import java.util.UUID;

import org.springframework.stereotype.Component;



import rnd.statemachine.orderworkflow.state.OrderData;
import tools.jackson.databind.json.JsonMapper;

@Component
public class MockData {

    static final UUID orderId = UUID.fromString("cacb4fd3-0139-4402-8ad7-9e8c5aba368a");
    static final UUID unknownOrderId = UUID.fromString("cacb4fd3-0139-4402-8ad7-9e8c5aba368b");
    
    public static UUID getOrderId() {
        return orderId;
    }

    public static JsonMapper getJsonMapper() {
        return new JsonMapper();
    }

    public static String createOrderSubmitDataJson() {
        return toJson(createOrderSubmitData());
    }

    public static String orderPaySubmitDataJson(UUID orderId) {
        return toJson(orderPaySubmitData(orderId));          
    }    
    
    public static String orderWrongPaySubmitDataJson(UUID orderId) {
        return toJson(orderWrongPaySubmitData(orderId));          
    }    

    private static String toJson(Object obj) {
        try {
            return getJsonMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }  
    
    public static OrderData createOrderSubmitData() {
        return OrderData.builder()
                .userId(1L)
                .orderId(null)
                .build();
    }

    public static OrderData orderPaySubmitData(UUID orderId) {
        return OrderData.builder()
                .userId(1L)
                .orderId(orderId)
                .payment(123.00d)
                .build();          
    }    
    
    public static OrderData orderWrongPaySubmitData(UUID orderId) {
        return OrderData.builder()
                .userId(1L)
                .orderId(orderId)
                .payment(0.0d)
                .build();          
    }     
}
