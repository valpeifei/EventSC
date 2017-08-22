package Message;

import java.util.ArrayList;

public class ProfileMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String fname;
	String lname;
	public String username;
	// int selfRating;
	ArrayList<String> upcomingEvents;
	ArrayList<Integer> upcomingEventIDs;
	ArrayList<String> previousEvents;
	ArrayList<Integer> previousEventIDs;
	ArrayList<String> myEvents; // events the user created
	ArrayList<Integer> myEventIDs;

	public ProfileMessage(ArrayList<String> userInfo, ArrayList<String> my, ArrayList<Integer> myIDs,
			ArrayList<String> previous, ArrayList<Integer> previousIDs, ArrayList<String> upcoming,
			ArrayList<Integer> upcomingIDs) {
		fname = userInfo.get(0);
		lname = userInfo.get(1);
		username = userInfo.get(2);
		// selfRating = userInfo.get(2);
		upcomingEvents = upcoming;
		upcomingEventIDs = upcomingIDs;
		previousEvents = previous;
		previousEventIDs = previousIDs;
		myEvents = my;
		myEventIDs = myIDs;
		this.action = "ProfileMesssage";
	}

}
