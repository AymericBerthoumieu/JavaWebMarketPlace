package no.oslomet.serverrestproject.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String Name;
    private String Description;
    private String quality;
    private long Quantity;
    private long likes;
    @ElementCollection
    private List<Long> stars;
    private String picture;
    private long merchant; // id of the merchant selling this product
}
