package com.medicai.pillpal.service.criteria;

import com.medicai.pillpal.domain.enumeration.ColorContentType;
import com.medicai.pillpal.domain.enumeration.MedicType;
import com.medicai.pillpal.domain.enumeration.PrescriptionStatusType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.medicai.pillpal.domain.Prescription} entity. This class is used
 * in {@link com.medicai.pillpal.web.rest.PrescriptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prescriptions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrescriptionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MedicType
     */
    public static class MedicTypeFilter extends Filter<MedicType> {

        public MedicTypeFilter() {}

        public MedicTypeFilter(MedicTypeFilter filter) {
            super(filter);
        }

        @Override
        public MedicTypeFilter copy() {
            return new MedicTypeFilter(this);
        }
    }

    /**
     * Class for filtering ColorContentType
     */
    public static class ColorContentTypeFilter extends Filter<ColorContentType> {

        public ColorContentTypeFilter() {}

        public ColorContentTypeFilter(ColorContentTypeFilter filter) {
            super(filter);
        }

        @Override
        public ColorContentTypeFilter copy() {
            return new ColorContentTypeFilter(this);
        }
    }

    /**
     * Class for filtering PrescriptionStatusType
     */
    public static class PrescriptionStatusTypeFilter extends Filter<PrescriptionStatusType> {

        public PrescriptionStatusTypeFilter() {}

        public PrescriptionStatusTypeFilter(PrescriptionStatusTypeFilter filter) {
            super(filter);
        }

        @Override
        public PrescriptionStatusTypeFilter copy() {
            return new PrescriptionStatusTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter prescriptionCode;

    private StringFilter barCode;

    private InstantFilter issueDate;

    private StringFilter usageDescription;

    private StringFilter importantInfo;

    private IntegerFilter qty;

    private BooleanFilter hasRefill;

    private InstantFilter refillTime;

    private StringFilter prescriptionImageUrl;

    private MedicTypeFilter medicType;

    private ColorContentTypeFilter medicColor;

    private PrescriptionStatusTypeFilter status;

    private LongFilter deviceId;

    private LongFilter timeTableId;

    private LongFilter patientInfoId;

    private LongFilter medicineId;

    private LongFilter pharmacyId;

    public PrescriptionCriteria() {}

    public PrescriptionCriteria(PrescriptionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.prescriptionCode = other.prescriptionCode == null ? null : other.prescriptionCode.copy();
        this.barCode = other.barCode == null ? null : other.barCode.copy();
        this.issueDate = other.issueDate == null ? null : other.issueDate.copy();
        this.usageDescription = other.usageDescription == null ? null : other.usageDescription.copy();
        this.importantInfo = other.importantInfo == null ? null : other.importantInfo.copy();
        this.qty = other.qty == null ? null : other.qty.copy();
        this.hasRefill = other.hasRefill == null ? null : other.hasRefill.copy();
        this.refillTime = other.refillTime == null ? null : other.refillTime.copy();
        this.prescriptionImageUrl = other.prescriptionImageUrl == null ? null : other.prescriptionImageUrl.copy();
        this.medicType = other.medicType == null ? null : other.medicType.copy();
        this.medicColor = other.medicColor == null ? null : other.medicColor.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.deviceId = other.deviceId == null ? null : other.deviceId.copy();
        this.timeTableId = other.timeTableId == null ? null : other.timeTableId.copy();
        this.patientInfoId = other.patientInfoId == null ? null : other.patientInfoId.copy();
        this.medicineId = other.medicineId == null ? null : other.medicineId.copy();
        this.pharmacyId = other.pharmacyId == null ? null : other.pharmacyId.copy();
    }

    @Override
    public PrescriptionCriteria copy() {
        return new PrescriptionCriteria(this);
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

    public StringFilter getPrescriptionCode() {
        return prescriptionCode;
    }

    public StringFilter prescriptionCode() {
        if (prescriptionCode == null) {
            prescriptionCode = new StringFilter();
        }
        return prescriptionCode;
    }

    public void setPrescriptionCode(StringFilter prescriptionCode) {
        this.prescriptionCode = prescriptionCode;
    }

    public StringFilter getBarCode() {
        return barCode;
    }

    public StringFilter barCode() {
        if (barCode == null) {
            barCode = new StringFilter();
        }
        return barCode;
    }

    public void setBarCode(StringFilter barCode) {
        this.barCode = barCode;
    }

    public InstantFilter getIssueDate() {
        return issueDate;
    }

    public InstantFilter issueDate() {
        if (issueDate == null) {
            issueDate = new InstantFilter();
        }
        return issueDate;
    }

    public void setIssueDate(InstantFilter issueDate) {
        this.issueDate = issueDate;
    }

    public StringFilter getUsageDescription() {
        return usageDescription;
    }

    public StringFilter usageDescription() {
        if (usageDescription == null) {
            usageDescription = new StringFilter();
        }
        return usageDescription;
    }

    public void setUsageDescription(StringFilter usageDescription) {
        this.usageDescription = usageDescription;
    }

    public StringFilter getImportantInfo() {
        return importantInfo;
    }

    public StringFilter importantInfo() {
        if (importantInfo == null) {
            importantInfo = new StringFilter();
        }
        return importantInfo;
    }

    public void setImportantInfo(StringFilter importantInfo) {
        this.importantInfo = importantInfo;
    }

    public IntegerFilter getQty() {
        return qty;
    }

    public IntegerFilter qty() {
        if (qty == null) {
            qty = new IntegerFilter();
        }
        return qty;
    }

    public void setQty(IntegerFilter qty) {
        this.qty = qty;
    }

    public BooleanFilter getHasRefill() {
        return hasRefill;
    }

    public BooleanFilter hasRefill() {
        if (hasRefill == null) {
            hasRefill = new BooleanFilter();
        }
        return hasRefill;
    }

    public void setHasRefill(BooleanFilter hasRefill) {
        this.hasRefill = hasRefill;
    }

    public InstantFilter getRefillTime() {
        return refillTime;
    }

    public InstantFilter refillTime() {
        if (refillTime == null) {
            refillTime = new InstantFilter();
        }
        return refillTime;
    }

    public void setRefillTime(InstantFilter refillTime) {
        this.refillTime = refillTime;
    }

    public StringFilter getPrescriptionImageUrl() {
        return prescriptionImageUrl;
    }

    public StringFilter prescriptionImageUrl() {
        if (prescriptionImageUrl == null) {
            prescriptionImageUrl = new StringFilter();
        }
        return prescriptionImageUrl;
    }

    public void setPrescriptionImageUrl(StringFilter prescriptionImageUrl) {
        this.prescriptionImageUrl = prescriptionImageUrl;
    }

    public MedicTypeFilter getMedicType() {
        return medicType;
    }

    public MedicTypeFilter medicType() {
        if (medicType == null) {
            medicType = new MedicTypeFilter();
        }
        return medicType;
    }

    public void setMedicType(MedicTypeFilter medicType) {
        this.medicType = medicType;
    }

    public ColorContentTypeFilter getMedicColor() {
        return medicColor;
    }

    public ColorContentTypeFilter medicColor() {
        if (medicColor == null) {
            medicColor = new ColorContentTypeFilter();
        }
        return medicColor;
    }

    public void setMedicColor(ColorContentTypeFilter medicColor) {
        this.medicColor = medicColor;
    }

    public PrescriptionStatusTypeFilter getStatus() {
        return status;
    }

    public PrescriptionStatusTypeFilter status() {
        if (status == null) {
            status = new PrescriptionStatusTypeFilter();
        }
        return status;
    }

    public void setStatus(PrescriptionStatusTypeFilter status) {
        this.status = status;
    }

    public LongFilter getDeviceId() {
        return deviceId;
    }

    public LongFilter deviceId() {
        if (deviceId == null) {
            deviceId = new LongFilter();
        }
        return deviceId;
    }

    public void setDeviceId(LongFilter deviceId) {
        this.deviceId = deviceId;
    }

    public LongFilter getTimeTableId() {
        return timeTableId;
    }

    public LongFilter timeTableId() {
        if (timeTableId == null) {
            timeTableId = new LongFilter();
        }
        return timeTableId;
    }

    public void setTimeTableId(LongFilter timeTableId) {
        this.timeTableId = timeTableId;
    }

    public LongFilter getPatientInfoId() {
        return patientInfoId;
    }

    public LongFilter patientInfoId() {
        if (patientInfoId == null) {
            patientInfoId = new LongFilter();
        }
        return patientInfoId;
    }

    public void setPatientInfoId(LongFilter patientInfoId) {
        this.patientInfoId = patientInfoId;
    }

    public LongFilter getMedicineId() {
        return medicineId;
    }

    public LongFilter medicineId() {
        if (medicineId == null) {
            medicineId = new LongFilter();
        }
        return medicineId;
    }

    public void setMedicineId(LongFilter medicineId) {
        this.medicineId = medicineId;
    }

    public LongFilter getPharmacyId() {
        return pharmacyId;
    }

    public LongFilter pharmacyId() {
        if (pharmacyId == null) {
            pharmacyId = new LongFilter();
        }
        return pharmacyId;
    }

    public void setPharmacyId(LongFilter pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PrescriptionCriteria that = (PrescriptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(prescriptionCode, that.prescriptionCode) &&
            Objects.equals(barCode, that.barCode) &&
            Objects.equals(issueDate, that.issueDate) &&
            Objects.equals(usageDescription, that.usageDescription) &&
            Objects.equals(importantInfo, that.importantInfo) &&
            Objects.equals(qty, that.qty) &&
            Objects.equals(hasRefill, that.hasRefill) &&
            Objects.equals(refillTime, that.refillTime) &&
            Objects.equals(prescriptionImageUrl, that.prescriptionImageUrl) &&
            Objects.equals(medicType, that.medicType) &&
            Objects.equals(medicColor, that.medicColor) &&
            Objects.equals(status, that.status) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(timeTableId, that.timeTableId) &&
            Objects.equals(patientInfoId, that.patientInfoId) &&
            Objects.equals(medicineId, that.medicineId) &&
            Objects.equals(pharmacyId, that.pharmacyId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            prescriptionCode,
            barCode,
            issueDate,
            usageDescription,
            importantInfo,
            qty,
            hasRefill,
            refillTime,
            prescriptionImageUrl,
            medicType,
            medicColor,
            status,
            deviceId,
            timeTableId,
            patientInfoId,
            medicineId,
            pharmacyId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PrescriptionCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (prescriptionCode != null ? "prescriptionCode=" + prescriptionCode + ", " : "") +
            (barCode != null ? "barCode=" + barCode + ", " : "") +
            (issueDate != null ? "issueDate=" + issueDate + ", " : "") +
            (usageDescription != null ? "usageDescription=" + usageDescription + ", " : "") +
            (importantInfo != null ? "importantInfo=" + importantInfo + ", " : "") +
            (qty != null ? "qty=" + qty + ", " : "") +
            (hasRefill != null ? "hasRefill=" + hasRefill + ", " : "") +
            (refillTime != null ? "refillTime=" + refillTime + ", " : "") +
            (prescriptionImageUrl != null ? "prescriptionImageUrl=" + prescriptionImageUrl + ", " : "") +
            (medicType != null ? "medicType=" + medicType + ", " : "") +
            (medicColor != null ? "medicColor=" + medicColor + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (deviceId != null ? "deviceId=" + deviceId + ", " : "") +
            (timeTableId != null ? "timeTableId=" + timeTableId + ", " : "") +
            (patientInfoId != null ? "patientInfoId=" + patientInfoId + ", " : "") +
            (medicineId != null ? "medicineId=" + medicineId + ", " : "") +
            (pharmacyId != null ? "pharmacyId=" + pharmacyId + ", " : "") +
            "}";
    }
}
