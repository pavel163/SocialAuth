package com.ebr163.socialauth.instagram.model;

import com.google.gson.annotations.SerializedName;

public class InstagramProfile {

    @SerializedName("id")
    private long id;

    @SerializedName("username")
    private String username;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("profile_picture")
    private String profilePicture;

    @SerializedName("bio")
    private String bio;

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getBio() {
        return bio;
    }

    @Override
    public String toString() {
        return "InstagramProfile{" +
                "name='" + username + '\'' +
                ", avatar='" + profilePicture + '\'' +
                ", fullName='" + fullName + '\'' +
                ", bio='" + fullName + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
