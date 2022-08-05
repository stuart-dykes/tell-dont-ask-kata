package it.gabrieletondi.telldontaskkata.domain;

abstract class OrderState {

	private final OrderStatus status;

	protected OrderState( final OrderStatus status ) {
		this.status = status;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public boolean canApprove() {
		return false;
	}

	public boolean canReject() {
		return false;
	}

	public boolean canShip() {
		return false;
	}

	public void approved( final Order order ) {
		throw new IllegalStateException();
	}

	public void rejected( final Order order ) {
		throw new IllegalStateException();
	}

	public void shipped( final Order order ) {
		throw new IllegalStateException();
	}
}
