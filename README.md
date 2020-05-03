# simple-state-machine

A simple state machine for Spring Boot projects

After importing into an IDE like STS can be run as Spring Boot application.

This project contains a framework and an illustration of the usage of the framework for a sample project like online order processing.

## Usage

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

That is it.

For the order sample considered in this project, the following two APIs are created to test the state machine:

http://localhost:8080/order

http://localhost:8080/order/cart?payment=123&orderId=123

(for quick testing in a browser both of the above are implemented as GET APIs)

When the above APIs are called the console log displays the state transitions that reflect the above table. (Note: payment=0 is used to mock payment error in this example)

### Related Projects

A non-blocking version of this project is at: <https://github.com/mapteb/non-blocking-state-machine>

The technique used in this Java project can be easily adapted to front-end JavaScript UI applications. Here is an [example](https://github.com/mapteb/state-transitions-with-webcomponents).

### More Info

More information about this project can be found at:
<https://dzone.com/articles/a-simple-state-machine-for-spring-boot-projects>
