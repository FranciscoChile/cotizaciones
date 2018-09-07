package com.hiab.sales.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SaleConditions.
 */
@Entity
@Table(name = "sale_conditions")
public class SaleConditions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_key")
    private String key;

    @Size(max = 2000)
    @Column(name = "jhi_value", length = 2000)
    private String value;

    @ManyToMany(mappedBy = "saleConditions")
    @JsonIgnore
    private Set<Sales> sales = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public SaleConditions key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public SaleConditions value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<Sales> getSales() {
        return sales;
    }

    public SaleConditions sales(Set<Sales> sales) {
        this.sales = sales;
        return this;
    }

    public SaleConditions addSales(Sales sales) {
        this.sales.add(sales);
        sales.getSaleConditions().add(this);
        return this;
    }

    public SaleConditions removeSales(Sales sales) {
        this.sales.remove(sales);
        sales.getSaleConditions().remove(this);
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
        SaleConditions saleConditions = (SaleConditions) o;
        if (saleConditions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), saleConditions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SaleConditions{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
