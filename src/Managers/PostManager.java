package Managers;

import java.util.Date;

import BaseClass.Event;
import BaseClass.User;
import Message.EventMessage;

public class PostManager {
	String currUser;
	String title;
	String time;
	String location;
	String description;
	String category;
	String image;
	Date date;
	float coordinateX;
	float coordinateY;
	DatabaseManager dm;

	public PostManager(String mUser, Date mDate, float mX, float mY) {
		currUser = mUser;
		date = mDate;
		coordinateX = mX;
		coordinateY = mY;
		dm = new DatabaseManager();
	}

	public void setTitle(String mTitle) {
		title = mTitle;
	}

	public void setTime(String mTime) {
		time = mTime;
	}

	public void setLocation(String mLocation) {
		location = mLocation;
	}

	public void setDescription(String mDescription) {
		description = mDescription;
	}

	public void setCategory(String mCategory) {
		category = mCategory;
	}

	public void setImage(String mImage) {
		image = mImage;
	}

	public EventMessage post() {	 
		Event newEvent = new Event(title, location, description, date, coordinateX, coordinateY, currUser);
		User user = dm.getUser(currUser);
		dm.addEvent(newEvent);
		EventMessage em = new EventMessage(newEvent, user);
		return em;
	}

}
