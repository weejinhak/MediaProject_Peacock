package com.peac.cock.peacock_project.projectDto;

import java.io.Serializable;

public class UserDto implements Serializable {

    String email;
    String name;
    String birthday;
    String gender;
    String job;
    String budget;


    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", job='" + job + '\'' +
                ", budget='" + budget + '\'' +
                '}';
    }
}
