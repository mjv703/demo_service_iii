package com.medicai.pillpal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.medicai.pillpal.domain.SideEffect} entity.
 */
public class SideEffectDTO implements Serializable {

    private Long id;

    @NotNull
    private String sideEffect;

    private MedicineDTO medicine;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSideEffect() {
        return sideEffect;
    }

    public void setSideEffect(String sideEffect) {
        this.sideEffect = sideEffect;
    }

    public MedicineDTO getMedicine() {
        return medicine;
    }

    public void setMedicine(MedicineDTO medicine) {
        this.medicine = medicine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SideEffectDTO)) {
            return false;
        }

        SideEffectDTO sideEffectDTO = (SideEffectDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, sideEffectDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SideEffectDTO{" +
            "id=" + getId() +
            ", sideEffect='" + getSideEffect() + "'" +
            ", medicine=" + getMedicine() +
            "}";
    }
}
