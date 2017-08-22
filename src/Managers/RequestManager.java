package Managers;
import BaseClass.Event;
import BaseClass.User;
import Message.EventMessage;

public class RequestManager { 
	int eventID;
	DatabaseManager dm;
	
	public RequestManager(int eventID){
		this.eventID = eventID;		
		dm = new DatabaseManager();
	}
	 
	public EventMessage request(){
		Event event = dm.getEventObject(eventID);
		User user = dm.getUser(event.getPoster());
		EventMessage em = new EventMessage (event, user);
		return em;
	}
	
}
