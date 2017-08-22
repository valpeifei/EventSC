package Managers;

import java.util.ArrayList;

import BaseClass.Event;
import BaseClass.User;
import Message.ProfileMessage;

public class ProfileManager {
	DatabaseManager dm;
	String username;
	User user;
	ArrayList<String> myEvents;
	ArrayList<String> pastEvents;
	ArrayList<String> upcomingEvents;
	ArrayList<Integer> myEventIDs;
	ArrayList<Integer> pastEventIDs;
	ArrayList<Integer> upcomingEventIDs;
	ArrayList<String> userInfo;

	public ProfileManager(String mUsername) {
		username = mUsername;
		dm = new DatabaseManager();
		user = dm.getUser(username);
		userInfo = getUserInfo();
		myEvents = getMyEvents();
		myEventIDs = getMyEventIDs();
		pastEvents = getPastEvents();
		pastEventIDs = getPastEventIDs();
		upcomingEvents = getUpcomingEvents();
		upcomingEventIDs = getUpcomingEventIDs();	 
	}
	public ProfileMessage getProfile(){
		ProfileMessage pm = new ProfileMessage(userInfo, myEvents, myEventIDs, pastEvents, pastEventIDs, upcomingEvents,
				upcomingEventIDs);
		System.out.println(username);
		return pm;
	}
	public ArrayList<String> getUserInfo() {
		ArrayList<String> mUserInfo = new ArrayList<>();
		String fname = user.getFname();
		mUserInfo.add(fname);
		String lname = user.getLname();
		mUserInfo.add(lname);
		mUserInfo.add(username);
		// String rating = user.getRating();
		// mUserInfo.add(rating);
		/*
		 * String pic = user.getPic(); mUserInfo.add(pic);
		 */
		return mUserInfo;
	}

	public ArrayList<String> getMyEvents() {
		ArrayList<String> myEventsTempString = new ArrayList<>();
		ArrayList<Event> myEventsTemp = dm.getEvents(username, true);
		for (int i = 0; i < myEventsTemp.size(); i++) {
			myEventsTempString.add(myEventsTemp.get(i).getTitle());
		}
		return myEventsTempString;
	}

	public ArrayList<Integer> getMyEventIDs() {
		ArrayList<Integer> myEventIDTemp = new ArrayList<>();
		ArrayList<Event> myEventsTemp = dm.getEvents(username, true);
		for (int i = 0; i < myEventsTemp.size(); i++) {
			myEventIDTemp.add(myEventsTemp.get(i).getEventID());
		}
		return myEventIDTemp;
	}

	public ArrayList<String> getPastEvents() {
		ArrayList<String> myEventsTempString = new ArrayList<>();
		ArrayList<Event> myEventsTemp = dm.getEvents(username, false);
		ArrayList<Event> myPastEvents = new ArrayList<Event>();
		for (int j = 0; j < myEventsTemp.size(); j++) {
			if (myEventsTemp.get(j).eventTime().equals("past")) {
				myPastEvents.add(myEventsTemp.get(j));
				myEventsTempString.add(myEventsTemp.get(j).getTitle());
			}
		}
		 
		return myEventsTempString;
	}

	public ArrayList<Integer> getPastEventIDs() {
		ArrayList<Integer> myEventIDTemp = new ArrayList<>();
		ArrayList<Event> myEventsTemp = dm.getEvents(username, false);
		for (int i = 0; i < myEventsTemp.size(); i++) {
			if (myEventsTemp.get(i).eventTime().equals("past")) {
				myEventIDTemp.add(myEventsTemp.get(i).getEventID());
			}
		}
		return myEventIDTemp;
	}

	public ArrayList<String> getUpcomingEvents() {
		ArrayList<String> myEventsTempString = new ArrayList<String>();
		ArrayList<Event> myEventsTemp = dm.getEvents(username, false);
		ArrayList<Event> myUpcomingEvents = new ArrayList<Event>();
		for (int j = 0; j < myEventsTemp.size(); j++) {
			if (myEventsTemp.get(j).eventTime().equals("upcoming")) {
				myUpcomingEvents.add(myEventsTemp.get(j));
				myEventsTempString.add(myEventsTemp.get(j).getTitle());
			}
		}
		 
		return myEventsTempString;
	}

	public ArrayList<Integer> getUpcomingEventIDs() {
		ArrayList<Integer> myEventIDTemp = new ArrayList<>();
		ArrayList<Event> myEventsTemp = dm.getEvents(username, false);
		for (int i = 0; i < myEventsTemp.size(); i++) {
			if (myEventsTemp.get(i).eventTime().equals("upcoming")) {
				myEventIDTemp.add(myEventsTemp.get(i).getEventID());
			}
		}
		return myEventIDTemp;
	}

}
