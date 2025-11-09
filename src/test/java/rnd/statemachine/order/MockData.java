package rnd.statemachine.order;

import java.util.UUID;

import rnd.statemachine.order.state.OrderData;
import rnd.statemachine.order.state.OrderEvent;

public class MockData {

    static final UUID orderId = UUID.fromString("cacb4fd3-0139-4402-8ad7-9e8c5aba368a");
    static final UUID unknownOrderId = UUID.fromString("cacb4fd3-0139-4402-8ad7-9e8c5aba368b");
    static final String illegalStateMessage = "Unknown orderId";

    public static UUID getOrderId() {
        return orderId;
    }

    public static OrderData CreateOrderSubmitData() {
        return OrderData.builder()
                .orderId(null)
                .event(OrderEvent.submit)
                .build();
    }

    public static OrderData OrderPaySubmitData() {
        return OrderData.builder()
                .orderId(orderId)
                .payment(123.00d)
                .event(OrderEvent.pay)
                .build();
    }    
    
    public static OrderData OrderWrongPaySubmitData() {
        return OrderData.builder()
                .orderId(orderId)
                .payment(0)
                .event(OrderEvent.pay)
                .build();
    }    
}
