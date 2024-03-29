package com.example.krishbhatia.instagramclone.models;

public class UserAccountSettings {
    private String description;
    private String display_name;
    private long following;
    private long followers;
    private long posts;
    private String profile_photo;
    private String username;
    private String website;

    public UserAccountSettings(String description, String display_name, long following, long followers, long posts, String profile_photo, String username, String website) {
        this.description = description;
        this.display_name = display_name;
        this.following = following;
        this.followers = followers;
        this.posts = posts;
        this.profile_photo = profile_photo;
        this.username = username;
        this.website = website;
    }



    public UserAccountSettings() {
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public void setPosts(long posts) {
        this.posts = posts;
    }

    public void setProfile_photo(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {

        return description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public long getFollowing() {
        return following;
    }

    public long getFollowers() {
        return followers;
    }

    public long getPosts() {
        return posts;
    }

    public String getProfile_photo() {
        return profile_photo;
    }

    public String getUsername() {
        return username;
    }

    public String getWebsite() {
        return website;
    }
}
