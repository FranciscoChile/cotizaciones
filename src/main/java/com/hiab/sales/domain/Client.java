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
 * A Client.
 */
@Entity
@Table(name = "client")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "num_document", nullable = false)
    private String numDocument;

    @Column(name = "address")
    private String address;

    @Column(name = "comments")
    private String comments;

    @Column(name = "create_date")
    private Instant createDate;

    @Column(name = "active")
    private Integer active;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private Set<Contact> contacts = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private Set<Location> locations = new HashSet<>();

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private Set<Sales> sales = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Client name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumDocument() {
        return numDocument;
    }

    public Client numDocument(String numDocument) {
        this.numDocument = numDocument;
        return this;
    }

    public void setNumDocument(String numDocument) {
        this.numDocument = numDocument;
    }

    public String getAddress() {
        return address;
    }

    public Client address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComments() {
        return comments;
    }

    public Client comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public Client createDate(Instant createDate) {
        this.createDate = createDate;
        return this;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Integer getActive() {
        return active;
    }

    public Client active(Integer active) {
        this.active = active;
        return this;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public Client contacts(Set<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }

    public Client addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setClient(this);
        return this;
    }

    public Client removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setClient(null);
        return this;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public Client locations(Set<Location> locations) {
        this.locations = locations;
        return this;
    }

    public Client addLocation(Location location) {
        this.locations.add(location);
        location.setClient(this);
        return this;
    }

    public Client removeLocation(Location location) {
        this.locations.remove(location);
        location.setClient(null);
        return this;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Sales> getSales() {
        return sales;
    }

    public Client sales(Set<Sales> sales) {
        this.sales = sales;
        return this;
    }

    public Client addSales(Sales sales) {
        this.sales.add(sales);
        sales.setClient(this);
        return this;
    }

    public Client removeSales(Sales sales) {
        this.sales.remove(sales);
        sales.setClient(null);
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
        Client client = (Client) o;
        if (client.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", numDocument='" + getNumDocument() + "'" +
            ", address='" + getAddress() + "'" +
            ", comments='" + getComments() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", active=" + getActive() +
            "}";
    }
}
