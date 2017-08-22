package Message;

import java.util.ArrayList;

import BaseClass.Event;
import BaseClass.User;

public class UserMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String userName, userID; 
	int selfRating;
	ArrayList<String> previousEvents;
	ArrayList<String> previousEventIDs;
	
	public UserMessage(ArrayList<Event> previous, User user){
		previousEvents = new ArrayList<String>();
		previousEventIDs = new ArrayList<String>();
		this.action = "UserMesssage";
	}
	
}
