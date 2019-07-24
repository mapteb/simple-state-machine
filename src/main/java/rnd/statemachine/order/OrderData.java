package rnd.statemachine.order;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rnd.statemachine.ProcessData;
import rnd.statemachine.ProcessEvent;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter
@Builder
public class OrderData implements ProcessData {
	private double payment;
	private ProcessEvent event;
	private UUID orderId;
	@Override
	public ProcessEvent getEvent() {
		return this.event;
	}
}
