package com.medicai.pillpal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medicai.pillpal.domain.enumeration.ColorContentType;
import com.medicai.pillpal.domain.enumeration.MedicType;
import com.medicai.pillpal.domain.enumeration.PrescriptionStatusType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prescription.
 */
@Entity
@Table(name = "prescription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Prescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "prescription_code")
    private String prescriptionCode;

    @Column(name = "bar_code")
    private String barCode;

    @NotNull
    @Column(name = "issue_date", nullable = false)
    private Instant issueDate;

    @NotNull
    @Column(name = "usage_description", nullable = false)
    private String usageDescription;

    @NotNull
    @Column(name = "important_info", nullable = false)
    private String importantInfo;

    @NotNull
    @Column(name = "qty", nullable = false)
    private Integer qty;

    @NotNull
    @Column(name = "has_refill", nullable = false)
    private Boolean hasRefill;

    @Column(name = "refill_time")
    private Instant refillTime;

    @Column(name = "prescription_image_url")
    private String prescriptionImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "medic_type")
    private MedicType medicType;

    @Enumerated(EnumType.STRING)
    @Column(name = "medic_color")
    private ColorContentType medicColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PrescriptionStatusType status;

    @OneToMany(mappedBy = "prescription")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patientInfo", "prescription" }, allowSetters = true)
    private Set<Device> devices = new HashSet<>();

    @OneToMany(mappedBy = "prescription")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prescription" }, allowSetters = true)
    private Set<TimeTable> timeTables = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "devices", "prescriptions", "userInfo" }, allowSetters = true)
    private PatientInfo patientInfo;

    @ManyToOne
    @JsonIgnoreProperties(value = { "sideEffects", "prescriptions" }, allowSetters = true)
    private Medicine medicine;

    @ManyToOne
    @JsonIgnoreProperties(value = { "prescriptions" }, allowSetters = true)
    private Pharmacy pharmacy;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prescription id(Long id) {
        this.id = id;
        return this;
    }

    public String getPrescriptionCode() {
        return this.prescriptionCode;
    }

    public Prescription prescriptionCode(String prescriptionCode) {
        this.prescriptionCode = prescriptionCode;
        return this;
    }

    public void setPrescriptionCode(String prescriptionCode) {
        this.prescriptionCode = prescriptionCode;
    }

    public String getBarCode() {
        return this.barCode;
    }

    public Prescription barCode(String barCode) {
        this.barCode = barCode;
        return this;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Instant getIssueDate() {
        return this.issueDate;
    }

    public Prescription issueDate(Instant issueDate) {
        this.issueDate = issueDate;
        return this;
    }

    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public String getUsageDescription() {
        return this.usageDescription;
    }

    public Prescription usageDescription(String usageDescription) {
        this.usageDescription = usageDescription;
        return this;
    }

    public void setUsageDescription(String usageDescription) {
        this.usageDescription = usageDescription;
    }

    public String getImportantInfo() {
        return this.importantInfo;
    }

    public Prescription importantInfo(String importantInfo) {
        this.importantInfo = importantInfo;
        return this;
    }

    public void setImportantInfo(String importantInfo) {
        this.importantInfo = importantInfo;
    }

    public Integer getQty() {
        return this.qty;
    }

    public Prescription qty(Integer qty) {
        this.qty = qty;
        return this;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Boolean getHasRefill() {
        return this.hasRefill;
    }

    public Prescription hasRefill(Boolean hasRefill) {
        this.hasRefill = hasRefill;
        return this;
    }

    public void setHasRefill(Boolean hasRefill) {
        this.hasRefill = hasRefill;
    }

    public Instant getRefillTime() {
        return this.refillTime;
    }

    public Prescription refillTime(Instant refillTime) {
        this.refillTime = refillTime;
        return this;
    }

    public void setRefillTime(Instant refillTime) {
        this.refillTime = refillTime;
    }

    public String getPrescriptionImageUrl() {
        return this.prescriptionImageUrl;
    }

    public Prescription prescriptionImageUrl(String prescriptionImageUrl) {
        this.prescriptionImageUrl = prescriptionImageUrl;
        return this;
    }

    public void setPrescriptionImageUrl(String prescriptionImageUrl) {
        this.prescriptionImageUrl = prescriptionImageUrl;
    }

    public MedicType getMedicType() {
        return this.medicType;
    }

    public Prescription medicType(MedicType medicType) {
        this.medicType = medicType;
        return this;
    }

    public void setMedicType(MedicType medicType) {
        this.medicType = medicType;
    }

    public ColorContentType getMedicColor() {
        return this.medicColor;
    }

    public Prescription medicColor(ColorContentType medicColor) {
        this.medicColor = medicColor;
        return this;
    }

    public void setMedicColor(ColorContentType medicColor) {
        this.medicColor = medicColor;
    }

    public PrescriptionStatusType getStatus() {
        return this.status;
    }

    public Prescription status(PrescriptionStatusType status) {
        this.status = status;
        return this;
    }

    public void setStatus(PrescriptionStatusType status) {
        this.status = status;
    }

    public Set<Device> getDevices() {
        return this.devices;
    }

    public Prescription devices(Set<Device> devices) {
        this.setDevices(devices);
        return this;
    }

    public Prescription addDevice(Device device) {
        this.devices.add(device);
        device.setPrescription(this);
        return this;
    }

    public Prescription removeDevice(Device device) {
        this.devices.remove(device);
        device.setPrescription(null);
        return this;
    }

    public void setDevices(Set<Device> devices) {
        if (this.devices != null) {
            this.devices.forEach(i -> i.setPrescription(null));
        }
        if (devices != null) {
            devices.forEach(i -> i.setPrescription(this));
        }
        this.devices = devices;
    }

    public Set<TimeTable> getTimeTables() {
        return this.timeTables;
    }

    public Prescription timeTables(Set<TimeTable> timeTables) {
        this.setTimeTables(timeTables);
        return this;
    }

    public Prescription addTimeTable(TimeTable timeTable) {
        this.timeTables.add(timeTable);
        timeTable.setPrescription(this);
        return this;
    }

    public Prescription removeTimeTable(TimeTable timeTable) {
        this.timeTables.remove(timeTable);
        timeTable.setPrescription(null);
        return this;
    }

    public void setTimeTables(Set<TimeTable> timeTables) {
        if (this.timeTables != null) {
            this.timeTables.forEach(i -> i.setPrescription(null));
        }
        if (timeTables != null) {
            timeTables.forEach(i -> i.setPrescription(this));
        }
        this.timeTables = timeTables;
    }

    public PatientInfo getPatientInfo() {
        return this.patientInfo;
    }

    public Prescription patientInfo(PatientInfo patientInfo) {
        this.setPatientInfo(patientInfo);
        return this;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

    public Medicine getMedicine() {
        return this.medicine;
    }

    public Prescription medicine(Medicine medicine) {
        this.setMedicine(medicine);
        return this;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public Pharmacy getPharmacy() {
        return this.pharmacy;
    }

    public Prescription pharmacy(Pharmacy pharmacy) {
        this.setPharmacy(pharmacy);
        return this;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prescription)) {
            return false;
        }
        return id != null && id.equals(((Prescription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prescription{" +
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
            "}";
    }
}
