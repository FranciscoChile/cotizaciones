package com.hiab.sales.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sales.
 */
@Entity
@Table(name = "sales")
public class Sales implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0)
    @Column(name = "final_price")
    private Integer finalPrice;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Contact contact;

    @Column(name = "currency")
    private String currency;


    @ManyToMany
    @JoinTable(name = "sales_product",
               joinColumns = @JoinColumn(name="sales_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="products_id", referencedColumnName="id"))
    private Set<Product> products = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "sales_sale_conditions",
               joinColumns = @JoinColumn(name="sales_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="sale_conditions_id", referencedColumnName="id"))
    private Set<SaleConditions> saleConditions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCurrency() {
      return currency;
    }

    public void setCurrency(String currency) {
      this.currency = currency;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public Sales finalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
        return this;
    }

    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Sales createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public Sales userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Client getClient() {
        return client;
    }

    public Sales client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Contact getContact() {
        return contact;
    }

    public Sales contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public Sales products(Set<Product> products) {
        this.products = products;
        return this;
    }

    public Sales addProduct(Product product) {
        this.products.add(product);
        product.getSales().add(this);
        return this;
    }

    public Sales removeProduct(Product product) {
        this.products.remove(product);
        product.getSales().remove(this);
        return this;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Set<SaleConditions> getSaleConditions() {
        return saleConditions;
    }

    public Sales saleConditions(Set<SaleConditions> saleConditions) {
        this.saleConditions = saleConditions;
        return this;
    }

    public Sales addSaleConditions(SaleConditions saleConditions) {
        this.saleConditions.add(saleConditions);
        saleConditions.getSales().add(this);
        return this;
    }

    public Sales removeSaleConditions(SaleConditions saleConditions) {
        this.saleConditions.remove(saleConditions);
        saleConditions.getSales().remove(this);
        return this;
    }

    public void setSaleConditions(Set<SaleConditions> saleConditions) {
        this.saleConditions = saleConditions;
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
        Sales sales = (Sales) o;
        if (sales.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sales.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sales{" +
            "id=" + getId() +
            ", finalPrice=" + getFinalPrice() +
            ", createDate='" + getCreateDate() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
