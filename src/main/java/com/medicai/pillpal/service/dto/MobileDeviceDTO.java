package com.medicai.pillpal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.medicai.pillpal.domain.MobileDevice} entity.
 */
public class MobileDeviceDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String os;

    private String deviceId;

    private UserInfoDTO userInfo;

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

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public UserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MobileDeviceDTO)) {
            return false;
        }

        MobileDeviceDTO mobileDeviceDTO = (MobileDeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, mobileDeviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MobileDeviceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", os='" + getOs() + "'" +
            ", deviceId='" + getDeviceId() + "'" +
            ", userInfo=" + getUserInfo() +
            "}";
    }
}
