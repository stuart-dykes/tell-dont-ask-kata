package it.gabrieletondi.telldontaskkata.domain;

import static java.math.BigDecimal.valueOf;
import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;

public record Product(String name, BigDecimal price, Category category) {

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    public BigDecimal getUnitaryTax() {
        return getPrice().divide( valueOf( 100 ) )
                .multiply( getCategory().getTaxPercentage() )
                .setScale( 2, HALF_UP );
    }

    public BigDecimal getUnitaryTaxedAmount() {
        return getPrice().add( getUnitaryTax() ).setScale( 2, HALF_UP );
    }
}
