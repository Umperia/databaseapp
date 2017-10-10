package com.company.ump.databaseapp.profile;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProfileItem extends RealmObject implements Profile {
    @PrimaryKey
    private String name;
    private String email;
    private String phone;
    private String path;

    public ProfileItem() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return name + "\n" + email + "\n" + phone;
    }
}
