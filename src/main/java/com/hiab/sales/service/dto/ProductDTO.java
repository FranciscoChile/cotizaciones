package com.hiab.sales.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Product entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    private String model;

    @Min(value = 0L)
    private Long priceList;

    @Min(value = 0)
    private Integer stock;

    @Size(max = 2000)
    private String description;

    @Lob
    private byte[] imageRef;
    private String imageRefContentType;

    @Lob
    private byte[] loadDiagram;
    private String loadDiagramContentType;

    private Instant createDate;

    private Integer active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getPriceList() {
        return priceList;
    }

    public void setPriceList(Long priceList) {
        this.priceList = priceList;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImageRef() {
        return imageRef;
    }

    public void setImageRef(byte[] imageRef) {
        this.imageRef = imageRef;
    }

    public String getImageRefContentType() {
        return imageRefContentType;
    }

    public void setImageRefContentType(String imageRefContentType) {
        this.imageRefContentType = imageRefContentType;
    }

    public byte[] getLoadDiagram() {
        return loadDiagram;
    }

    public void setLoadDiagram(byte[] loadDiagram) {
        this.loadDiagram = loadDiagram;
    }

    public String getLoadDiagramContentType() {
        return loadDiagramContentType;
    }

    public void setLoadDiagramContentType(String loadDiagramContentType) {
        this.loadDiagramContentType = loadDiagramContentType;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if(productDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", model='" + getModel() + "'" +
            ", priceList=" + getPriceList() +
            ", stock=" + getStock() +
            ", description='" + getDescription() + "'" +
            ", imageRef='" + getImageRef() + "'" +
            ", loadDiagram='" + getLoadDiagram() + "'" +
            ", createDate='" + getCreateDate() + "'" +
            ", active=" + getActive() +
            "}";
    }
}
