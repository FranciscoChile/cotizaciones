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
 * Criteria class for the Contact entity. This class is used in ContactResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /contacts?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ContactCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter name;

    private StringFilter surname;

    private StringFilter position;

    private StringFilter area;

    private StringFilter address;

    private StringFilter cellphone;

    private StringFilter linePhone;

    private StringFilter email;

    private StringFilter comments;

    private InstantFilter createDate;

    private IntegerFilter active;

    private LongFilter clientId;

    private LongFilter salesId;

    public ContactCriteria() {
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

    public StringFilter getSurname() {
        return surname;
    }

    public void setSurname(StringFilter surname) {
        this.surname = surname;
    }

    public StringFilter getPosition() {
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StringFilter getArea() {
        return area;
    }

    public void setArea(StringFilter area) {
        this.area = area;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getCellphone() {
        return cellphone;
    }

    public void setCellphone(StringFilter cellphone) {
        this.cellphone = cellphone;
    }

    public StringFilter getLinePhone() {
        return linePhone;
    }

    public void setLinePhone(StringFilter linePhone) {
        this.linePhone = linePhone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public LongFilter getSalesId() {
        return salesId;
    }

    public void setSalesId(LongFilter salesId) {
        this.salesId = salesId;
    }

    @Override
    public String toString() {
        return "ContactCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (surname != null ? "surname=" + surname + ", " : "") +
                (position != null ? "position=" + position + ", " : "") +
                (area != null ? "area=" + area + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (cellphone != null ? "cellphone=" + cellphone + ", " : "") +
                (linePhone != null ? "linePhone=" + linePhone + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (comments != null ? "comments=" + comments + ", " : "") +
                (createDate != null ? "createDate=" + createDate + ", " : "") +
                (active != null ? "active=" + active + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
                (salesId != null ? "salesId=" + salesId + ", " : "") +
            "}";
    }

}
