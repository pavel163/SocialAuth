package com.ebr163.socialauth.instagram.rest;

public final class InstagramConfig {
    private static InstagramConfig instance;

    private InstagramConfig() {
    }

    public static InstagramConfig getInstance() {
        if (instance == null) {
            instance = new InstagramConfig();
        }
        return instance;
    }

    String redirectUri;
    String clientId;

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAuthorizationUrl() {
        if (redirectUri == null || clientId == null) {
            throw new NullPointerException("Set redirectUri and clientId!");
        }
        return "https://api.instagram.com/oauth/authorize/?"
                + "client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=token";
    }

    public String getLogOutUrl() {
        return "https://instagram.com/accounts/logout/";
    }
}
