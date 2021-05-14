package com.medicai.pillpal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MobileDevice.
 */
@Entity
@Table(name = "mobile_device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MobileDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "os", nullable = false)
    private String os;

    @Column(name = "device_id")
    private String deviceId;

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

    public MobileDevice id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public MobileDevice name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOs() {
        return this.os;
    }

    public MobileDevice os(String os) {
        this.os = os;
        return this;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public MobileDevice deviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public MobileDevice userInfo(UserInfo userInfo) {
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
        if (!(o instanceof MobileDevice)) {
            return false;
        }
        return id != null && id.equals(((MobileDevice) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MobileDevice{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", os='" + getOs() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            "}";
    }
}
