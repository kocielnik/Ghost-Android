package me.vickychijwani.spectre.model.entity;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import io.realm.annotations.Required;

@SuppressWarnings("unused")
@io.realm.annotations.RealmClass
public class User implements io.realm.RealmModel {

    @io.realm.annotations.PrimaryKey
    private String id;

    @io.realm.annotations.Required
    private String name;

    @io.realm.annotations.Required
    private String slug;

    @io.realm.annotations.Required
    private String email;

    private String profileImage;
    private String bio;

    private io.realm.RealmList<Role> roles;

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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public io.realm.RealmList<Role> getRoles() {
        return roles;
    }

    public void setRoles(io.realm.RealmList<Role> roles) {
        this.roles = roles;
    }

    // the way Ghost permissions work is, authors and contributors can only see and edit their own
    // posts, while all other roles can see and edit everybody's posts
    public boolean isOnlyAuthorOrContributor() {
        // a user can have multiple roles, so they should be restricted to author/contributor
        // permissions only if ALL their roles are "Author" or "Contributor"
        boolean onlyAuthorOrContributor = true;
        for (Role role : roles) {
            if (!"author".equalsIgnoreCase(role.getName())
                    && !"contributor".equalsIgnoreCase(role.getName())) {
                onlyAuthorOrContributor = false;
            }
        }
        return onlyAuthorOrContributor;
    }

}
