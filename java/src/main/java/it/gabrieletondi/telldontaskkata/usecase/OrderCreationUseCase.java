package it.gabrieletondi.telldontaskkata.usecase;

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
		Order order = Order.create();

		request.getRequests()
				.stream()
				.map( itemRequest -> productCatalog.getByName( itemRequest.getProductName() )
						.map( product -> new OrderItem( product, itemRequest.getQuantity() ) )
						.orElseThrow( UnknownProductException::new ) )
				.forEach( order::addItem );

		orderRepository.save( order );
	}

}
