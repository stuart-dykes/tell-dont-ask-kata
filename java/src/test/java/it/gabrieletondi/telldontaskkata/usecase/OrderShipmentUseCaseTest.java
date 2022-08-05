package it.gabrieletondi.telldontaskkata.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.SHIPPED;

import org.junit.jupiter.api.Test;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.doubles.TestShipmentService;

class OrderShipmentUseCaseTest {
	private final TestOrderRepository orderRepository = new TestOrderRepository();
	private final TestShipmentService shipmentService = new TestShipmentService();
	private final OrderShipmentUseCase useCase = new OrderShipmentUseCase( orderRepository,
			shipmentService );

	@Test
	void shipApprovedOrder() {
		Order initialOrder = Order.loadApproved( 1 );
		orderRepository.addOrder( initialOrder );

		OrderShipmentRequest request = new OrderShipmentRequest( 1 );

		useCase.run( request );

		assertThat( orderRepository.getSavedOrder().getStatus() ).isEqualTo( SHIPPED );
		assertThat( shipmentService.getShippedOrder() ).isEqualTo( initialOrder );
	}

	@Test
	void createdOrdersCannotBeShipped() {
		Order initialOrder = Order.loadCreated( 1 );
		orderRepository.addOrder( initialOrder );

		OrderShipmentRequest request = new OrderShipmentRequest( 1 );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				IllegalOperationException.class );

		assertThat( orderRepository.getSavedOrder() ).isNull();
		assertThat( shipmentService.getShippedOrder() ).isNull();
	}

	@Test
	void rejectedOrdersCannotBeShipped() {
		Order initialOrder = Order.loadRejected( 1 );
		orderRepository.addOrder( initialOrder );

		OrderShipmentRequest request = new OrderShipmentRequest( 1 );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				IllegalOperationException.class );
		assertThat( orderRepository.getSavedOrder() ).isNull();
		assertThat( shipmentService.getShippedOrder() ).isNull();
	}

	@Test
	void shippedOrdersCannotBeShippedAgain() {
		Order initialOrder = Order.loadShipped( 1 );
		orderRepository.addOrder( initialOrder );

		OrderShipmentRequest request = new OrderShipmentRequest( 1 );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				IllegalOperationException.class );

		assertThat( orderRepository.getSavedOrder() ).isNull();
		assertThat( shipmentService.getShippedOrder() ).isNull();
	}
}
