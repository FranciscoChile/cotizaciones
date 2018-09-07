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

    private IntegerFilter userId;

    private LongFilter clientId;

    private LongFilter contactId;

    private LongFilter productId;

    private LongFilter saleConditionsId;

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

    public IntegerFilter getUserId() {
        return userId;
    }

    public void setUserId(IntegerFilter userId) {
        this.userId = userId;
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

    public LongFilter getProductId() {
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
    }

    public LongFilter getSaleConditionsId() {
        return saleConditionsId;
    }

    public void setSaleConditionsId(LongFilter saleConditionsId) {
        this.saleConditionsId = saleConditionsId;
    }

    @Override
    public String toString() {
        return "SalesCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (finalPrice != null ? "finalPrice=" + finalPrice + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
                (contactId != null ? "contactId=" + contactId + ", " : "") +
                (productId != null ? "productId=" + productId + ", " : "") +
                (saleConditionsId != null ? "saleConditionsId=" + saleConditionsId + ", " : "") +
            "}";
    }

}
