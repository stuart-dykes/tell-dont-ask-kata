package it.gabrieletondi.telldontaskkata.domain;

class ApprovedOrderState extends OrderState {

	protected ApprovedOrderState() {
		super( OrderStatus.APPROVED );
	}

	@Override
	public boolean canShip() {
		return true;
	}

	@Override
	public void shipped( final Order order ) {
		order.changeState( new ShippedOrderState() );
	}
}
