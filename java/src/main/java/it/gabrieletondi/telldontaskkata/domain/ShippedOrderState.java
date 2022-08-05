package it.gabrieletondi.telldontaskkata.domain;

class ShippedOrderState extends OrderState {
	public ShippedOrderState() {
		super( OrderStatus.SHIPPED );
	}
}
