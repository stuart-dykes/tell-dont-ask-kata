package it.gabrieletondi.telldontaskkata.domain;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;

public record OrderItem(Product product, int quantity) {

	public OrderItem {
		if ( quantity < 1 ) {
			throw new IllegalArgumentException();
		}
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getTaxedAmount() {
		return product.getUnitaryTaxedAmount()
				.multiply( BigDecimal.valueOf( quantity ) )
				.setScale( 2, HALF_UP );
	}

	public BigDecimal getTax() {
		return product.getUnitaryTax().multiply( BigDecimal.valueOf( quantity ) );
	}
}
