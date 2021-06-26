package no.oslomet.clientrestproject.model;

import lombok.Data;
import java.util.List;

@Data
public class User {
    private long user_id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String roles;
    private List<Long> starred; // list of product's ids already starred
    private List<Long> products; //list of id of products liked for a normal user, products to sell for a merchant
    private List<Order> orders;
    private List<Shipping> shippings;


    public User(String firstName, String lastName, String email, String password, String roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(){}

    public User(long user_id, String firstName, String lastName, String email, String password, String roles, List<Long> starred, List<Long> products, List<Order> orders, List<Shipping> shippings) {
        this.user_id = user_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.starred = starred;
        this.products = products;
        this.orders = orders;
        this.shippings = shippings;
    }

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

    public List<Long> getProducts() {
        return products;
    }

    public void setProducts(List<Long> products) {
        this.products = products;
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

    public List<Long> getStarred() {
        return starred;
    }

    public void setStarred(List<Long> starred) {
        this.starred = starred;
    }

}
