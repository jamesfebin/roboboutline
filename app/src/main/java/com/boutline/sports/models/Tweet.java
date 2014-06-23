package com.boutline.sports.models;

public class Tweet {
    public String tweetId; // ID given by MONGO 
    public String tweetUsername;
    public String tweetPropicUrl;
    public String tweetHandle;
    public String tweetMessage;
    public String tweetUnixtime;
    public String tweetPhotoUrl;
    public String tweetStatusId; // ID given by Twitter
    
    // Constructor
    
    public Tweet(String tweetId, String tweetPhotoUrl, String tweetUsername, String tweetPropicUrl, String tweetHandle, String tweetMessage, String tweetUnixtime, String tweetStatusId) {
       this.tweetHandle = tweetHandle;
       this.tweetId = tweetId;
       this.tweetMessage = tweetMessage;
       this.tweetPropicUrl = tweetPropicUrl;
       this.tweetStatusId = tweetStatusId;
       this.tweetUnixtime = tweetUnixtime;
       this.tweetUsername = tweetUsername;
       this.tweetPhotoUrl = tweetPhotoUrl;
    }
    
    // Getters and Setters
    
    public String getTweetHandle() {
		return tweetHandle;
	}
    
    public void setTweetHandle(String tweetHandle) {
		this.tweetHandle = tweetHandle;
	}
    
    public String getTweetPhotoUrl() {
		return tweetPhotoUrl;
	}
    
    public void setTweetPhotoUrl(String tweetPhotoUrl) {
		this.tweetPhotoUrl = tweetPhotoUrl;
	}
    
    public String getTweetId() {
		return tweetId;
	}
    
    public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}
    
    public String getTweetUsername() {
		return tweetUsername;
	}
    
    public void setTweetUsername(String tweetUsername) {
		this.tweetUsername = tweetUsername;
	}
    
    public String getTweetPropicUrl() {
		return tweetPropicUrl;
	}
    
    public void setTweetPropicUrl(String tweetPropicUrl) {
		this.tweetPropicUrl = tweetPropicUrl;
	}
    
    public String getTweetMessage() {
		return tweetMessage;
	}
    
    public void setTweetMessage(String tweetMessage) {
		this.tweetMessage = tweetMessage;
	}
    
    public void setTweetStatusId(String tweetStatusId) {
		this.tweetStatusId = tweetStatusId;
	}
    
    public String getTweetStatusId() {
		return tweetStatusId;
	}
    
    public String getTweetUnixtime() {
		return tweetUnixtime;
	}
    
    public void setTweetUnixtime(String tweetUnixtime) {
		this.tweetUnixtime = tweetUnixtime;
	}
}