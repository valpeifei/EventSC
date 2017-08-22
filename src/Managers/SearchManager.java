package Managers;

import java.util.ArrayList;

import BaseClass.Event;
import BaseClass.User;
import Message.SearchMessage;

public class SearchManager {
	User user;
	String searchParameter;
	String searchType;
	DatabaseManager dm;
	ArrayList<Event> resultEvents;
	ArrayList<User> resultUsers;

	public SearchManager(String searchType, String searchParameter) {
		this.searchParameter = searchParameter;
		this.searchType = searchType;
		dm = new DatabaseManager();
		resultEvents = new ArrayList<Event>();
		resultUsers = new ArrayList<User>();
	}

	public SearchMessage search() {
		if (searchType.equals("user")) {
			resultEvents = dm.searchByUser(searchParameter);
		} else if (searchType.equals("event")) {
			resultEvents = dm.searchByEvent(searchParameter);
		}
		for(Event tempEvent : resultEvents){
			resultUsers.add(dm.getUser(tempEvent.getPoster()));
		}
		SearchMessage sm = new SearchMessage(resultEvents, resultUsers);
		return sm;
	}
}
