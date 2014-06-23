package com.boutline.sports.models;

import java.util.ArrayList;

public class Conversation {
    String conversationId; 
    String conversationName;
    String tournamentName;
    ArrayList<String> members;
    String lastMessage;
    
    
    // Constructor
    
    public Conversation(String conversationId, String conversationName, String tournamentName, ArrayList<String> members, String lastMessage) {
       this.conversationId = conversationId;
       this.conversationName = conversationName;
       this.tournamentName = tournamentName;
       this.lastMessage = lastMessage;
       this.members = members;
    }
    
    // Getters and Setters
    public ArrayList<String> getMembers() {
		return members;
	}
    public void setMembers(ArrayList<String> members) {
		this.members = members;
	}
    
    public String getConversationId() {
		return conversationId;
	}
    
    public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
    
    public String getConversationName() {
		return conversationName;
	}
    
    public void setConversationName(String conversationName) {
		this.conversationName = conversationName;
	}
    
    public String getTournamentName() {
		return tournamentName;
	}
    
    public void setTournamentName(String tournamentName) {
		this.tournamentName = tournamentName;
	}
    
    public String getLastMessage() {
		return lastMessage;
	}
    
    public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}    
    
}