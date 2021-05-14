package com.medicai.pillpal.service.dto;

import com.medicai.pillpal.domain.Medicine;
import com.medicai.pillpal.domain.SideEffect;
import com.medicai.pillpal.domain.enumeration.MedicAppearanceType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.medicai.pillpal.domain.Medicine} entity.
 */
public class MedicineDTO implements Serializable {

    private Long id;

    @NotNull
    private String brandName;

    @NotNull
    private String genericName;

    @NotNull
    private String substanceName;

    @NotNull
    private String manufacturerName;

    @NotNull
    private String productNDC;

    @NotNull
    private String packageNDC;

    private String rx;

    private MedicAppearanceType medicRout;

    private String medicImageUrl;

    private Set<SideEffect> sideEffects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getSubstanceName() {
        return substanceName;
    }

    public void setSubstanceName(String substanceName) {
        this.substanceName = substanceName;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getProductNDC() {
        return productNDC;
    }

    public void setProductNDC(String productNDC) {
        this.productNDC = productNDC;
    }

    public String getPackageNDC() {
        return packageNDC;
    }

    public void setPackageNDC(String packageNDC) {
        this.packageNDC = packageNDC;
    }

    public String getRx() {
        return rx;
    }

    public void setRx(String rx) {
        this.rx = rx;
    }

    public MedicAppearanceType getMedicRout() {
        return medicRout;
    }

    public void setMedicRout(MedicAppearanceType medicRout) {
        this.medicRout = medicRout;
    }

    public String getMedicImageUrl() {
        return medicImageUrl;
    }

    public void setMedicImageUrl(String medicImageUrl) {
        this.medicImageUrl = medicImageUrl;
    }

    public Set<SideEffect> getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(Set<SideEffect> sideEffects) {
        this.sideEffects = sideEffects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicineDTO)) {
            return false;
        }

        MedicineDTO medicineDTO = (MedicineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicineDTO{" +
            "id=" + getId() +
            ", brandName='" + getBrandName() + "'" +
            ", genericName='" + getGenericName() + "'" +
            ", substanceName='" + getSubstanceName() + "'" +
            ", manufacturerName='" + getManufacturerName() + "'" +
            ", productNDC='" + getProductNDC() + "'" +
            ", packageNDC='" + getPackageNDC() + "'" +
            ", rx='" + getRx() + "'" +
            ", medicRout='" + getMedicRout() + "'" +
            ", medicImageUrl='" + getMedicImageUrl() + "'" +
            "}";
    }
}
