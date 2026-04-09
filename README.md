## Simple Statemachine

A simple statemachine for Spring Boot projects. This project presents a simple statemachine framework and a sample usage of the framework for a project like an online order processing.

### Application Requirements

In this approach we write the requirements as a set of state transitions. For this demo the following state transitions are considered.
```
  Initial State  |  Pre-event |   Processor         |   Post-event    |   Final State

  DEFAULT        ->  CHECKOUT -> orderProcessor()   -> ORDERCREATED   -> PAYMENTPENDING
  PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTERROR   -> PAYMENTPENDING
  PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 
```
where the PAYMENTERROR is thrown as an exception so the Final State remains unchanged.


### Benefits

By writing the requirements as a set of state transitions we get the following benefits:

Enables building robust applications since the application can only be in one of the three known states specified in the requirements.<br>
Simplifies writing unit tests since writing three tests for the three processors ensures 100% code coverage.<br>
Enables adding new processes faster due to the modular nature of the framework.

### Usage Workflow

1. To use this framework first create a state transitions table like above.

2. Then Configure the transitions in the enums and in the processor registry:
   - [OrderState](https://github.com/mapteb/simple-state-machine/blob/master/src/main/java/rnd/statemachine/order/state/OrderState.java)
   - [OrderEvent](https://github.com/mapteb/simple-state-machine/blob/master/src/main/java/rnd/statemachine/order/state/OrderEvent.java)
   - [EventProcessoryRegistry](https://github.com/mapteb/simple-state-machine/blob/master/src/main/java/rnd/statemachine/order/state/EventProcessorRegistry.java)

3. Identify a primary key for the process. For the order process it would be orderId, for a time sheet application it would be userId-week-ending-date etc. (In this demo we store the state in a HashMap. Also, for this quick demo we do not store the state history.)

4. Implement the StateTransitionsManager. See the OrderStateTransitionsManager class for an example.

5. Implement the Processor class. See the OrderProcessor and the PaymentProcessor classes for examples.

6. Create a controller class. See the OrderController for an example.

### Unit Testing

Unit tests can be run using the ".\gradlew test" command at the project root.

### Build and Deploy

Run the command ".\gradlew bootRun" at the prject root.

### Integration Testing

The Swagger UI can be used for integration testing http://localhost:8080/swagger-ui/index.html

OR the following CURL commands can be used to test the APIs:

For the order sample considered in this project, the following APIs are called to test the order process:
 
1. Test the CHECKOUT event
```
In this demo example the shopping cart content is not considered so RequestBody is empty. This API just creates an order and returns an orderId (as a UUID).

curl -X POST "http://localhost:8080/api/orders" -H "accept: */*" -H "Content-Type: application/json"

<< This API returns an ORDERCREATED response with an orderId >>
```
2. Test the error path PAY event with an invalid amount (state remains unchanged due to error)
```
An invalid payment (0.0) is submitted. We use the orderId returned from the above API.

curl -X PUT "http://localhost:8080/api/orders/607b8d29-18d6-4f41-966e-7c26484a742a" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"payment\": 0.0, \"orderId\": \"607b8d29-18d6-4f41-966e-7c26484a742a\" }" -v

<< This API return an HTTP 500 error response >>
```

3. Test the happy  path PAY event
```
Update the order created in step #1. A valid payment (>= 1.0) is submitted. We use the orderId returned from the above Step #1.

curl -X PUT "http://localhost:8080/api/orders/607b8d29-18d6-4f41-966e-7c26484a742a" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"payment\": 1.0, \"orderId\": \"607b8d29-18d6-4f41-966e-7c26484a742a\" }"

<< This API returns PAYMENTSUCCESS response >>
```

### Also See

Spring has a more comprehensive statemachine Framework - [Spring Statemachine](https://docs.spring.io/spring-statemachine/docs/current/reference/)
