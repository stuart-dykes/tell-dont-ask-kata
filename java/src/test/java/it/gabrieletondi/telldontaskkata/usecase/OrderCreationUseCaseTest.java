package it.gabrieletondi.telldontaskkata.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.gabrieletondi.telldontaskkata.domain.Category;
import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
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

		final Order order = orderRepository.getSavedOrder();
		assertThat( order.getStatus() ).isEqualTo( OrderStatus.CREATED );
		assertThat( order.getTotal() ).isEqualTo( new BigDecimal( "23.20" ) );
		assertThat( order.getTax() ).isEqualTo( new BigDecimal( "2.13" ) );
		assertThat( order.getCurrency() ).isEqualTo( "EUR" );
		assertThat( order.getItems() ).hasSize( 2 );
		final OrderItem item1 = order.getItems().get( 0 );
		assertThat( item1.getProduct().getName() ).isEqualTo( "salad" );
		assertThat( item1.getProduct().getPrice() ).isEqualTo( new BigDecimal( "3.56" ) );
		assertThat( item1.getQuantity() ).isEqualTo( 2 );
		assertThat( item1.getTaxedAmount() ).isEqualTo( new BigDecimal( "7.84" ) );
		assertThat( item1.getTax() ).isEqualTo( new BigDecimal( "0.72" ) );
		final OrderItem item2 = order.getItems().get( 1 );
		assertThat( item2.getProduct().getName() ).isEqualTo( "tomato" );
		assertThat( item2.getProduct().getPrice() ).isEqualTo( new BigDecimal( "4.65" ) );
		assertThat( item2.getQuantity() ).isEqualTo( 3 );
		assertThat( item2.getTaxedAmount() ).isEqualTo( new BigDecimal( "15.36" ) );
		assertThat( item2.getTax() ).isEqualTo( new BigDecimal( "1.41" ) );
	}

	@Test
	void unknownProduct() {
		SellItemsRequest request = new SellItemsRequest(
				List.of( new SellItemRequest( "unknown product", 1 ) ) );

		assertThatThrownBy( () -> useCase.run( request ) ).isExactlyInstanceOf(
				UnknownProductException.class );
	}
}
