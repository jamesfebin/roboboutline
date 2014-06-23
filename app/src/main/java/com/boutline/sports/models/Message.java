package com.boutline.sports.models;

public class Message {
	String messageId;
    String userPropic;
    String username;
    String senderId;
    String message;
    String unixtime;
    String conversationId;
    
    
    // Constructor
    
    public Message(String messageId, String userPropic, String username, String senderId, String message, String unixtime, String conversationId) {
      
    	this.messageId = messageId;
    	this.userPropic = userPropic;
    	this.username = username;
    	this.senderId = senderId;
    	this.message = message;
    	this.unixtime = unixtime;
    	this.conversationId = conversationId;    	
    }
    
    // Getters and Setters
    
    public String getConversationId() {
		return conversationId;
	}
    
    public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
    
    public String getMessage() {
		return message;
	}
    
    public void setMessage(String message) {
		this.message = message;
	}
    
    public String getMessageId() {
		return messageId;
	}
    
    public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
    
    public String getSenderId() {
		return senderId;
	}
    
    public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
    
    public String getUnixtime() {
		return unixtime;
	}
    
    public void setUnixtime(String unixtime) {
		this.unixtime = unixtime;
	}
    
    public String getUsername() {
		return username;
	}
    
    public void setUsername(String username) {
		this.username = username;
	}
    
    public String getUserPropic() {
		return userPropic;
	}
    
    public void setUserPropic(String userPropic) {
		this.userPropic = userPropic;
	}
    
}