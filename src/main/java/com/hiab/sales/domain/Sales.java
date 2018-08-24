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

    @Column(name = "active")
    private Integer active;

    @Size(max = 2000)
    @Column(name = "conditions", length = 2000)
    private String conditions;

    @Column(name = "user_id")
    private Integer userId;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Contact contact;

    @ManyToOne
    private Location location;

    @OneToMany(mappedBy = "sales")
    private Set<SaleCondition> saleConditions = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "sales_product",
               joinColumns = @JoinColumn(name="sales_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="products_id", referencedColumnName="id"))
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public Integer getActive() {
        return active;
    }

    public Sales active(Integer active) {
        this.active = active;
        return this;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getConditions() {
        return conditions;
    }

    public Sales conditions(String conditions) {
        this.conditions = conditions;
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
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

    public Location getLocation() {
        return location;
    }

    public Sales location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<SaleCondition> getSaleConditions() {
        return saleConditions;
    }

    public Sales saleConditions(Set<SaleCondition> saleConditions) {
        this.saleConditions = saleConditions;
        return this;
    }

    public Sales addSaleCondition(SaleCondition saleCondition) {
        this.saleConditions.add(saleCondition);
        saleCondition.setSales(this);
        return this;
    }

    public Sales removeSaleCondition(SaleCondition saleCondition) {
        this.saleConditions.remove(saleCondition);
        saleCondition.setSales(null);
        return this;
    }

    public void setSaleConditions(Set<SaleCondition> saleConditions) {
        this.saleConditions = saleConditions;
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
            ", active=" + getActive() +
            ", conditions='" + getConditions() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
