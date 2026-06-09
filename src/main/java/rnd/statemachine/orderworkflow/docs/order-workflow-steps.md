```
  Initial State  |  Pre-event |   Processor         |   Post-event    |   Final State

  DEFAULT        ->  CHECKOUT -> orderProcessor()   -> ORDERCREATED   -> PAYMENTPENDING
  PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTERROR   -> PAYMENTPENDING
  PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 
```

onCHECKOUT
orderEvent = eventHandler.handleEvent(orderEvent)
             - orderEvent = eventProcessorRegistry.getNextProcessor(orderEventType).process(orderEvent)
             - eventProcessorRegistry.getNextWorkflowState(orderEventType)
             - persist orderState
             - return orderEvent

return orderEvent.getOrderData