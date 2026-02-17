package rnd.statemachine.order;

import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import rnd.statemachine.order.state.OrderData;
import rnd.statemachine.order.state.OrderEvent;

public class MockData {

    static final UUID orderId = UUID.fromString("cacb4fd3-0139-4402-8ad7-9e8c5aba368a");
    static final UUID unknownOrderId = UUID.fromString("cacb4fd3-0139-4402-8ad7-9e8c5aba368b");
    static final String illegalStateMessage = "Unknown orderId";

    
    public static UUID getOrderId() {
        return orderId;
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
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }  
    
    public static OrderData createOrderSubmitData() {
        return OrderData.builder()
                .orderId(null)
                .event(OrderEvent.CHECKOUT)
                .build();
    }

    public static OrderData orderPaySubmitData(UUID orderId) {
        return OrderData.builder()
                .orderId(orderId)
                .payment(123.00d)
                .event(OrderEvent.PAY)
                .build();          
    }    
    
    public static OrderData orderWrongPaySubmitData(UUID orderId) {
        return OrderData.builder()
                .orderId(orderId)
                .payment(0)
                .event(OrderEvent.PAY)
                .build();          
    }     
}
