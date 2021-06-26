package no.oslomet.serverrestproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.util.Pair;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long order_id;
    private String date;
    @ElementCollection
    private List<Long> productsOrdered; //list of (id,quantity) of ordered products

    @ManyToOne
    @JoinColumn(name="shipping_id")
    private Shipping shipping;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    public Order() {}

    public Order(String date, User user) {
        this.date = date;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", date='" + date + '\'' +
                ", shipping=" + shipping +
                ", products=" + productsOrdered +
                ", user=" + user +
                '}';
    }

    public Order(String date, List<Long> productsOrdered, Shipping shipping, User user) {
        this.date = date;
        this.productsOrdered = productsOrdered;
        this.shipping = shipping;
        this.user = user;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public void setProducts(List<Long> products) {
        this.productsOrdered = products;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getOrder_id() {
        return order_id;
    }

    public String getDate() {
        return date;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public List<Long> getProducts() {
        return productsOrdered;
    }

    public User getUser() {
        return user;
    }
}
