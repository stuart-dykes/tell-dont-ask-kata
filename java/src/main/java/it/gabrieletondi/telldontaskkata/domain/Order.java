package it.gabrieletondi.telldontaskkata.domain;

import static java.math.BigDecimal.ZERO;

import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.APPROVED;
import static it.gabrieletondi.telldontaskkata.domain.OrderStatus.CREATED;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

	public boolean canApprove() {
		return this.status == CREATED;
	}

	public void approved() {
		if ( !canApprove() ) {
			throw new IllegalStateException();
		}
		this.status = OrderStatus.APPROVED;
	}

	public boolean canReject() {
		return this.status == CREATED;
	}

	public void rejected() {
		if ( !canReject() ) {
			throw new IllegalStateException();
		}
		this.status = OrderStatus.REJECTED;
	}

	public boolean canShip() {
		return this.status == APPROVED;
	}

	public void shipped() {
		if ( !canShip() ) {
			throw new IllegalStateException();
		}
		this.status = OrderStatus.SHIPPED;
	}
}
