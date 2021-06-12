package com.medicai.pillpal.service.dto;

import com.medicai.pillpal.domain.enumeration.TakenStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.medicai.pillpal.domain.TimeTable} entity.
 */
public class TimeTableDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant startDatetime;

    @NotNull
    private Instant endDateTime;

    @NotNull
    private TakenStatus isTaken;

    private PrescriptionDTO prescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Instant startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Instant getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }

    public TakenStatus getIsTaken() {
        return isTaken;
    }

    public void setIsTaken(TakenStatus isTaken) {
        this.isTaken = isTaken;
    }

    public PrescriptionDTO getPrescription() {
        return prescription;
    }

    public void setPrescription(PrescriptionDTO prescription) {
        this.prescription = prescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimeTableDTO)) {
            return false;
        }

        TimeTableDTO timeTableDTO = (TimeTableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, timeTableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TimeTableDTO{" +
            "id=" + getId() +
            ", startDatetime='" + getStartDatetime() + "'" +
            ", endDateTime='" + getEndDateTime() + "'" +
            ", isTaken='" + getIsTaken() + "'" +
            ", prescription=" + getPrescription() +
            "}";
    }
}
