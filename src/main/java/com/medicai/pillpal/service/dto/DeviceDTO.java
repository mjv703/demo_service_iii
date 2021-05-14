package com.medicai.pillpal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.medicai.pillpal.domain.Device} entity.
 */
public class DeviceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String model;

    @NotNull
    private String serialNo;

    private PatientInfoDTO patientInfo;

    private PrescriptionDTO prescription;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public PatientInfoDTO getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfoDTO patientInfo) {
        this.patientInfo = patientInfo;
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
        if (!(o instanceof DeviceDTO)) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", model='" + getModel() + "'" +
            ", serialNo='" + getSerialNo() + "'" +
            ", patientInfo=" + getPatientInfo() +
            ", prescription=" + getPrescription() +
            "}";
    }
}
