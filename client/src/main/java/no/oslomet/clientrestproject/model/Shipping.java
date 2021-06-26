package no.oslomet.clientrestproject.model;

import lombok.Data;

import java.util.List;

@Data
public class Shipping {
    private long shipping_id;
    private String firstName;
    private String lastName;
    private String address;
    private User user;
    private List<Order> orders;


    public Shipping() {
    }


}
