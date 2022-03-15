package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Users extends AbstractEntity {

    @NotEmpty
    @NotBlank
    private String firstName = "";

    @NotEmpty
    @NotBlank
    private String lastName = "";

    @NotEmpty
    @NotBlank
    private String loginUser;

    @NotEmpty
    @NotBlank
    private String passwordUser;

    @NotNull
    @NotBlank
    private String roleSystem;

    @NotNull
    @ManyToOne
    private Department department;

    @NotEmpty
    @NotBlank
    private String post;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    @NotNull
    private boolean status = true;

    @NotNull
    @ManyToOne
    private Location location;

    @Override
    public String toString() {
        return firstName + " " + lastName;
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

    public String getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(String login) {
        this.loginUser = login;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String password) {
        this.passwordUser = password;
    }

    public String getRoleSystem() {
        return roleSystem;
    }

    public void setRoleSystem(String roleSystem) {
        this.roleSystem = roleSystem;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return firstName + " " + lastName;
    }
}
