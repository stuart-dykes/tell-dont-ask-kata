package it.gabrieletondi.telldontaskkata.domain;

class RejectedOrderState extends OrderState {
	public RejectedOrderState() {
		super( OrderStatus.REJECTED );
	}
}
