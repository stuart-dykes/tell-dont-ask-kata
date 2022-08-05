package it.gabrieletondi.telldontaskkata.usecase;

import static java.math.RoundingMode.HALF_UP;

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

    public OrderCreationUseCase(OrderRepository orderRepository, ProductCatalog productCatalog) {
        this.orderRepository = orderRepository;
        this.productCatalog = productCatalog;
    }

    public void run(SellItemsRequest request) {
        Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setItems(new ArrayList<>());
        order.setCurrency("EUR");
        order.setTotal(new BigDecimal("0.00"));
        order.setTax(new BigDecimal("0.00"));

        for (SellItemRequest itemRequest : request.getRequests()) {
            Product product = productCatalog.getByName(itemRequest.getProductName());

            if (product == null) {
                throw new UnknownProductException();
            }
            else {
                final BigDecimal unitaryTax = product.getUnitaryTax();
                final BigDecimal unitaryTaxedAmount = product.getUnitaryTaxedAmount();
                final BigDecimal taxedAmount = unitaryTaxedAmount.multiply(
                        BigDecimal.valueOf( itemRequest.getQuantity() ) ).setScale( 2, HALF_UP );
                final BigDecimal taxAmount = unitaryTax.multiply(
                        BigDecimal.valueOf( itemRequest.getQuantity() ) );

                final OrderItem orderItem = new OrderItem();
                orderItem.setProduct( product );
                orderItem.setQuantity( itemRequest.getQuantity() );
                orderItem.setTax( taxAmount );
                orderItem.setTaxedAmount( taxedAmount );

                order.addItem( orderItem );
                order.setTotal( order.getTotal().add( orderItem.getTaxedAmount() ) );
                order.setTax( order.getTax().add( orderItem.getTax() ) );
            }
        }

        orderRepository.save(order);
    }

}
