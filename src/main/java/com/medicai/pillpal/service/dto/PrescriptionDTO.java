package com.medicai.pillpal.service.dto;

import com.medicai.pillpal.domain.enumeration.ColorContentType;
import com.medicai.pillpal.domain.enumeration.MedicType;
import com.medicai.pillpal.domain.enumeration.PrescriptionStatusType;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.medicai.pillpal.domain.Prescription} entity.
 */
public class PrescriptionDTO implements Serializable {

    private Long id;

    private String prescriptionCode;

    private String barCode;

    @NotNull
    private Instant issueDate;

    @NotNull
    private String usageDescription;

    @NotNull
    private String importantInfo;

    @NotNull
    private Integer qty;

    @NotNull
    private Boolean hasRefill;

    private Instant refillTime;

    private String prescriptionImageUrl;

    private MedicType medicType;

    private ColorContentType medicColor;

    private PrescriptionStatusType status;

    private PatientInfoDTO patientInfo;

    private MedicineDTO medicine;

    private PharmacyDTO pharmacy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrescriptionCode() {
        return prescriptionCode;
    }

    public void setPrescriptionCode(String prescriptionCode) {
        this.prescriptionCode = prescriptionCode;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Instant getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public String getUsageDescription() {
        return usageDescription;
    }

    public void setUsageDescription(String usageDescription) {
        this.usageDescription = usageDescription;
    }

    public String getImportantInfo() {
        return importantInfo;
    }

    public void setImportantInfo(String importantInfo) {
        this.importantInfo = importantInfo;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Boolean getHasRefill() {
        return hasRefill;
    }

    public void setHasRefill(Boolean hasRefill) {
        this.hasRefill = hasRefill;
    }

    public Instant getRefillTime() {
        return refillTime;
    }

    public void setRefillTime(Instant refillTime) {
        this.refillTime = refillTime;
    }

    public String getPrescriptionImageUrl() {
        return prescriptionImageUrl;
    }

    public void setPrescriptionImageUrl(String prescriptionImageUrl) {
        this.prescriptionImageUrl = prescriptionImageUrl;
    }

    public MedicType getMedicType() {
        return medicType;
    }

    public void setMedicType(MedicType medicType) {
        this.medicType = medicType;
    }

    public ColorContentType getMedicColor() {
        return medicColor;
    }

    public void setMedicColor(ColorContentType medicColor) {
        this.medicColor = medicColor;
    }

    public PrescriptionStatusType getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatusType status) {
        this.status = status;
    }

    public PatientInfoDTO getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfoDTO patientInfo) {
        this.patientInfo = patientInfo;
    }

    public MedicineDTO getMedicine() {
        return medicine;
    }

    public void setMedicine(MedicineDTO medicine) {
        this.medicine = medicine;
    }

    public PharmacyDTO getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(PharmacyDTO pharmacy) {
        this.pharmacy = pharmacy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrescriptionDTO)) {
            return false;
        }

        PrescriptionDTO prescriptionDTO = (PrescriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, prescriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrescriptionDTO{" +
            "id=" + getId() +
            ", prescriptionCode='" + getPrescriptionCode() + "'" +
            ", barCode='" + getBarCode() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", usageDescription='" + getUsageDescription() + "'" +
            ", importantInfo='" + getImportantInfo() + "'" +
            ", qty=" + getQty() +
            ", hasRefill='" + getHasRefill() + "'" +
            ", refillTime='" + getRefillTime() + "'" +
            ", prescriptionImageUrl='" + getPrescriptionImageUrl() + "'" +
            ", medicType='" + getMedicType() + "'" +
            ", medicColor='" + getMedicColor() + "'" +
            ", status='" + getStatus() + "'" +
            ", patientInfo=" + getPatientInfo() +
            ", medicine=" + getMedicine() +
            ", pharmacy=" + getPharmacy() +
            "}";
    }
}
