package com.hiab.sales.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Sales entity. This class is used in SalesResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sales?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SalesCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private IntegerFilter finalPrice;

    private InstantFilter createDate;

    private IntegerFilter active;

    private StringFilter conditions;

    private LongFilter clientId;

    private LongFilter contactId;

    private LongFilter locationId;

    private LongFilter productId;

    public SalesCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(IntegerFilter finalPrice) {
        this.finalPrice = finalPrice;
    }

    public InstantFilter getCreateDate() {
        return createDate;
    }

    public void setCreateDate(InstantFilter createDate) {
        this.createDate = createDate;
    }

    public IntegerFilter getActive() {
        return active;
    }

    public void setActive(IntegerFilter active) {
        this.active = active;
    }

    public StringFilter getConditions() {
        return conditions;
    }

    public void setConditions(StringFilter conditions) {
        this.conditions = conditions;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getContactId() {
        return contactId;
    }

    public void setContactId(LongFilter contactId) {
        this.contactId = contactId;
    }

    public LongFilter getLocationId() {
        return locationId;
    }

    public void setLocationId(LongFilter locationId) {
        this.locationId = locationId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "SalesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (finalPrice != null ? "finalPrice=" + finalPrice + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (conditions != null ? "conditions=" + conditions + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
                (contactId != null ? "contactId=" + contactId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
            "}";
    }

}
