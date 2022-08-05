package it.gabrieletondi.telldontaskkata.usecase;

import java.util.List;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;
import it.gabrieletondi.telldontaskkata.repository.ProductCatalog;

public class OrderCreationUseCase {
	private final OrderRepository orderRepository;
	private final ProductCatalog productCatalog;

	public OrderCreationUseCase( OrderRepository orderRepository, ProductCatalog productCatalog ) {
		this.orderRepository = orderRepository;
		this.productCatalog = productCatalog;
	}

	public void run( SellItemsRequest request ) {

		final List<OrderItem> items = request.getRequests()
				.stream()
				.map( itemRequest -> productCatalog.getByName( itemRequest.getProductName() )
						.map( product -> new OrderItem( product, itemRequest.getQuantity() ) )
						.orElseThrow( UnknownProductException::new ) )
				.toList();

		orderRepository.save( new Order( items ) );
	}

}
