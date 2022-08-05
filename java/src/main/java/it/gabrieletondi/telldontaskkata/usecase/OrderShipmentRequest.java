package it.gabrieletondi.telldontaskkata.usecase;

public record OrderShipmentRequest(int orderId) {

    public int getOrderId() {
        return orderId;
    }
}
