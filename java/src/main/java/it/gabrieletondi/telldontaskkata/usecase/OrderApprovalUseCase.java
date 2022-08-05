package it.gabrieletondi.telldontaskkata.usecase;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class OrderApprovalUseCase {
    private final OrderRepository orderRepository;

    public OrderApprovalUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void run(OrderApprovalRequest request) {
        final Order order = orderRepository.getById( request.getOrderId() );

        if ( request.isApproved() ) {
            order.setApproved();
        } else {
            order.setRejected();
        }

        orderRepository.save( order );
    }

}
