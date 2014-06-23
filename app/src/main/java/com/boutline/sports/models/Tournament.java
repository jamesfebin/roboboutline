package com.boutline.sports.models;

public class Tournament {
    String tournamentId; 
    String tournamentName;
    String tournamentStarttime;
    String tournamentEndtime;
    String tournamentHashtag;
    int tournamentPriority;
    boolean tournamentFollow;
    
    // Constructor
    
    public Tournament(String tournamentId, String tournamentName, String tournamentStarttime, String tournamentEndtime, String tournamentHashtag, int tournamentPriority, boolean tournamentFollow) {
       this.tournamentId = tournamentId;
       this.tournamentName = tournamentName;
       this.tournamentStarttime = tournamentStarttime;
       this.tournamentEndtime = tournamentEndtime;
       this.tournamentHashtag = tournamentHashtag;
       this.tournamentPriority = tournamentPriority;
       this.tournamentFollow = tournamentFollow;
    }
    
    // Getters and Setters
    
    public String getTournamentEndtime() {
		return tournamentEndtime;
	}
    
    public void setTournamentEndtime(String tournamentEndtime) {
		this.tournamentEndtime = tournamentEndtime;
	}
    
    public String getTournamentHashtag() {
		return tournamentHashtag;
	}
    
    public void setTournamentHashtag(String tournamentHashtag) {
		this.tournamentHashtag = tournamentHashtag;
	}
    
    public String getTournamentId() {
		return tournamentId;
	}
    
    public void setTournamentId(String tournamentId) {
		this.tournamentId = tournamentId;
	}
    
    public String getTournamentName() {
		return tournamentName;
	}
    
    public void setTournamentName(String tournamentName) {
		this.tournamentName = tournamentName;
	}
    
    public int getTournamentPriority() {
		return tournamentPriority;
	}
    
    public void setTournamentPriority(int tournamentPriority) {
		this.tournamentPriority = tournamentPriority;
	}
    
    public String getTournamentStarttime() {
		return tournamentStarttime;
	}
    
    public void setTournamentStarttime(String tournamentStarttime) {
		this.tournamentStarttime = tournamentStarttime;
	}
    
    public boolean getTournamentFollow(){
    	return tournamentFollow;
    }
 
    public void setTournamentFollow(boolean tournamentFollow){
    	this.tournamentFollow = tournamentFollow;
    }

}
