package it.gabrieletondi.telldontaskkata.usecase;

import java.math.BigDecimal;
import java.util.ArrayList;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderItem;
import it.gabrieletondi.telldontaskkata.domain.OrderStatus;
import it.gabrieletondi.telldontaskkata.domain.Product;
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
		Order order = new Order();
		order.setStatus( OrderStatus.CREATED );
		order.setItems( new ArrayList<>() );
		order.setCurrency( "EUR" );
		order.setTotal( new BigDecimal( "0.00" ) );
		order.setTax( new BigDecimal( "0.00" ) );

		for ( SellItemRequest itemRequest : request.getRequests() ) {
			Product product = productCatalog.getByName( itemRequest.getProductName() );

			if ( product == null ) {
				throw new UnknownProductException();
			} else {
				final OrderItem orderItem = new OrderItem( product, itemRequest.getQuantity() );

				order.addItem( orderItem );
			}
		}

		orderRepository.save( order );
	}

}
