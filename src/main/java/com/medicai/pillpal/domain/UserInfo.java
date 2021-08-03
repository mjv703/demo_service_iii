package com.medicai.pillpal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserInfo.
 */
@Entity
@Table(name = "user_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    //@SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "user_code", unique = true)
    private String userCode;

    @Column(name = "phone_number_1")
    private String phoneNumber1;

    @Column(name = "phone_number_2")
    private String phoneNumber2;

    @Column(name = "address")
    private String address;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "devices", "prescriptions", "userInfo" }, allowSetters = true)
    private Set<PatientInfo> patientInfos = new HashSet<>();

    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userInfo" }, allowSetters = true)
    private Set<MobileDevice> mobileDevices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserInfo id(Long id) {
        this.id = id;
        return this;
    }

    public String getUserCode() {
        return this.userCode;
    }

    public UserInfo userCode(String userCode) {
        this.userCode = userCode;
        return this;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPhoneNumber1() {
        return this.phoneNumber1;
    }

    public UserInfo phoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
        return this;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return this.phoneNumber2;
    }

    public UserInfo phoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
        return this;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getAddress() {
        return this.address;
    }

    public UserInfo address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User getUser() {
        return this.user;
    }

    public UserInfo user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<PatientInfo> getPatientInfos() {
        return this.patientInfos;
    }

    public UserInfo patientInfos(Set<PatientInfo> patientInfos) {
        this.setPatientInfos(patientInfos);
        return this;
    }

    public UserInfo addPatientInfo(PatientInfo patientInfo) {
        this.patientInfos.add(patientInfo);
        patientInfo.setUserInfo(this);
        return this;
    }

    public UserInfo removePatientInfo(PatientInfo patientInfo) {
        this.patientInfos.remove(patientInfo);
        patientInfo.setUserInfo(null);
        return this;
    }

    public void setPatientInfos(Set<PatientInfo> patientInfos) {
        if (this.patientInfos != null) {
            this.patientInfos.forEach(i -> i.setUserInfo(null));
        }
        if (patientInfos != null) {
            patientInfos.forEach(i -> i.setUserInfo(this));
        }
        this.patientInfos = patientInfos;
    }

    public Set<MobileDevice> getMobileDevices() {
        return this.mobileDevices;
    }

    public UserInfo mobileDevices(Set<MobileDevice> mobileDevices) {
        this.setMobileDevices(mobileDevices);
        return this;
    }

    public UserInfo addMobileDevice(MobileDevice mobileDevice) {
        this.mobileDevices.add(mobileDevice);
        mobileDevice.setUserInfo(this);
        return this;
    }

    public UserInfo removeMobileDevice(MobileDevice mobileDevice) {
        this.mobileDevices.remove(mobileDevice);
        mobileDevice.setUserInfo(null);
        return this;
    }

    public void setMobileDevices(Set<MobileDevice> mobileDevices) {
        if (this.mobileDevices != null) {
            this.mobileDevices.forEach(i -> i.setUserInfo(null));
        }
        if (mobileDevices != null) {
            mobileDevices.forEach(i -> i.setUserInfo(this));
        }
        this.mobileDevices = mobileDevices;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserInfo)) {
            return false;
        }
        return id != null && id.equals(((UserInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + getId() +
            ", userCode='" + getUserCode() + "'" +
            ", phoneNumber1='" + getPhoneNumber1() + "'" +
            ", phoneNumber2='" + getPhoneNumber2() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
