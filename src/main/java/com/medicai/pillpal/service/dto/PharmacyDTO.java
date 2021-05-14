package com.medicai.pillpal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.medicai.pillpal.domain.Pharmacy} entity.
 */
public class PharmacyDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String store;

    private String address;

    private String phoneNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PharmacyDTO)) {
            return false;
        }

        PharmacyDTO pharmacyDTO = (PharmacyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pharmacyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PharmacyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", store='" + getStore() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
