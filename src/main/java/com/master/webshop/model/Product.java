package com.master.webshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private double price;

    private String unit;

    private String imageURL;

    @Column(name = "quantity", nullable = false)
    @Min(value = 0, message = "*Quantity has to be non negative number")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category category;

    @OneToMany(mappedBy="selected_product", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonIgnore
    private List<Association> productsList;

    @OneToMany(mappedBy="associated_product", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JsonIgnore
    private List<Association> associatedProductsList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Association> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Association> productsList) {
        this.productsList = productsList;
    }

    public List<Association> getAssociatedProductsList() {
        return associatedProductsList;
    }

    public void setAssociatedProductsList(List<Association> associatedProductsList) {
        this.associatedProductsList = associatedProductsList;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", quantity=" + quantity +
                ", category=" + category +
                ", productsList=" + productsList +
                ", associatedProductsList=" + associatedProductsList +
                '}';
    }
}
