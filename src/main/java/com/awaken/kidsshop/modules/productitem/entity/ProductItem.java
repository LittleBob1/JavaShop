package com.awaken.kidsshop.modules.productitem.entity;

import com.awaken.kidsshop.modules.product.entity.Product;
import com.awaken.kidsshop.modules.size.entity.Size;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "size_id")
    @NotNull
    private Size size;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull
    private Product product;

    public ProductItem() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
