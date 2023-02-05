## simple-state-machine/refactor-1

A simple state machine for Spring Boot projects

This [branch](https://github.com/mapteb/simple-state-machine/tree/refactor-1) contains a State Transitions framework and a usage of the framework for a sample project like online order processing.


###  Usage Workflow

1. To use this framework first write all the application state transitions and configure the states, events and processors. See the AppEventsConfig class as an example.

2. Then implement the interfaces ProcessState, ProcessEvent and ProcessData.
See OrderState, OrderEvent and OrderData classes for examples.

3. Identify a primary key for the process. For the order process it would be orderId, for a time sheet application it would be userId-week-ending-date etc.

4. Implement the StateTransitionsManager. See the OrderStateTransitionsManager class for an example.

5. Implement the Processor class. See the OrderProcessor and the PaymentProcessor classes for examples.

6. Create a controller class. See the OrderController class for an example.


### Unit Testing

Unit tests can be run using the "\gradlew test" command.


### Integration Testing

The application can be run using the ".\gradlew bootRun" command. For the order sample considered in this project, the following two APIs are created to test the order process:
 
1. User request to create an order. This API is implemented as GET so it can be tested quickly in the browser.
http://localhost:8080/order << creates an order and returns an orderId >>

2. User makes a payment. This API is also implemented as GET so it can be tested quickly in the browser.
http://localhost:8080/order/cart?payment=123&orderId=123

(for quick testing in a browser both of the above are implemented as GET APIs)

When the above APIs are called the console log displays the state transitions that reflect the above table. (Note: payment=0 is used to mock payment error in this example)


### Related Projects

A non-blocking version of this project is at: <https://github.com/mapteb/non-blocking-state-machine>

The technique used in this Java project can be easily adapted to Angular applications. Here is an [Angular example](https://github.com/mapteb/smart-component-for-angular-projects).


### Also see

1. Spring Framework's [State Machine libray](https://docs.spring.io/spring-statemachine/docs/current/reference/)


