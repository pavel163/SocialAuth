package com.ebr163.socialauth.google;

/**
 * Created by Ergashev on 04.01.2017.
 */
public class GooglePlusProfile {

    private String name;
    private String token;
    private String avatar;
    private String email;
    private String id;

    public GooglePlusProfile(String name, String token, String avatar, String email, String id) {
        this.name = name;
        this.token = token;
        this.avatar = avatar;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }

    public String getAvatar() {
        if (avatar != null) {
            return avatar.replace("s96-c", "s512-c");
        } else {
            return null;
        }
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GooglePlusProfile{" +
                "name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
