package rnd.statemachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A simple state machine implementation for Spring Boot projects
 * Includes a framework and a sample application of the framework
 * to an online order process
 *
 * @author Nalla Senthilnathan https://github.com/mapteb/simple-state-machine
 * @author Guilherme Biff Zarelli https://github.com/gbzarelli/simple-state-machine
 */
@SpringBootApplication
public class StateMachineApplication {
	public static void main(String[] args) {
		SpringApplication.run(StateMachineApplication.class, args);
	}
}
