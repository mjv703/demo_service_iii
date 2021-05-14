package com.medicai.pillpal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medicai.pillpal.domain.enumeration.TakenStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TimeTable.
 */
@Entity
@Table(name = "time_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TimeTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "start_datetime", nullable = false)
    private Instant startDatetime;

    @NotNull
    @Column(name = "end_date_time", nullable = false)
    private Instant endDateTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "is_taken", nullable = false)
    private TakenStatus isTaken;

    @ManyToOne
    @JsonIgnoreProperties(value = { "devices", "timeTables", "patientInfo", "medicine", "pharmacy" }, allowSetters = true)
    private Prescription prescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TimeTable id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getStartDatetime() {
        return this.startDatetime;
    }

    public TimeTable startDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
        return this;
    }

    public void setStartDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Instant getEndDateTime() {
        return this.endDateTime;
    }

    public TimeTable endDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    public void setEndDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }

    public TakenStatus getIsTaken() {
        return this.isTaken;
    }

    public TimeTable isTaken(TakenStatus isTaken) {
        this.isTaken = isTaken;
        return this;
    }

    public void setIsTaken(TakenStatus isTaken) {
        this.isTaken = isTaken;
    }

    public Prescription getPrescription() {
        return this.prescription;
    }

    public TimeTable prescription(Prescription prescription) {
        this.setPrescription(prescription);
        return this;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeTable)) {
            return false;
        }
        return id != null && id.equals(((TimeTable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeTable{" +
            "id=" + getId() +
            ", startDatetime='" + getStartDatetime() + "'" +
            ", endDateTime='" + getEndDateTime() + "'" +
            ", isTaken='" + getIsTaken() + "'" +
            "}";
    }
}
