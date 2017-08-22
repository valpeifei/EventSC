package Message;

import java.util.ArrayList;

import org.apache.catalina.User;

import BaseClass.Event;

public class SearchMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<String> eventTitles;
	ArrayList<String> posterNames;
	ArrayList<Integer> eventIDs;
	public SearchMessage(ArrayList<Event> events, ArrayList<BaseClass.User> users){
		eventTitles = new ArrayList<String> ();
		posterNames = new ArrayList<String> ();
		eventIDs = new ArrayList<Integer> ();
		for(int i = 0; i<events.size(); i++){
			eventTitles.add(events.get(i).getTitle());
			posterNames.add(users.get(i).getFname()+" "+users.get(i).getLname());
			eventIDs.add(events.get(i).getEventID());
		}
		this.action = "SearchMessage";
	}
}
