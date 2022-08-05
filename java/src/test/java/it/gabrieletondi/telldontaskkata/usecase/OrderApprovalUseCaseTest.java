package it.gabrieletondi.telldontaskkata.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;

class OrderApprovalUseCaseTest {
	private final TestOrderRepository orderRepository = new TestOrderRepository();
	private final OrderApprovalUseCase useCase = new OrderApprovalUseCase( orderRepository );

	@Test
	void approvedExistingOrder() {
		Order initialOrder = new Order();
		initialOrder.setId( 1 );
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, true );

		useCase.run( request );

		final Order savedOrder = orderRepository.getSavedOrder();
		assertThat( savedOrder.getStatus() ).isEqualTo( OrderStatus.APPROVED );
	}

	@Test
	void rejectedExistingOrder() {
		Order initialOrder = new Order();
		initialOrder.setId( 1 );
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, false );

		useCase.run( request );

		final Order savedOrder = orderRepository.getSavedOrder();
		assertThat( savedOrder.getStatus() ).isEqualTo( OrderStatus.REJECTED );
	}

	@Test
	void cannotApproveRejectedOrder() {
		Order initialOrder = new Order();
		initialOrder.setId( 1 );
		initialOrder.rejected();
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, true );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				RejectedOrderCannotBeApprovedException.class );
		assertThat( orderRepository.getSavedOrder() ).isNull();
	}

	@Test
	void cannotRejectApprovedOrder() {
		Order initialOrder = new Order();
		initialOrder.setId( 1 );
		initialOrder.approved();
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, false );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				ApprovedOrderCannotBeRejectedException.class );
		assertThat( orderRepository.getSavedOrder() ).isNull();
	}

	@Test
	void shippedOrdersCannotBeApproved() {
		Order initialOrder = new Order();
		initialOrder.setId( 1 );
		initialOrder.approved();
		initialOrder.shipped();
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, true );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				ShippedOrdersCannotBeChangedException.class );
		assertThat( orderRepository.getSavedOrder() ).isNull();
	}

	@Test
	void shippedOrdersCannotBeRejected() {
		Order initialOrder = new Order();
		initialOrder.setId( 1 );
		initialOrder.approved();
		initialOrder.shipped();
		orderRepository.addOrder( initialOrder );

		OrderApprovalRequest request = new OrderApprovalRequest( 1, false );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				ShippedOrdersCannotBeChangedException.class );
		assertThat( orderRepository.getSavedOrder() ).isNull();
	}
}
