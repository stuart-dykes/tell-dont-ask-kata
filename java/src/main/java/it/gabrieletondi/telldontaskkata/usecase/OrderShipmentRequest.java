package it.gabrieletondi.telldontaskkata.usecase;

public class OrderShipmentRequest {
    private int orderId;

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }
}
