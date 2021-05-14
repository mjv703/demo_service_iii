package com.medicai.pillpal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.medicai.pillpal.domain.UserInfo} entity.
 */
public class UserInfoDTO implements Serializable {

    private Long id;

    private String userCode;

    private String phoneNumber1;

    private String phoneNumber2;

    private String address;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserInfoDTO)) {
            return false;
        }

        UserInfoDTO userInfoDTO = (UserInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserInfoDTO{" +
            "id=" + getId() +
            ", userCode='" + getUserCode() + "'" +
            ", phoneNumber1='" + getPhoneNumber1() + "'" +
            ", phoneNumber2='" + getPhoneNumber2() + "'" +
            ", address='" + getAddress() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
