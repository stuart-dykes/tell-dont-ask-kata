package it.gabrieletondi.telldontaskkata.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.APPROVED;
import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.REJECTED;

import org.junit.jupiter.api.Test;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;

class OrderApprovalUseCaseTest {
	private final TestOrderRepository orderRepository = new TestOrderRepository();
	private final OrderApprovalUseCase useCase = new OrderApprovalUseCase( orderRepository );

	@Test
	void approvedExistingOrder() {
		Order initialOrder = Order.loadCreated( 1 );
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, true );

		useCase.run( request );

		final Order savedOrder = orderRepository.getSavedOrder();
		assertThat( savedOrder.getStatus() ).isEqualTo( APPROVED );
	}

	@Test
	void rejectedExistingOrder() {
		Order initialOrder = Order.loadCreated( 1 );
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, false );

		useCase.run( request );

		final Order savedOrder = orderRepository.getSavedOrder();
		assertThat( savedOrder.getStatus() ).isEqualTo( REJECTED );
	}

	@Test
	void cannotApproveRejectedOrder() {
		Order initialOrder = Order.loadRejected( 1 );
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, true );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				IllegalOperationException.class );
		assertThat( orderRepository.getSavedOrder() ).isNull();
	}

	@Test
	void cannotRejectApprovedOrder() {
		Order initialOrder = Order.loadApproved( 1 );
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, false );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				IllegalOperationException.class );
		assertThat( orderRepository.getSavedOrder() ).isNull();
	}

	@Test
	void shippedOrdersCannotBeApproved() {
		Order initialOrder = Order.loadShipped( 1 );
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, true );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				IllegalOperationException.class );
		assertThat( orderRepository.getSavedOrder() ).isNull();
	}

	@Test
	void shippedOrdersCannotBeRejected() {
		Order initialOrder = Order.loadShipped( 1 );
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, false );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				IllegalOperationException.class );
		assertThat( orderRepository.getSavedOrder() ).isNull();
	}
}
