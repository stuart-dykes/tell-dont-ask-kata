package it.gabrieletondi.telldontaskkata.domain;

import java.math.BigDecimal;

public record Category(String name, BigDecimal taxPercentage) {

	public String getName() {
		return name;
	}

	public BigDecimal getTaxPercentage() {
		return taxPercentage;
	}
}
