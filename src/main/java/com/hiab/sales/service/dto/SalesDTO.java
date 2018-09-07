package com.hiab.sales.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Sales entity.
 */
public class SalesDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer finalPrice;

    private Instant createDate;

    private Integer userId;

    private Long clientId;

    private Long contactId;

    private Set<ProductDTO> products = new HashSet<>();

    private Set<SaleConditionsDTO> saleConditions = new HashSet<>();

    private String clientName;

    private String contactName;

    private String contactSurname;

    private String clientAddress;

    private String clientNumDocument;

    private String contactCellphone;

    private String contactEmail;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Integer finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    public Set<SaleConditionsDTO> getSaleConditions() {
        return saleConditions;
    }

    public void setSaleConditions(Set<SaleConditionsDTO> saleConditions) {
        this.saleConditions = saleConditions;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }


    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactSurname() {
        return contactSurname;
    }

    public void setContactSurname(String contactSurname) {
        this.contactSurname = contactSurname;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }


    public String getContactCellPhone() {
        return contactCellphone;
    }

    public void setContactCellPhone(String contactCellphone) {
        this.contactCellphone = contactCellphone;
    }

    public String getClientNumDocument() {
        return clientNumDocument;
    }

    public void setClientNumDocument(String clientNumDocument) {
        this.clientNumDocument = clientNumDocument;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SalesDTO salesDTO = (SalesDTO) o;
        if(salesDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), salesDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SalesDTO{" +
            "id=" + getId() +
            ", finalPrice=" + getFinalPrice() +
            ", createDate='" + getCreateDate() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
