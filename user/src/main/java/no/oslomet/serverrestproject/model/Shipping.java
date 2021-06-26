package no.oslomet.serverrestproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Entity
@Data
public class Shipping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long shipping_id;
    private String firstName;
    private String lastName;
    private String address;

    public Shipping(String firstName, String lastName, String address, User user, List<Order> orders) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.user = user;
        this.orders = orders;
    }

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy="shipping", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;

    @Override
    public String toString() {
        return "Shipping{" +
                "shipping_id=" + shipping_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", user=" + user +
                ", orders=" + orders +
                '}';
    }

    public long getShipping_id() {
        return shipping_id;
    }

    public void setShipping_id(long shipping_id) {
        this.shipping_id = shipping_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Shipping() {
    }
}
