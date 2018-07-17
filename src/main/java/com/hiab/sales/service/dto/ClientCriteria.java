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
 * Criteria class for the Client entity. This class is used in ClientResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /clients?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClientCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter numDocument;

    private StringFilter address;

    private StringFilter comments;

    private InstantFilter createDate;

    private IntegerFilter active;

    private LongFilter contactId;

    private LongFilter locationId;

    private LongFilter salesId;

    public ClientCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getNumDocument() {
        return numDocument;
    }

    public void setNumDocument(StringFilter numDocument) {
        this.numDocument = numDocument;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getComments() {
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
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

    public LongFilter getSalesId() {
        return salesId;
    }

    public void setSalesId(LongFilter salesId) {
        this.salesId = salesId;
    }

    @Override
    public String toString() {
        return "ClientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (numDocument != null ? "numDocument=" + numDocument + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (comments != null ? "comments=" + comments + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (contactId != null ? "contactId=" + contactId + ", " : "") +
                (locationId != null ? "locationId=" + locationId + ", " : "") +
                (salesId != null ? "salesId=" + salesId + ", " : "") +
            "}";
    }

}
