package me.vickychijwani.spectre.model.entity;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@io.realm.annotations.RealmClass
public class Role implements io.realm.RealmModel {

    @io.realm.annotations.PrimaryKey
    private String id;

    @io.realm.annotations.Required
    private String name;

    private String description;

    // accessors
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
