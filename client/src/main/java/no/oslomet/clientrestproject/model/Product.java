package no.oslomet.clientrestproject.model;

import lombok.Data;

import java.util.List;

@Data
public class Product {
    private long id;
    private String Name;
    private String Description;
    private String quality;
    private long Quantity;
    private long likes=0;
    private List<Long> stars;
    private String picture;
    private long merchant; // id of the merchant selling this product

    public Product(String name, String description, String Quality, long quantity, long likes, long merchant) {
        Name = name;
        Description = description;
        this.quality = Quality;
        Quantity = quantity;
        this.likes = likes;
        this.merchant = merchant;
    }

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", Quantity=" + Quantity +
                ", likes=" + likes +
                ", stars=" + stars +
                ", merchant=" + merchant +
                '}';
    }

    public String toStringSearch() {
        return "id=" + id +
                ", Name='" + Name + '\'' +
                ", Description='" + Description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public long getQuantity() {
        return Quantity;
    }

    public void setQuantity(long quantity) {
        Quantity = quantity;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public List<Long> getStars() {
        return stars;
    }

    public void setStars(List<Long> stars) {
        this.stars = stars;
    }

    public long getMerchant() {
        return merchant;
    }

    public void setMerchant(long merchant) {
        this.merchant = merchant;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}
