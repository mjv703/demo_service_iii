package com.medicai.pillpal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medicai.pillpal.domain.enumeration.MedicAppearanceType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medicine.
 */
@Entity
@Table(name = "medicine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medicine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "brand_name", nullable = false)
    private String brandName;

    @NotNull
    @Column(name = "generic_name", nullable = false)
    private String genericName;

    @NotNull
    @Column(name = "substance_name", nullable = false)
    private String substanceName;

    @NotNull
    @Column(name = "manufacturer_name", nullable = false)
    private String manufacturerName;

    @NotNull
    @Column(name = "product_ndc", nullable = false)
    private String productNDC;

    @NotNull
    @Column(name = "package_ndc", nullable = false)
    private String packageNDC;

    @Column(name = "rx")
    private String rx;

    @Enumerated(EnumType.STRING)
    @Column(name = "medic_rout")
    private MedicAppearanceType medicRout;

    @Column(name = "medic_image_url")
    private String medicImageUrl;

    @OneToMany(mappedBy = "medicine")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medicine" }, allowSetters = true)
    private Set<SideEffect> sideEffects = new HashSet<>();

    @OneToMany(mappedBy = "medicine")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "devices", "timeTables", "patientInfo", "medicine", "pharmacy" }, allowSetters = true)
    private Set<Prescription> prescriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medicine id(Long id) {
        this.id = id;
        return this;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public Medicine brandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getGenericName() {
        return this.genericName;
    }

    public Medicine genericName(String genericName) {
        this.genericName = genericName;
        return this;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getSubstanceName() {
        return this.substanceName;
    }

    public Medicine substanceName(String substanceName) {
        this.substanceName = substanceName;
        return this;
    }

    public void setSubstanceName(String substanceName) {
        this.substanceName = substanceName;
    }

    public String getManufacturerName() {
        return this.manufacturerName;
    }

    public Medicine manufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
        return this;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getProductNDC() {
        return this.productNDC;
    }

    public Medicine productNDC(String productNDC) {
        this.productNDC = productNDC;
        return this;
    }

    public void setProductNDC(String productNDC) {
        this.productNDC = productNDC;
    }

    public String getPackageNDC() {
        return this.packageNDC;
    }

    public Medicine packageNDC(String packageNDC) {
        this.packageNDC = packageNDC;
        return this;
    }

    public void setPackageNDC(String packageNDC) {
        this.packageNDC = packageNDC;
    }

    public String getRx() {
        return this.rx;
    }

    public Medicine rx(String rx) {
        this.rx = rx;
        return this;
    }

    public void setRx(String rx) {
        this.rx = rx;
    }

    public MedicAppearanceType getMedicRout() {
        return this.medicRout;
    }

    public Medicine medicRout(MedicAppearanceType medicRout) {
        this.medicRout = medicRout;
        return this;
    }

    public void setMedicRout(MedicAppearanceType medicRout) {
        this.medicRout = medicRout;
    }

    public String getMedicImageUrl() {
        return this.medicImageUrl;
    }

    public Medicine medicImageUrl(String medicImageUrl) {
        this.medicImageUrl = medicImageUrl;
        return this;
    }

    public void setMedicImageUrl(String medicImageUrl) {
        this.medicImageUrl = medicImageUrl;
    }

    public Set<SideEffect> getSideEffects() {
        return this.sideEffects;
    }

    public Medicine sideEffects(Set<SideEffect> sideEffects) {
        this.setSideEffects(sideEffects);
        return this;
    }

    public Medicine addSideEffect(SideEffect sideEffect) {
        this.sideEffects.add(sideEffect);
        sideEffect.setMedicine(this);
        return this;
    }

    public Medicine removeSideEffect(SideEffect sideEffect) {
        this.sideEffects.remove(sideEffect);
        sideEffect.setMedicine(null);
        return this;
    }

    public void setSideEffects(Set<SideEffect> sideEffects) {
        if (this.sideEffects != null) {
            this.sideEffects.forEach(i -> i.setMedicine(null));
        }
        if (sideEffects != null) {
            sideEffects.forEach(i -> i.setMedicine(this));
        }
        this.sideEffects = sideEffects;
    }

    public Set<Prescription> getPrescriptions() {
        return this.prescriptions;
    }

    public Medicine prescriptions(Set<Prescription> prescriptions) {
        this.setPrescriptions(prescriptions);
        return this;
    }

    public Medicine addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
        prescription.setMedicine(this);
        return this;
    }

    public Medicine removePrescription(Prescription prescription) {
        this.prescriptions.remove(prescription);
        prescription.setMedicine(null);
        return this;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        if (this.prescriptions != null) {
            this.prescriptions.forEach(i -> i.setMedicine(null));
        }
        if (prescriptions != null) {
            prescriptions.forEach(i -> i.setMedicine(this));
        }
        this.prescriptions = prescriptions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicine)) {
            return false;
        }
        return id != null && id.equals(((Medicine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medicine{" +
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
