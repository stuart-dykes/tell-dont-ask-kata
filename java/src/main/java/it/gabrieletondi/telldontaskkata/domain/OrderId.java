package it.gabrieletondi.telldontaskkata.domain;

public record OrderId(int id) {

	public OrderId {
		if ( id < 1 ) {
			throw new IllegalArgumentException();
		}
	}

	public static OrderId of( int id ) {
		return new OrderId( id );
	}
}
