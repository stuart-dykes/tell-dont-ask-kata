package it.gabrieletondi.telldontaskkata.usecase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderId;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderApprovalUseCase {
	private final OrderRepository orderRepository;

	public OrderApprovalUseCase( OrderRepository orderRepository ) {
		this.orderRepository = orderRepository;
	}

	public void run( OrderApprovalRequest request ) {
		if ( request.isApproved() ) {
			approveOrder( request.getOrderId() );
		} else {
			rejectOrder( request.getOrderId() );
		}
	}

	private void rejectOrder( final int orderId ) {
		final Order order = orderRepository.getById( OrderId.of( orderId ) )
				.orElseThrow( UnknownOrderException::new );
		if ( order.canReject() ) {
			order.rejected();
			orderRepository.save( order );
		} else {
			throw new IllegalOperationException();
		}
	}

	private void approveOrder( final int orderId ) {
		final Order order = orderRepository.getById( OrderId.of( orderId ) )
				.orElseThrow( UnknownOrderException::new );
		if ( order.canApprove() ) {
			order.approved();
			orderRepository.save( order );
		} else {
			throw new IllegalOperationException();
		}
	}

}
