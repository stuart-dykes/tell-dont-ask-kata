package it.gabrieletondi.telldontaskkata.domain;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.CREATED;
import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.REJECTED;
import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.SHIPPED;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.gabrieletondi.telldontaskkata.usecase.ApprovedOrderCannotBeRejectedException;
import it.gabrieletondi.telldontaskkata.usecase.OrderCannotBeShippedException;
import it.gabrieletondi.telldontaskkata.usecase.OrderCannotBeShippedTwiceException;
import it.gabrieletondi.telldontaskkata.usecase.RejectedOrderCannotBeApprovedException;
import it.gabrieletondi.telldontaskkata.usecase.ShippedOrdersCannotBeChangedException;

public class Order {
    private final List<OrderItem> items = new ArrayList<>();
    private BigDecimal tax = new BigDecimal( "0.00" );
    private BigDecimal total = new BigDecimal( "0.00" );
    private OrderStatus status = CREATED;
    private int id;

    public BigDecimal getTotal() {
        return total;
    }

    public String getCurrency() {
        return "EUR";
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public static Order create() {
        return new Order();
    }

    public void addItem( final OrderItem item ) {
        items.add( item );
        this.total = getTotal().add( item.getTaxedAmount() );
        this.tax = getTax().add( item.getTax() );
    }

    public void approved() {
        if ( getStatus().equals( OrderStatus.SHIPPED ) ) {
            throw new ShippedOrdersCannotBeChangedException();
        }

        if ( getStatus().equals( OrderStatus.REJECTED ) ) {
            throw new RejectedOrderCannotBeApprovedException();
        }
        this.status = OrderStatus.APPROVED;
    }

    public void rejected() {
        if ( getStatus().equals( OrderStatus.SHIPPED ) ) {
            throw new ShippedOrdersCannotBeChangedException();
        }
        if ( getStatus().equals( OrderStatus.APPROVED ) ) {
            throw new ApprovedOrderCannotBeRejectedException();
        }
        this.status = OrderStatus.REJECTED;
    }

    public void shipped() {
        if ( getStatus().equals( CREATED ) || getStatus().equals( REJECTED ) ) {
            throw new OrderCannotBeShippedException();
        }

        if ( getStatus().equals( SHIPPED ) ) {
            throw new OrderCannotBeShippedTwiceException();
        }
        this.status = OrderStatus.SHIPPED;
    }
}
