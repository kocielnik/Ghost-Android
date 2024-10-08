package me.vickychijwani.spectre.model.entity;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@io.realm.annotations.RealmClass
public class Setting implements io.realm.RealmModel {

    @io.realm.annotations.PrimaryKey
    private String id;

    @io.realm.annotations.Required
    private String key;

    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Setting[" + this.key + " = " + this.value + "]";
    }

}
