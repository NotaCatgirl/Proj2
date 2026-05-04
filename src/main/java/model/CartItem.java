package model;

public class CartItem {
    private final int cartItemId;
    private final int productId;
    private final String productName;
    private final double productPrice;
    private int quantity;

    public CartItem(int cartItemId, int productId, String productName,
                    double productPrice, int quantity) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getLineTotal() {
        return productPrice * quantity;
    }
}