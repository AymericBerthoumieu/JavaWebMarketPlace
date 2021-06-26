package no.oslomet.serverrestproject.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String roles;
    @ElementCollection
    private List<Long> starred;

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", starred=" + starred +
                ", products=" + products +
                ", orders=" + orders +
                ", shippings=" + shippings +
                '}';
    }

    public List<Long> getStarred() {
        return starred;
    }

    public void setStarred(List<Long> starred) {
        this.starred = starred;
    }

    @ElementCollection
    private List<Long> products; //list of id of products liked for a normal user, products to sell for a merchant

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List<Shipping> shippings;


    public User() {
    }

    public User(String firstName, String lastName, String email, String password, String roles, List<Long> products, List<Order> orders, List<Shipping> shippings) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.products = products;
        this.orders = orders;
        this.shippings = shippings;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Shipping> getShippings() {
        return shippings;
    }

    public void setShippings(List<Shipping> shippings) {
        this.shippings = shippings;
    }

    public List<Long> getProducts() {
        return products;
    }

    public void setProducts(List<Long> products) {
        this.products = products;
    }
}
