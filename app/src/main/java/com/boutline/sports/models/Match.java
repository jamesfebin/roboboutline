package com.boutline.sports.models;

public class Match {
	
    String matchId; 
    String matchName;
    String matchStartTime;
    String matchEndtime;
    String matchVenue;
    String matchShortName;
    String matchCity;
    String matchPriority;
    String matchTournamentId;
    
    
    // Constructor
    
    public Match(String matchId, String matchName, String matchStartTime, String matchEndTime, String matchVenue, String matchShortName, String matchCity, String matchPriority, String matchTournamentId) {
       this.matchId = matchId;
       this.matchName = matchName;
       this.matchStartTime = matchStartTime;
       this.matchEndtime = matchEndTime;
       this.matchVenue = matchVenue;
       this.matchShortName = matchShortName;
       this.matchCity = matchCity;
       this.matchPriority = matchPriority;
       this.matchTournamentId = matchTournamentId;
    }
    
    // Getters and Setters
    
    
    public String getMatchId() {
		return matchId;
	}
    
    public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
    
    public String getMatchName() {
		return matchName;
	}
    
    public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
    
    public String getMatchStartTime() {
		return matchStartTime;
	}
    
    public void setMatchStartTime(String matchStartTime) {
		this.matchStartTime = matchStartTime;
	}
    
    public String getMatchEndtime() {
		return matchEndtime;
	}
    
    public void setMatchEndtime(String matchEndtime) {
		this.matchEndtime = matchEndtime;
	}
    
    public String getMatchShortName() {
		return matchShortName;
	}
    
    public void setMatchShortName(String matchShortName) {
		this.matchShortName = matchShortName;
	}
    
    public String getMatchCity() {
		return matchCity;
	}
    
    public void setMatchCity(String matchCity) {
		this.matchCity = matchCity;
	}
    
    public String getMatchPriority() {
		return matchPriority;
	}
    
    public void setMatchPriority(String matchPriority) {
		this.matchPriority = matchPriority;
	}
    
    public String getMatchTournamentId() {
		return matchTournamentId;
	}
    
    public void setMatchTournamentId(String matchTournamentId) {
		this.matchTournamentId = matchTournamentId;
	}
    
    public String getMatchVenue() {
		return matchVenue;
	}
    
    public void setMatchVenue(String matchVenue) {
		this.matchVenue = matchVenue;
	}
}