package POJO;

public class OrderDetails {
    private String paymentInformation;
    private String shippingInformation;
    private float itemTotalPrice;
    private float tax;
    private float totalPrice;

    public OrderDetails(String paymentInformation, String shippingInformation, float totalPrice, float itemTotalPrice, float tax) {
        this.paymentInformation = paymentInformation;
        this.shippingInformation = shippingInformation;
        this.totalPrice = totalPrice;
        this.itemTotalPrice = itemTotalPrice;
        this.tax = tax;
    }

    public String getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(String paymentInformation) {
        this.paymentInformation = paymentInformation;
    }

    public String getShippingInformation() {
        return shippingInformation;
    }

    public void setShippingInformation(String shippingInformation) {
        this.shippingInformation = shippingInformation;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public float getItemTotalPrice() {
        return itemTotalPrice;
    }

    public void setItemTotalPrice(float itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "paymentInformation='" + paymentInformation + '\'' +
                ", shippingInformation='" + shippingInformation + '\'' +
                ", totalPrice=" + totalPrice +
                ", itemTotalPrice=" + itemTotalPrice +
                ", tax=" + tax +
                '}';
    }
}
