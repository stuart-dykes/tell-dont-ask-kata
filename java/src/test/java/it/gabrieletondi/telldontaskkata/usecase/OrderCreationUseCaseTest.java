package it.gabrieletondi.telldontaskkata.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.gabrieletondi.telldontaskkata.domain.Category;
import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
import it.gabrieletondi.telldontaskkata.doubles.InMemoryProductCatalog;
import it.gabrieletondi.telldontaskkata.doubles.TestOrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

class OrderCreationUseCaseTest {
	private final TestOrderRepository orderRepository = new TestOrderRepository();
	private final Category food = new Category( "food", new BigDecimal( "10" ) );

	private final ProductCatalog productCatalog = new InMemoryProductCatalog(
			Arrays.asList( new Product( "salad", new BigDecimal( "3.56" ), food ),
					new Product( "tomato", new BigDecimal( "4.65" ), food ) ) );
	private final OrderCreationUseCase useCase = new OrderCreationUseCase( orderRepository,
			productCatalog );

	@Test
	void sellMultipleItems() {
		SellItemRequest saladRequest = new SellItemRequest( "salad", 2 );
		SellItemRequest tomatoRequest = new SellItemRequest( "tomato", 3 );

		final SellItemsRequest request = new SellItemsRequest(
				List.of( saladRequest, tomatoRequest ) );

		useCase.run( request );

		final Order insertedOrder = orderRepository.getSavedOrder();
		assertThat( insertedOrder.getStatus() ).isEqualTo( OrderStatus.CREATED );
		assertThat( insertedOrder.getTotal() ).isEqualTo( new BigDecimal( "23.20" ) );
		assertThat( insertedOrder.getTax() ).isEqualTo( new BigDecimal( "2.13" ) );
		assertThat( insertedOrder.getCurrency() ).isEqualTo( "EUR" );
		assertThat( insertedOrder.getItems() ).hasSize( 2 );
		assertThat( insertedOrder.getItems().get( 0 ).getProduct().getName() ).isEqualTo( "salad" );
		assertThat( insertedOrder.getItems().get( 0 ).getProduct().getPrice() ).isEqualTo(
				new BigDecimal( "3.56" ) );
		assertThat( insertedOrder.getItems().get( 0 ).getQuantity() ).isEqualTo( 2 );
		assertThat( insertedOrder.getItems().get( 0 ).getTaxedAmount() ).isEqualTo(
				new BigDecimal( "7.84" ) );
		assertThat( insertedOrder.getItems().get( 0 ).getTax() ).isEqualTo(
				new BigDecimal( "0.72" ) );
		assertThat( insertedOrder.getItems().get( 1 ).getProduct().getName() ).isEqualTo(
				"tomato" );
		assertThat( insertedOrder.getItems().get( 1 ).getProduct().getPrice() ).isEqualTo(
				new BigDecimal( "4.65" ) );
		assertThat( insertedOrder.getItems().get( 1 ).getQuantity() ).isEqualTo( 3 );
		assertThat( insertedOrder.getItems().get( 1 ).getTaxedAmount() ).isEqualTo(
				new BigDecimal( "15.36" ) );
		assertThat( insertedOrder.getItems().get( 1 ).getTax() ).isEqualTo(
				new BigDecimal( "1.41" ) );
	}

	@Test
	void unknownProduct() {
		SellItemsRequest request = new SellItemsRequest(
				List.of( new SellItemRequest( "unknown product", 1 ) ) );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				UnknownProductException.class );
	}
}
