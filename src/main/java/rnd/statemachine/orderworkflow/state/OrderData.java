package rnd.statemachine.orderworkflow.state;

import java.util.UUID;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rnd.statemachine.core.WorkflowData;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@Builder
public class OrderData implements WorkflowData {
	// For this demo the cart details are not included. 
	// This field is just for demonstration and is not used in the state machine logic.
	private Long userId;
	@Builder.Default
	private String cartData = "";

	@Nullable
	@Builder.Default
	private Double payment = 0.0d;

	@Nullable
	@Builder.Default
	private UUID orderId = null;
}
