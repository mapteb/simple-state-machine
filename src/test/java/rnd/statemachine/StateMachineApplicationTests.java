package rnd.statemachine;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import rnd.statemachine.order.processor.OrderProcessor;
import rnd.statemachine.order.processor.PaymentProcessor;

@SpringBootTest(classes= {PaymentProcessor.class,OrderProcessor.class})
public class StateMachineApplicationTests {

	@Test
	public void contextLoads() {
	}

}
