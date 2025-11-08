package rnd.statemachine.order.state;

import java.util.UUID;

public class MockData {

    static final UUID orderId = UUID.fromString("cacb4fd3-0139-4402-8ad7-9e8c5aba368a");
    static final UUID unknownOrderId = UUID.fromString("cacb4fd3-0139-4402-8ad7-9e8c5aba368b");
    static final String illegalStateMessage = "Unknown orderId";

    public static UUID getOrderId() {
        return orderId;
    }

    static OrderData CreateOrderSubmitData() {
        return OrderData.builder()
                .orderId(null)
                .event(OrderEvent.submit)
                .build();
    }

    static OrderData OrderPaySubmitData() {
        return OrderData.builder()
                .orderId(orderId)
                .payment(123.00d)
                .event(OrderEvent.pay)
                .build();
    }    
    
    static OrderData OrderWrongPaySubmitData() {
        return OrderData.builder()
                .orderId(orderId)
                .payment(0)
                .event(OrderEvent.pay)
                .build();
    }    
    static OrderData SubmitSuccessData() {
        return OrderData.builder()
                .orderId(orderId)
                .event(OrderEvent.submit)
                .build();
    }
    
    static OrderData paymentSuccessData() {
        return OrderData.builder()
                .orderId(orderId)
                .event(OrderEvent.paymentSuccess)
                .build();
    }
    
    static OrderData paymentErrorData() {
        return OrderData.builder()
                .event(OrderEvent.paymentError)
                .orderId(orderId)
                .build();
    }
}
