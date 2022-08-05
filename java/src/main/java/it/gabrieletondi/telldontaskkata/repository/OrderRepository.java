package it.gabrieletondi.telldontaskkata.repository;

import java.util.Optional;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderId;

public interface OrderRepository {
    void save(Order order);

    Optional<Order> getById( OrderId orderId );
}
