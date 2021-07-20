package com.medicai.pillpal.service.dto;

import com.medicai.pillpal.domain.enumeration.BloodType;
import com.medicai.pillpal.domain.enumeration.MaritalStatusType;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the {@link com.medicai.pillpal.domain.PatientInfo} entity.
 */
public class PatientInfoDTO implements Serializable {

    private Long id;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private LocalDate birthDate;

    private String idNo;

    private String address;

    private String phoneNumber1;

    private String phoneNumber2;

    private String email;

    private Integer height;

    private Integer age;

    private Integer weight;

    private BloodType bloodType;

    private MaritalStatusType maritalStatus;

    private String relationshipWithUser;

    private String patientImageUrl;

    private UserInfoDTO userInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public MaritalStatusType getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatusType maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getRelationshipWithUser() {
        return relationshipWithUser;
    }

    public void setRelationshipWithUser(String relationshipWithUser) {
        this.relationshipWithUser = relationshipWithUser;
    }

    public String getPatientImageUrl() {
        return patientImageUrl;
    }

    public void setPatientImageUrl(String patientImageUrl) {
        this.patientImageUrl = patientImageUrl;
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
        if (!(o instanceof PatientInfoDTO)) {
            return false;
        }

        PatientInfoDTO patientInfoDTO = (PatientInfoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patientInfoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientInfoDTO{" +
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
            ", userInfo=" + getUserInfo() +
            "}";
    }
}
