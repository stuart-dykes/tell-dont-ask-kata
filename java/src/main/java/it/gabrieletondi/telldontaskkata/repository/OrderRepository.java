package it.gabrieletondi.telldontaskkata.repository;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderId;

public interface OrderRepository {
    void save(Order order);

    Order getById( OrderId orderId );
}
