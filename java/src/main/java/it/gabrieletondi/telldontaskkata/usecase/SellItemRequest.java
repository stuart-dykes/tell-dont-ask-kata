package it.gabrieletondi.telldontaskkata.usecase;

public record SellItemRequest(String productName, int quantity) {

	public int getQuantity() {
		return quantity;
	}

	public String getProductName() {
		return productName;
	}
}
