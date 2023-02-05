package rnd.statemachine;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import rnd.statemachine.order.PaymentProcessor;
import rnd.statemachine.order.OrderDbService;
import rnd.statemachine.order.OrderProcessor;

@SpringBootTest(classes= {PaymentProcessor.class,OrderProcessor.class, OrderDbService.class})
public class StateMachineApplicationTests {

	@Test
	public void contextLoads() {
	}

}
