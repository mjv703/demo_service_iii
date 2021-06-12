package com.medicai.pillpal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.medicai.pillpal.domain.enumeration.BloodType;
import com.medicai.pillpal.domain.enumeration.MaritalStatusType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PatientInfo.
 */
@Entity
@Table(name = "patient_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PatientInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private Instant birthDate;

    @Column(name = "id_no")
    private String idNo;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number_1")
    private String phoneNumber1;

    @Column(name = "phone_number_2")
    private String phoneNumber2;

    @Column(name = "email")
    private String email;

    @Column(name = "height")
    private Integer height;

    @Column(name = "age")
    private Integer age;

    @Column(name = "weight")
    private Integer weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type")
    private BloodType bloodType;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatusType maritalStatus;

    @Column(name = "relationship_with_user")
    private String relationshipWithUser;

    @Column(name = "patient_image_url")
    private String patientImageUrl;

    @OneToMany(mappedBy = "patientInfo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patientInfo", "prescription" }, allowSetters = true)
    private Set<Device> devices = new HashSet<>();

    @OneToMany(mappedBy = "patientInfo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "devices", "timeTables", "patientInfo", "medicine", "pharmacy" }, allowSetters = true)
    private Set<Prescription> prescriptions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "patientInfos", "mobileDevices" }, allowSetters = true)
    private UserInfo userInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PatientInfo id(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public PatientInfo firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public PatientInfo lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Instant getBirthDate() {
        return this.birthDate;
    }

    public PatientInfo birthDate(Instant birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getIdNo() {
        return this.idNo;
    }

    public PatientInfo idNo(String idNo) {
        this.idNo = idNo;
        return this;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAddress() {
        return this.address;
    }

    public PatientInfo address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber1() {
        return this.phoneNumber1;
    }

    public PatientInfo phoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
        return this;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return this.phoneNumber2;
    }

    public PatientInfo phoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
        return this;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getEmail() {
        return this.email;
    }

    public PatientInfo email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getHeight() {
        return this.height;
    }

    public PatientInfo height(Integer height) {
        this.height = height;
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getAge() {
        return this.age;
    }

    public PatientInfo age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWeight() {
        return this.weight;
    }

    public PatientInfo weight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public BloodType getBloodType() {
        return this.bloodType;
    }

    public PatientInfo bloodType(BloodType bloodType) {
        this.bloodType = bloodType;
        return this;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public MaritalStatusType getMaritalStatus() {
        return this.maritalStatus;
    }

    public PatientInfo maritalStatus(MaritalStatusType maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public void setMaritalStatus(MaritalStatusType maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getRelationshipWithUser() {
        return this.relationshipWithUser;
    }

    public PatientInfo relationshipWithUser(String relationshipWithUser) {
        this.relationshipWithUser = relationshipWithUser;
        return this;
    }

    public void setRelationshipWithUser(String relationshipWithUser) {
        this.relationshipWithUser = relationshipWithUser;
    }

    public String getPatientImageUrl() {
        return this.patientImageUrl;
    }

    public PatientInfo patientImageUrl(String patientImageUrl) {
        this.patientImageUrl = patientImageUrl;
        return this;
    }

    public void setPatientImageUrl(String patientImageUrl) {
        this.patientImageUrl = patientImageUrl;
    }

    public Set<Device> getDevices() {
        return this.devices;
    }

    public PatientInfo devices(Set<Device> devices) {
        this.setDevices(devices);
        return this;
    }

    public PatientInfo addDevice(Device device) {
        this.devices.add(device);
        device.setPatientInfo(this);
        return this;
    }

    public PatientInfo removeDevice(Device device) {
        this.devices.remove(device);
        device.setPatientInfo(null);
        return this;
    }

    public void setDevices(Set<Device> devices) {
        if (this.devices != null) {
            this.devices.forEach(i -> i.setPatientInfo(null));
        }
        if (devices != null) {
            devices.forEach(i -> i.setPatientInfo(this));
        }
        this.devices = devices;
    }

    public Set<Prescription> getPrescriptions() {
        return this.prescriptions;
    }

    public PatientInfo prescriptions(Set<Prescription> prescriptions) {
        this.setPrescriptions(prescriptions);
        return this;
    }

    public PatientInfo addPrescription(Prescription prescription) {
        this.prescriptions.add(prescription);
        prescription.setPatientInfo(this);
        return this;
    }

    public PatientInfo removePrescription(Prescription prescription) {
        this.prescriptions.remove(prescription);
        prescription.setPatientInfo(null);
        return this;
    }

    public void setPrescriptions(Set<Prescription> prescriptions) {
        if (this.prescriptions != null) {
            this.prescriptions.forEach(i -> i.setPatientInfo(null));
        }
        if (prescriptions != null) {
            prescriptions.forEach(i -> i.setPatientInfo(this));
        }
        this.prescriptions = prescriptions;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public PatientInfo userInfo(UserInfo userInfo) {
        this.setUserInfo(userInfo);
        return this;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientInfo)) {
            return false;
        }
        return id != null && id.equals(((PatientInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientInfo{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", idNo='" + getIdNo() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber1='" + getPhoneNumber1() + "'" +
            ", phoneNumber2='" + getPhoneNumber2() + "'" +
            ", email='" + getEmail() + "'" +
            ", height=" + getHeight() +
            ", age=" + getAge() +
            ", weight=" + getWeight() +
            ", bloodType='" + getBloodType() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", relationshipWithUser='" + getRelationshipWithUser() + "'" +
            ", patientImageUrl='" + getPatientImageUrl() + "'" +
            "}";
    }
}
