package it.gabrieletondi.telldontaskkata.domain;

import static java.math.BigDecimal.ZERO;

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
	private static final String EUR = "EUR";

	private int id;
	private OrderStatus status = CREATED;
	private final List<OrderItem> items;

	public Order( final List<OrderItem> items ) {
		this.items = items;
	}

	public Order( final int id, final OrderStatus status ) {
		this.id = id;
		this.status = status;
		this.items = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public String getCurrency() {
		return EUR;
	}

	public BigDecimal getTotal() {
		return items.stream()
				.map( OrderItem::getTaxedAmount )
				.reduce( BigDecimal::add )
				.orElse( ZERO );
	}

	public BigDecimal getTax() {
		return items.stream().map( OrderItem::getTax ).reduce( BigDecimal::add ).orElse( ZERO );
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
