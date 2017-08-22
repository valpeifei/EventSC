package Managers;

import BaseClass.Event;
import BaseClass.User;

public class AttendManager {
	User user;
	Event event;
	DatabaseManager dm;

	public AttendManager(String username, int eventID) {
		this.dm = new DatabaseManager();
		this.user = dm.getUser(username);
		this.event = dm.getEventObject(eventID);
	}

	public void attend() {
		dm.attendEvent(user, event);
	}

}
