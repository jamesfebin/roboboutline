package com.boutline.sports.models;

public class Sport {
	
    String sportId; 
    String sportName;
    String sportDescription;
    Boolean sportFollow;
    
    
    // Constructor
    
    public Sport(String sportId, String sportName, String sportDescription, Boolean sportFollow) {
       this.sportId = sportId;
       this.sportName = sportName;
       this.sportFollow = sportFollow;
       this.sportDescription = sportDescription;      
    }
    
    // Getters and Setters
    
    public String getSportId() {
		return sportId;
	}
    
    public void setSportId(String sportId) {
		this.sportId = sportId;
	}
    public String getSportDescription() {
		return sportDescription;
	}
    
    public void setSportDescription(String sportDescription) {
		this.sportDescription = sportDescription;
	}
    
   public String getSportName() {
	return sportName;
   }
   
   public void setSportName(String sportName) {
	this.sportName = sportName;
   }
   
   public Boolean getSportFollow() {
	return sportFollow;
   }
   public void setSportFollow(Boolean sportFollow) {
	this.sportFollow = sportFollow;
   }
}