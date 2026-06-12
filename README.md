## Simple State Machine

A simple statemachine for Spring Boot projects. This project presents a simple statemachine framework and a sample usage of the framework for a project like an online order processing. This approach is best suited for business processes that involve multiple steps. Benefits of using this approach are discussed below.

### Application Requirements

When a business process involves multiple steps, we envision a workflow and write the requirements as a set of state transitions. For this demo a business process like customer creates an order and pays for the order is considered. The workflow steps with the following state transitions are considered.
```
  Initial State  |  Pre-event |   Processor         |   Post-event    |   Final State

  DEFAULT        ->  CHECKOUT -> orderProcessor()   -> ORDERCREATED   -> PAYMENTPENDING
  PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTERROR   -> PAYMENTPENDING
  PAYMENTPENDING ->  PAY      -> paymentProcessor() -> PAYMENTSUCCESS -> PAYMENTSUCCESS
 
```
where the PAYMENTERROR is thrown as an exception so the Final State remains unchanged.


### Benefits

By writing the requirements as a set of state transitions we get the following benefits:

1. Enables building robust applications since the application can only be in one of the listed states specified in the requirements.<br>
1. Simplifies writing unit tests since writing three tests for the three processors ensures 100% code coverage.<br>
1. Enables adding new steps to the business process faster due to the modular nature of the framework.

### Framework Usage

To use this framework for a business process that has multiple steps

1. Identify and model the business process steps as state transitions like the one above
1. Then Configure the state and event enums and the state transition rules in the EventProcessorRegistry:
   - [OrderState](https://github.com/mapteb/simple-state-machine/blob/master/src/main/java/rnd/statemachine/orderworkflow/state/OrderState.java)
   - [OrderEvent](https://github.com/mapteb/simple-state-machine/blob/master/src/main/java/rnd/statemachine/orderworkflow/state/OrderEvent.java)
   - [OrderEventType](https://github.com/mapteb/simple-state-machine/blob/master/src/main/java/rnd/statemachine/orderworkflow/state/OrderEventType.java)   
   - [OrderEventProcessorRegistry](https://github.com/mapteb/simple-state-machine/blob/master/src/main/java/rnd/statemachine/orderworkflow/state/OrderEventProcessorRegistry.java)

1. Setup a table to track the state values. In this demo we store the state in a HashMap. Also, for this quick demo we do not store the state history. Note that all the steps in a given process have one primary key. In the example above orderId is used as the key across the steps. In a business process like a timesheet application the key would be composite key like endOfWeekDate-userId, etc.

1. Implement the Processor class for each step. See the [CheckoutProcessor](https://github.com/mapteb/simple-state-machine/blob/master/src/main/java/rnd/statemachine/orderworkflow/processor/CheckoutProcessor.java) and the [PaymentProcessor](https://github.com/mapteb/simple-state-machine/blob/master/src/main/java/rnd/statemachine/orderworkflow/processor/PaymentProcessor.java) classes for examples.

1. Create a controller class. See the [OrderController](https://github.com/mapteb/simple-state-machine/blob/master/src/main/java/rnd/statemachine/web/controller/OrderController.java) for an example.


### Build and Deploy

Run the command ".\gradlew bootRun" at the prject root.

### Integration Testing

Integration tests can be run using the ".\gradlew test" command at the project root.

### JMeter Testing

Multi-user access can be tested using the JMeter [TestPlan script](https://github.com/mapteb/simple-state-machine/blob/master/src/test/jmeter/ssm_apis_testing.jmx).

### Swagger Testing

The Swagger UI can be used for integration testing http://localhost:8080/swagger-ui/index.html

OR the following CURL commands can be used to test the APIs:

For the order sample considered in this project, the following APIs are called to test the order process:
 
1. Test the CHECKOUT event
```
In this demo example the shopping cart content is not considered so RequestBody is empty. This API just creates an order and returns an orderId (as a UUID).

curl -X POST "http://localhost:8080/api/orders" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"userId\": 123, \"cartData\": \"3 pencils\" }"

<< This API returns an ORDERCREATED response with an orderId >>
```
2. Test the error path PAY event with an invalid amount (state remains unchanged due to error)
```
An invalid payment (0.0) is submitted. We use the orderId returned from the above API.

curl -X PUT "http://localhost:8080/api/orders/607b8d29-18d6-4f41-966e-7c26484a742a" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"userId\": 123, \"payment\": 0.0, \"orderId\": \"607b8d29-18d6-4f41-966e-7c26484a742a\" }" -v

<< This API return an HTTP 500 error response >>
```

3. Test the happy  path PAY event
```
Update the order created in step #1. A valid payment (>= 1.0) is submitted. We use the orderId returned from the above Step #1.

curl -X PUT "http://localhost:8080/api/orders/607b8d29-18d6-4f41-966e-7c26484a742a" -H "accept: */*" -H "Content-Type: application/json" -d "{ \"userId\": 123, \"payment\": 1.0, \"orderId\": \"607b8d29-18d6-4f41-966e-7c26484a742a\" }"

<< This API returns PAYMENTSUCCESS response >>
```

### Also See

Spring has a more comprehensive statemachine Framework - [Spring Statemachine](https://docs.spring.io/spring-statemachine/docs/current/reference/)
