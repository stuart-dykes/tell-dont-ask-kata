package it.gabrieletondi.telldontaskkata.domain;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;

public class OrderItem {

	private final Product product;
	private final int quantity;
	private final BigDecimal taxedAmount;
	private final BigDecimal tax;

	public OrderItem( final Product product, final int quantity ) {
		this.product = product;
		this.quantity = quantity;
		this.tax = product.getUnitaryTax().multiply( BigDecimal.valueOf( quantity ) );
		this.taxedAmount = product.getUnitaryTaxedAmount()
				.multiply( BigDecimal.valueOf( quantity ) )
				.setScale( 2, HALF_UP );
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getTaxedAmount() {
		return taxedAmount;
	}

	public BigDecimal getTax() {
		return tax;
	}
}
