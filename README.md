## simple-state-machine

A simple state machine for Spring Boot projects. This project contains a framework and a sample usage of the framework for a project like online order processing.

### Application Requirements

In this approach we write the requirements as a set of state transitions. In this demo the order state transitions are considered.

|Initial State |Pre-event |   Processor    |        Post-event  |  Final State  |
| --- | --- | --- | --- | --- |  
|DEFAULT    ->|  submit ->| orderProcessor() ->| orderCreated   -> |PMTPENDING |
|PMTPENDING -> | pay    ->| paymentProcessor() ->| paymentError   -> |PMTPENDING |
|PMTPENDING ->|  pay    ->| paymentProcessor() ->| paymentSuccess ->| COMPLETED |


### Benefits

By writing the requirements as a set of state transitions we derive the following benefits:

Enables building robust applications since the application can only be in one of the three known states specified in the requirements.<br>
Simplifies writing unit tests since writing three tests for the three processors implies 100% code coverage.<br>
Enables adding new processes faster due to the modular nature of the framework.

### Usage Workflow

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

Unit tests can be run using the ".\gradlew clean build" command at the project root.

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
<< where UUID is  the orderId returned by the API call in Step #1. Returns paymentError status. Payment value less than 1.00 is considered for the error transition >>
```

3. User makes a payment. This API is also implemented as GET so it can be tested quickly in the browser.
```
http://localhost:8080/order/cart?payment=123&orderId=UUID 
<< where UUID is  the orderId returned by API call in Step #1 above. Returns Completed status >>
```

<< for quick testing in a browser all of the above APIs are implemented as GET APIs >>
When the above APIs are called the console log displays the state transitions that reflect the above table. (Note: payment=0 is used to mock payment error in this example)

### Related Projects

A refactored versin of this exists in the [branch](https://github.com/mapteb/simple-state-machine/tree/refactor-1).

### Also See

Spring Framework's [State Machine libray](https://docs.spring.io/spring-statemachine/docs/current/reference/)
