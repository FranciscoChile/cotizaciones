package com.hiab.sales.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "active")
    private Integer active;

    @ManyToOne
    private Client client;

    @OneToMany(mappedBy = "location")
    @JsonIgnore
    private Set<Sales> sales = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public Location description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Location createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Integer getActive() {
        return active;
    }

    public Location active(Integer active) {
        this.active = active;
        return this;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Client getClient() {
        return client;
    }

    public Location client(Client client) {
        this.client = client;
        return this;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Sales> getSales() {
        return sales;
    }

    public Location sales(Set<Sales> sales) {
        this.sales = sales;
        return this;
    }

    public Location addSales(Sales sales) {
        this.sales.add(sales);
        sales.setLocation(this);
        return this;
    }

    public Location removeSales(Sales sales) {
        this.sales.remove(sales);
        sales.setLocation(null);
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
        Location location = (Location) o;
        if (location.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), location.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", active=" + getActive() +
            "}";
    }
}
