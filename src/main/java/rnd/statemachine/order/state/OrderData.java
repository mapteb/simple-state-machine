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
	@Builder.Default
	private String cartData = "";
	@Builder.Default
	private double payment = 0.0d;
	@JsonIgnore
	private ProcessEvent event;
	@Builder.Default
	private UUID orderId = null;
	@Override
	public ProcessEvent getEvent() {
		return this.event;
	}
}
