package it.gabrieletondi.telldontaskkata.domain;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;

public class OrderItem {
    private final Product product;
    private final int quantity;
    private BigDecimal taxedAmount;
    private BigDecimal tax;

    public OrderItem( final Product product, final int quantity ) {
        this.product = product;
        this.quantity = quantity;
        updateTax( product, quantity );
        updateTaxedAmount( product, quantity );
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

    public void setTaxedAmount(BigDecimal taxedAmount) {
        this.taxedAmount = taxedAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax( BigDecimal tax ) {
        this.tax = tax;
    }

    public void updateTaxedAmount( final Product product, final int quantity ) {
        final BigDecimal unitaryTaxedAmount = product.getUnitaryTaxedAmount();
        setTaxedAmount( unitaryTaxedAmount.multiply( BigDecimal.valueOf( quantity ) )
                .setScale( 2, HALF_UP ) );
    }

    public void updateTax( final Product product, final int quantity ) {
        final BigDecimal unitaryTax = product.getUnitaryTax();
        setTax( unitaryTax.multiply( BigDecimal.valueOf( quantity ) ) );
    }
}
