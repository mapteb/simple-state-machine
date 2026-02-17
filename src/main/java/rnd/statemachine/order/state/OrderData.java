package rnd.statemachine.order.state;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rnd.statemachine.core.ProcessData;
import rnd.statemachine.core.ProcessEvent;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@Builder
public class OrderData implements ProcessData {
	// For this demo the cart details are not included. 
	// This field is just for demonstration and is not used in the state machine logic.
	private String cartData;
	private double payment;
	@JsonIgnore
	private ProcessEvent event;
	private UUID orderId;
	@Override
	public ProcessEvent getEvent() {
		return this.event;
	}
}
