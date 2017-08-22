package Managers;

import java.util.ArrayList;

import BaseClass.Event;
import BaseClass.User;
import Message.MapMessage;
import Message.EventMessage;

public class MapManager {
	DatabaseManager dm;
	String currUser;
	ArrayList<Event> events;

	public MapManager(String mUser) { // we prolly don't need the user object
		currUser = mUser;
		dm = new DatabaseManager();
	}

	public MapMessage populateMap() {
		events = (ArrayList<Event>) dm.requestAllEvents();
		MapMessage mm = new MapMessage(events);
		return mm;
	}

	public EventMessage getEvent(int eventID) { // get a particular event when the user clicks an event
		Event event = dm.getEventObject(eventID);
		User user = dm.getUser(event.getPoster());
		EventMessage em = new EventMessage(event, user);
		return em;
	}
}
