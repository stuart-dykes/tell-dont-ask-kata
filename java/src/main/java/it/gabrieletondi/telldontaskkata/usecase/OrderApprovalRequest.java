package it.gabrieletondi.telldontaskkata.usecase;

public record OrderApprovalRequest(int orderId, boolean approved) {

	public int getOrderId() {
		return orderId;
	}

	public boolean isApproved() {
		return approved;
	}
}
