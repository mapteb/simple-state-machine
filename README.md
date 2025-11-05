# simple-state-machine

A simple state machine for Spring Boot projects. This project contains a framework and an illustration of the usage of the framework for a sample project like online order processing.

## Benefits

Enables building robust applications,<br>
Simplifies writing unit tests,<br>
Enables adding new processes faster.

## Usage Workflow

|Initial State |Pre-event |   Processor    |        Post-event  |  Final State  |
| --- | --- | --- | --- | --- |  
|DEFAULT    ->|  submit ->| orderProcessor() ->| orderCreated   -> |PMTPENDING |
|PMTPENDING -> | pay    ->| paymentProcessor() ->| paymentError   -> |PMTPENDING |
|PMTPENDING ->|  pay    ->| paymentProcessor() ->| paymentSuccess ->| COMPLETED |

1. To use this framework first create a state transitions table like above.

2. Then implement the interfaces ProcessState and ProcessEvent.
See OrderState and OrderEvent classes for examples

3. Identify a primary key for the process. For the order process it would be orderId, for a time sheet application it would be userId-week-ending-date etc.

4. Implement the StateTransitionsManager. See the OrderStateTransitionsManager class for an example.

5. Implement the Processor class. See the OrderProcessor and the PaymentProcessor classes for examples.

6. Create a controller class. See the OrderController for an example.

### Build

Run the command ".\gradlew build" at the project root

### Unit Testing

Unit tests can be run using the ".\gradlew test" command at the project root.

### Build and Deploy

Run the command ".\gradlew bootRun" at the prject root.

### Integration Testing

For the order sample considered in this project, the following APIs are called to test the order process:
 
1. User request to create an order. This API is implemented as GET so it can be tested quickly in the browser.
```
http://localhost:8080/order 
<< creates an order and returns an orderId (as a UUID). Selected product ids are not included in this demo example  >>
```

2. User makes a wrong payment. This API is also implemented as GET so it can be tested quickly in the browser.
```
http://localhost:8080/order/cart?payment=0&orderId=UUID 
<< where UUID is  the orderId returned by the first API. Payment value less than 1.00 is considered for the error transition >>
```

3. User makes a payment. This API is also implemented as GET so it can be tested quickly in the browser.
```
http://localhost:8080/order/cart?payment=123&orderId=UUID 
<< where UUID is  the orderId returned by the first API. >>
```

<< for quick testing in a browser all of the above APIs are implemented as GET APIs >>
When the above APIs are called the console log displays the state transitions that reflect the above table. (Note: payment=0 is used to mock payment error in this example)

### Related Projects

A refactored versin of this exists in the [branch](https://github.com/mapteb/simple-state-machine/tree/refactor-1).

### Also See

Spring Framework's [State Machine libray](https://docs.spring.io/spring-statemachine/docs/current/reference/)
