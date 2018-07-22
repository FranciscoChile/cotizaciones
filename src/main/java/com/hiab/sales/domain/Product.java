package com.hiab.sales.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model")
    private String model;

    @Min(value = 0L)
    @Column(name = "price_list")
    private Long priceList;

    @Min(value = 0)
    @Column(name = "stock")
    private Integer stock;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @Lob
    @Column(name = "image_ref")
    private byte[] imageRef;

    @Column(name = "image_ref_content_type")
    private String imageRefContentType;

    @Lob
    @Column(name = "load_diagram")
    private byte[] loadDiagram;

    @Column(name = "load_diagram_content_type")
    private String loadDiagramContentType;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "active")
    private Integer active;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private Set<Sales> sales = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public Product model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getPriceList() {
        return priceList;
    }

    public Product priceList(Long priceList) {
        this.priceList = priceList;
        return this;
    }

    public void setPriceList(Long priceList) {
        this.priceList = priceList;
    }

    public Integer getStock() {
        return stock;
    }

    public Product stock(Integer stock) {
        this.stock = stock;
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImageRef() {
        return imageRef;
    }

    public Product imageRef(byte[] imageRef) {
        this.imageRef = imageRef;
        return this;
    }

    public void setImageRef(byte[] imageRef) {
        this.imageRef = imageRef;
    }

    public String getImageRefContentType() {
        return imageRefContentType;
    }

    public Product imageRefContentType(String imageRefContentType) {
        this.imageRefContentType = imageRefContentType;
        return this;
    }

    public void setImageRefContentType(String imageRefContentType) {
        this.imageRefContentType = imageRefContentType;
    }

    public byte[] getLoadDiagram() {
        return loadDiagram;
    }

    public Product loadDiagram(byte[] loadDiagram) {
        this.loadDiagram = loadDiagram;
        return this;
    }

    public void setLoadDiagram(byte[] loadDiagram) {
        this.loadDiagram = loadDiagram;
    }

    public String getLoadDiagramContentType() {
        return loadDiagramContentType;
    }

    public Product loadDiagramContentType(String loadDiagramContentType) {
        this.loadDiagramContentType = loadDiagramContentType;
        return this;
    }

    public void setLoadDiagramContentType(String loadDiagramContentType) {
        this.loadDiagramContentType = loadDiagramContentType;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Product createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Integer getActive() {
        return active;
    }

    public Product active(Integer active) {
        this.active = active;
        return this;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Set<Sales> getSales() {
        return sales;
    }

    public Product sales(Set<Sales> sales) {
        this.sales = sales;
        return this;
    }

    public Product addSales(Sales sales) {
        this.sales.add(sales);
        sales.getProducts().add(this);
        return this;
    }

    public Product removeSales(Sales sales) {
        this.sales.remove(sales);
        sales.getProducts().remove(this);
        return this;
    }

    public void setSales(Set<Sales> sales) {
        this.sales = sales;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        if (product.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), product.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", model='" + getModel() + "'" +
            ", priceList=" + getPriceList() +
            ", stock=" + getStock() +
            ", description='" + getDescription() + "'" +
            ", imageRef='" + getImageRef() + "'" +
            ", imageRefContentType='" + getImageRefContentType() + "'" +
            ", loadDiagram='" + getLoadDiagram() + "'" +
            ", loadDiagramContentType='" + getLoadDiagramContentType() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", active=" + getActive() +
            "}";
    }
}
