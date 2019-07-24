package rnd.statemachine;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import rnd.statemachine.order.PaymentProcessor;
import rnd.statemachine.order.OrderProcessor;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {PaymentProcessor.class,OrderProcessor.class})
public class StateMachineApplicationTests {

	@Test
	public void contextLoads() {
	}

}
