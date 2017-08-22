package Message;

import java.util.ArrayList;

import BaseClass.Event;

public class MapMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<Float> coordinatesX;
	ArrayList<Float> coordinatesY;
	ArrayList<String> eventTitles;
	ArrayList<Integer> eventIDs;
	public MapMessage(ArrayList<Event> events){
		coordinatesX = new ArrayList<Float>();
		coordinatesY = new ArrayList<Float>();
		eventTitles = new ArrayList<String>();
		eventIDs = new ArrayList<Integer>();
		this.action = "MapMessage";
		for(Event ev: events){
			coordinatesX.add(ev.getCoordinateX());
			coordinatesY.add(ev.getCoordinateY());
			eventTitles.add(ev.getTitle());
			eventIDs.add(ev.getEventID());
		}
	}
	public MapMessage (Event event){ //display a specific event on the map
	}
	
	
}
