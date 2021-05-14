package com.medicai.pillpal.service.criteria;

import com.medicai.pillpal.domain.enumeration.MedicAppearanceType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.medicai.pillpal.domain.Medicine} entity. This class is used
 * in {@link com.medicai.pillpal.web.rest.MedicineResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medicines?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MedicineCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MedicAppearanceType
     */
    public static class MedicAppearanceTypeFilter extends Filter<MedicAppearanceType> {

        public MedicAppearanceTypeFilter() {}

        public MedicAppearanceTypeFilter(MedicAppearanceTypeFilter filter) {
            super(filter);
        }

        @Override
        public MedicAppearanceTypeFilter copy() {
            return new MedicAppearanceTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter brandName;

    private StringFilter genericName;

    private StringFilter substanceName;

    private StringFilter manufacturerName;

    private StringFilter productNDC;

    private StringFilter packageNDC;

    private StringFilter rx;

    private MedicAppearanceTypeFilter medicRout;

    private StringFilter medicImageUrl;

    private LongFilter sideEffectId;

    private LongFilter prescriptionId;

    public MedicineCriteria() {}

    public MedicineCriteria(MedicineCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.brandName = other.brandName == null ? null : other.brandName.copy();
        this.genericName = other.genericName == null ? null : other.genericName.copy();
        this.substanceName = other.substanceName == null ? null : other.substanceName.copy();
        this.manufacturerName = other.manufacturerName == null ? null : other.manufacturerName.copy();
        this.productNDC = other.productNDC == null ? null : other.productNDC.copy();
        this.packageNDC = other.packageNDC == null ? null : other.packageNDC.copy();
        this.rx = other.rx == null ? null : other.rx.copy();
        this.medicRout = other.medicRout == null ? null : other.medicRout.copy();
        this.medicImageUrl = other.medicImageUrl == null ? null : other.medicImageUrl.copy();
        this.sideEffectId = other.sideEffectId == null ? null : other.sideEffectId.copy();
        this.prescriptionId = other.prescriptionId == null ? null : other.prescriptionId.copy();
    }

    @Override
    public MedicineCriteria copy() {
        return new MedicineCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getBrandName() {
        return brandName;
    }

    public StringFilter brandName() {
        if (brandName == null) {
            brandName = new StringFilter();
        }
        return brandName;
    }

    public void setBrandName(StringFilter brandName) {
        this.brandName = brandName;
    }

    public StringFilter getGenericName() {
        return genericName;
    }

    public StringFilter genericName() {
        if (genericName == null) {
            genericName = new StringFilter();
        }
        return genericName;
    }

    public void setGenericName(StringFilter genericName) {
        this.genericName = genericName;
    }

    public StringFilter getSubstanceName() {
        return substanceName;
    }

    public StringFilter substanceName() {
        if (substanceName == null) {
            substanceName = new StringFilter();
        }
        return substanceName;
    }

    public void setSubstanceName(StringFilter substanceName) {
        this.substanceName = substanceName;
    }

    public StringFilter getManufacturerName() {
        return manufacturerName;
    }

    public StringFilter manufacturerName() {
        if (manufacturerName == null) {
            manufacturerName = new StringFilter();
        }
        return manufacturerName;
    }

    public void setManufacturerName(StringFilter manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public StringFilter getProductNDC() {
        return productNDC;
    }

    public StringFilter productNDC() {
        if (productNDC == null) {
            productNDC = new StringFilter();
        }
        return productNDC;
    }

    public void setProductNDC(StringFilter productNDC) {
        this.productNDC = productNDC;
    }

    public StringFilter getPackageNDC() {
        return packageNDC;
    }

    public StringFilter packageNDC() {
        if (packageNDC == null) {
            packageNDC = new StringFilter();
        }
        return packageNDC;
    }

    public void setPackageNDC(StringFilter packageNDC) {
        this.packageNDC = packageNDC;
    }

    public StringFilter getRx() {
        return rx;
    }

    public StringFilter rx() {
        if (rx == null) {
            rx = new StringFilter();
        }
        return rx;
    }

    public void setRx(StringFilter rx) {
        this.rx = rx;
    }

    public MedicAppearanceTypeFilter getMedicRout() {
        return medicRout;
    }

    public MedicAppearanceTypeFilter medicRout() {
        if (medicRout == null) {
            medicRout = new MedicAppearanceTypeFilter();
        }
        return medicRout;
    }

    public void setMedicRout(MedicAppearanceTypeFilter medicRout) {
        this.medicRout = medicRout;
    }

    public StringFilter getMedicImageUrl() {
        return medicImageUrl;
    }

    public StringFilter medicImageUrl() {
        if (medicImageUrl == null) {
            medicImageUrl = new StringFilter();
        }
        return medicImageUrl;
    }

    public void setMedicImageUrl(StringFilter medicImageUrl) {
        this.medicImageUrl = medicImageUrl;
    }

    public LongFilter getSideEffectId() {
        return sideEffectId;
    }

    public LongFilter sideEffectId() {
        if (sideEffectId == null) {
            sideEffectId = new LongFilter();
        }
        return sideEffectId;
    }

    public void setSideEffectId(LongFilter sideEffectId) {
        this.sideEffectId = sideEffectId;
    }

    public LongFilter getPrescriptionId() {
        return prescriptionId;
    }

    public LongFilter prescriptionId() {
        if (prescriptionId == null) {
            prescriptionId = new LongFilter();
        }
        return prescriptionId;
    }

    public void setPrescriptionId(LongFilter prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MedicineCriteria that = (MedicineCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(brandName, that.brandName) &&
            Objects.equals(genericName, that.genericName) &&
            Objects.equals(substanceName, that.substanceName) &&
            Objects.equals(manufacturerName, that.manufacturerName) &&
            Objects.equals(productNDC, that.productNDC) &&
            Objects.equals(packageNDC, that.packageNDC) &&
            Objects.equals(rx, that.rx) &&
            Objects.equals(medicRout, that.medicRout) &&
            Objects.equals(medicImageUrl, that.medicImageUrl) &&
            Objects.equals(sideEffectId, that.sideEffectId) &&
            Objects.equals(prescriptionId, that.prescriptionId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            brandName,
            genericName,
            substanceName,
            manufacturerName,
            productNDC,
            packageNDC,
            rx,
            medicRout,
            medicImageUrl,
            sideEffectId,
            prescriptionId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedicineCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (brandName != null ? "brandName=" + brandName + ", " : "") +
            (genericName != null ? "genericName=" + genericName + ", " : "") +
            (substanceName != null ? "substanceName=" + substanceName + ", " : "") +
            (manufacturerName != null ? "manufacturerName=" + manufacturerName + ", " : "") +
            (productNDC != null ? "productNDC=" + productNDC + ", " : "") +
            (packageNDC != null ? "packageNDC=" + packageNDC + ", " : "") +
            (rx != null ? "rx=" + rx + ", " : "") +
            (medicRout != null ? "medicRout=" + medicRout + ", " : "") +
            (medicImageUrl != null ? "medicImageUrl=" + medicImageUrl + ", " : "") +
            (sideEffectId != null ? "sideEffectId=" + sideEffectId + ", " : "") +
            (prescriptionId != null ? "prescriptionId=" + prescriptionId + ", " : "") +
            "}";
    }
}
