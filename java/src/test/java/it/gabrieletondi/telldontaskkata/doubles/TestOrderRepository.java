package it.gabrieletondi.telldontaskkata.doubles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.gabrieletondi.telldontaskkata.domain.Order;
import it.gabrieletondi.telldontaskkata.domain.OrderId;
import it.gabrieletondi.telldontaskkata.repository.OrderRepository;

public class TestOrderRepository implements OrderRepository {
    private Order insertedOrder;
    private final List<Order> orders = new ArrayList<>();

    public Order getSavedOrder() {
        return insertedOrder;
    }

    @Override
    public void save(Order order) {
        this.insertedOrder = order;
    }

    @Override
    public Optional<Order> getById( OrderId orderId ) {
        return orders.stream().filter( o -> o.getId().equals( orderId ) ).findFirst();
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }
}
