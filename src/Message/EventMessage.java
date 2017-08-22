package Message;

import BaseClass.Event;

import BaseClass.User;

public class EventMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String title, location, description, posterName, posterID;
	float coordinateX, coordinateY, posterRating;
	int eventID;
	String time;
	
	public EventMessage(Event event, User user){
		this.action = "EventMessage";
		this.title = event.getTitle();
		this.location = event.getLocationString();
		this.description = event.getDescription();
		this.posterName = user.getFname()+ " " + user.getLname();
		this.posterID = user.getUsername();
		this.coordinateX = event.getCoordinateX();
		this.coordinateY = event.getCoordinateY();
		this.eventID = event.getEventID();
		this.time = event.getDate().getHours()+":"+event.getDate().getMinutes()+" "+event.getDate().getMonth()+ "."+event.getDate().getDate()+"."+event.getDate().getYear(); 
	}
}
