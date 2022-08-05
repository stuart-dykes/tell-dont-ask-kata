package it.gabrieletondi.telldontaskkata.domain;

class CreatedOrderState extends OrderState {

	protected CreatedOrderState() {
		super( OrderStatus.CREATED );
	}

	@Override
	public boolean canApprove() {
		return true;
	}

	@Override
	public boolean canReject() {
		return true;
	}

	@Override
	public void approved( final Order order ) {
		order.changeState( new ApprovedOrderState() );
	}

	@Override
	public void rejected( final Order order ) {
		order.changeState( new RejectedOrderState() );
	}
}
