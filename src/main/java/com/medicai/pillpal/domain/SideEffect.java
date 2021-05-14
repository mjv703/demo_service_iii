package com.medicai.pillpal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SideEffect.
 */
@Entity
@Table(name = "side_effect")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SideEffect implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "side_effect", nullable = false)
    private String sideEffect;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sideEffects", "prescriptions" }, allowSetters = true)
    private Medicine medicine;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SideEffect id(Long id) {
        this.id = id;
        return this;
    }

    public String getSideEffect() {
        return this.sideEffect;
    }

    public SideEffect sideEffect(String sideEffect) {
        this.sideEffect = sideEffect;
        return this;
    }

    public void setSideEffect(String sideEffect) {
        this.sideEffect = sideEffect;
    }

    public Medicine getMedicine() {
        return this.medicine;
    }

    public SideEffect medicine(Medicine medicine) {
        this.setMedicine(medicine);
        return this;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SideEffect)) {
            return false;
        }
        return id != null && id.equals(((SideEffect) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SideEffect{" +
            "id=" + getId() +
            ", sideEffect='" + getSideEffect() + "'" +
            "}";
    }
}
