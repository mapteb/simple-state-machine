package rnd.statemachine.order;

import java.util.UUID;

import rnd.statemachine.order.statemachine.OrderEvent;

class MockData {

    static final UUID orderId = UUID.fromString("cacb4fd3-0139-4402-8ad7-9e8c5aba368a");
    static final UUID unknownOrderId = UUID.fromString("cacb4fd3-0139-4402-8ad7-9e8c5aba368b");
    static final String illegalStateMessage = "Unknown orderId";
    
    static OrderData SubmitSuccessData() {
        return OrderData.builder()
                .orderId(orderId)
                .event(OrderEvent.ORDER_CREATED)
                .build();
    }
    
    static OrderData paymentSuccessData() {
        return OrderData.builder()
                .orderId(orderId)
                .event(OrderEvent.PAYMENT_SUCCESS)
                .build();
    }
    
    static OrderData paymentErrorData() {
        return OrderData.builder()
                .event(OrderEvent.PAYMENT_ERROR)
                .orderId(orderId)
                .build();
    }
}
