package no.oslomet.clientrestproject.model;

import javafx.util.Pair;
import lombok.Data;

import java.util.List;

@Data
public class Order {
    private long order_id;
    private String date;
    private List<Long> products; //list of (id, quantity) of ordered products
    private Shipping shipping;
    private User user;

    public Order() {
    }

    public Order(List<Long> products, User user) {
        this.products = products;
        this.user = user;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Long> getProducts() {
        return products;
    }

    public void setProducts(List<Long> products) {
        this.products = products;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
