package com.medicai.pillpal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Pharmacy.
 */
@Entity
@Table(name = "pharmacy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pharmacy implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "store", nullable = false)
    private String store;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "pharmacy")
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

    public Pharmacy id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Pharmacy name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore() {
        return this.store;
    }

    public Pharmacy store(String store) {
        this.store = store;
        return this;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getAddress() {
        return this.address;
    }

    public Pharmacy address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Pharmacy phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Prescription> getPrescriptions() {
        return this.prescriptions;
    }

    public Pharmacy prescriptions(Set<Prescription> prescriptions) {
        this.setPrescriptions(prescriptions);
        return this;
    }

    public Pharmacy addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
        prescription.setPharmacy(this);
        return this;
    }

    public Pharmacy removePrescription(Prescription prescription) {
        this.prescriptions.remove(prescription);
        prescription.setPharmacy(null);
        return this;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        if (this.prescriptions != null) {
            this.prescriptions.forEach(i -> i.setPharmacy(null));
        }
        if (prescriptions != null) {
            prescriptions.forEach(i -> i.setPharmacy(this));
        }
        this.prescriptions = prescriptions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pharmacy)) {
            return false;
        }
        return id != null && id.equals(((Pharmacy) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pharmacy{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", store='" + getStore() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
