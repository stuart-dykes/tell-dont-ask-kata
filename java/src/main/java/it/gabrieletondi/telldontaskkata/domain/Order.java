package it.gabrieletondi.telldontaskkata.domain;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
	private static final String EUR = "EUR";

	private int id;
	private OrderState state;
	private final List<OrderItem> items;

	private Order( final int id, final OrderState state, final List<OrderItem> items ) {
		this.id = id;
		this.state = state;
		this.items = items;
	}

	public int getId() {
		return id;
	}

	public OrderStatus getStatus() {
		return state.getStatus();
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

	void changeState( final OrderState state ) {
		this.state = state;
	}

	public boolean canApprove() {
		return this.state.canApprove();
	}

	public void approved() {
		this.state.approved( this );
	}

	public boolean canReject() {
		return this.state.canReject();
	}

	public void rejected() {
		this.state.rejected( this );
	}

	public boolean canShip() {
		return this.state.canShip();
	}

	public void shipped() {
		this.state.shipped( this );
	}

	public static Order create( final List<OrderItem> items ) {
		return new Order( -1, new CreatedOrderState(), items );
	}

	public static Order loadCreated( final int id ) {
		return new Order( id, new CreatedOrderState(), new ArrayList<>() );
	}

	public static Order loadApproved( final int id ) {
		return new Order( id, new ApprovedOrderState(), new ArrayList<>() );
	}

	public static Order loadRejected( final int id ) {
		return new Order( id, new RejectedOrderState(), new ArrayList<>() );
	}

	public static Order loadShipped( final int id ) {
		return new Order( id, new ShippedOrderState(), new ArrayList<>() );
	}
}
