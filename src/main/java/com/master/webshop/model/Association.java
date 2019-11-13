package com.master.webshop.model;

import javax.persistence.*;

@Entity
public class Association {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "selected_product_id") // add to_
    private Product selected_product;

    @ManyToOne
    @JoinColumn(name = "associated_product_id") // add to_
    private Product associated_product;

    private int occurences;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getSelectedProduct() {
        return selected_product;
    }

    public void setSelectedProduct(Product selectedProduct) {
        this.selected_product= selectedProduct;
    }

    public Product getAssociatedProduct() {
        return associated_product;
    }

    public void setAssociatedProduct(Product associatedProduct) {
        this.associated_product = associatedProduct;
    }

    public int getOccurences() {
        return occurences;
    }

    public void setOccurences(int occurences) {
        this.occurences = occurences;
    }

    @Override
    public String toString() {
        return "Association{" +
                "id=" + id +
                ", selectedProduct=" + selected_product +
                ", associatedProduct=" + associated_product +
                ", occurences=" + occurences +
                '}';
    }

}
