package Managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import BaseClass.Event;
import BaseClass.User;

public class DatabaseManager {

	public DatabaseManager() {
	}

	private int getUserID(String username, Connection conn) {
		Statement st = null;
		ResultSet rs = null;
		int userID = -1;
		try {
			st = conn.createStatement();
			// mysql Select statement
			String newUsername = username.toLowerCase();
			rs = st.executeQuery("SELECT * FROM UserTable WHERE username='" + newUsername + "';");
			while (rs.next()) {
				userID = rs.getInt("userID");
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage());
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return userID;
	}

	private String getUsername(int userID, Connection conn) {
		Statement st = null;
		ResultSet rs = null;
		String username = null;
		try {
			st = conn.createStatement();
			// mysql Select statement
			rs = st.executeQuery("SELECT username FROM UserTable WHERE userID='" + userID + "';");
			while (rs.next()) {
				username = rs.getString("username");
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage());
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return username;
	}

	// return the user's past, current, upcoming events depending on when
	// paramater
	private ArrayList<Event> getEventList(String username, boolean created, Connection conn) {
		Statement st = null;
		ResultSet rs = null;
		ArrayList<Event> events = new ArrayList<>();
		try {
			st = conn.createStatement();
			// mysql Select statement
			username.toLowerCase();
			int userID = getUserID(username, conn);
			// get the list of eventID from certain table
			if (created) {
				rs = st.executeQuery("SELECT * FROM User_Event_Created WHERE userID='" + userID + "';");
			} else {
				rs = st.executeQuery("SELECT * FROM User_Event_Participated WHERE userID='" + userID + "';");
			}
			while (rs.next()) {
				int eventID = rs.getInt("eventID");
				Event event = createEventObject(eventID, conn);
				events.add(event);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage());
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return events;
	}

	private Event createEventObject(int eventID, Connection conn) {
		Statement st = null;
		ResultSet rs = null;
		// dyanamically load a class at runtime
		try {
			// get the event info then add it to events list
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM EventTable WHERE eventID='" + eventID + "';");
			while (rs.next()) {
				String title = rs.getString("title");
				String location = rs.getString("location");
				String description = rs.getString("description");
				String dateString = rs.getString("dateEvent");
				// parse date data
				SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy HH:mm");
				Date date = formatter.parse(dateString);
				int coordinateX = rs.getInt("coordinateX");
				int coordinateY = rs.getInt("coordinateY");
				// create user object from posterID
				int posterID = rs.getInt("posterID");
				String username = getUsername(posterID, conn);
				Event event = new Event(title, location, description, date, coordinateX, coordinateY, username);
				;
				event.setEventID(eventID);
				return event;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return null;
	}

	// get user object with username
	private User createUserObject(String username, Connection conn) {
		Statement st = null;
		ResultSet rs = null;
		User user = null;
		try {
			st = conn.createStatement();
			// mysql Select statement
			String newUsername = username.toLowerCase();
			rs = st.executeQuery("SELECT * FROM UserTable WHERE username='" + newUsername + "';");
			while (rs.next()) {
				int userID = rs.getInt("userID");
				String fname = rs.getString("fname");
				String lname = rs.getString("lname");
				// String nickname = rs.getString("nickname");
				// float rating = rs.getFloat("rating");
				user = new User(userID, username, fname, lname);
				ArrayList<Event> myEvents = getEventList(username, true, conn);
				ArrayList<Event> participatedEvents = getEventList(username, false, conn);
				ArrayList<Event> previousEvents = new ArrayList<>();
				ArrayList<Event> upcomingEvents = new ArrayList<>();
				for (int i = 0; i < participatedEvents.size(); i++) {
					if (participatedEvents.get(i).eventTime().equals("past")) {
						previousEvents.add(participatedEvents.get(i));
					} else {
						upcomingEvents.add(participatedEvents.get(i));
					}
				}
				user.setMyEvents(myEvents);
				user.setPreviousEvents(previousEvents);
				user.setUpcomingEvents(upcomingEvents);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage());
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return user;
	}

	// public functions
	public boolean checkLoginValidation(String username, String password) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// dyanamically load a class at runtime
			Class.forName("com.mysql.jdbc.Driver");
			// connect with the driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost/EventSC?user=root&password=dudu19960618&useSSL=false");
			st = conn.createStatement();
			// mysql Select statement
			String newUsername = username.toLowerCase();
			rs = st.executeQuery("SELECT * FROM UserTable WHERE username='" + newUsername + "';");
			String passwordData = null;
			while (rs.next()) {
				passwordData = rs.getString("pass");
			}
			if (passwordData == null) {
				return false;
			} else if (passwordData.equals(password)) {
				return true;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return false;
	}

	// add an account to the database returns false if username already esits
	public boolean signUpUser(String username, String pass, String fname, String lname) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement preparedStmt = null;
		try {
			// dyanamically load a class at runtime
			Class.forName("com.mysql.jdbc.Driver");
			// connect with the driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost/EventSC?user=root&password=dudu19960618&useSSL=false");
			// check if username already exists
			st = conn.createStatement();
			// mysql Select statement
			String newUsername = username.toLowerCase();
			rs = st.executeQuery("SELECT * FROM UserTable WHERE username='" + newUsername + "';");
			// if SELECT statements returns anything then username already
			// exists and return false
			while (rs.next()) {
				return false;
			}
			// create the mysql insert statement
			String query = "INSERT INTO UserTable (username, pass, fname, lname)" + "VALUES (?, ?, ?, ?)";
			// create the insert preparedstatement
			preparedStmt = conn.prepareStatement(query);
			// input values
			preparedStmt.setString(1, newUsername);
			preparedStmt.setString(2, pass);
			preparedStmt.setString(3, fname);
			preparedStmt.setString(4, lname);
			// execute
			preparedStmt.executeUpdate();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (preparedStmt != null) {
					preparedStmt.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return true;
	}

	public User getUser(String username) {
		Connection conn = null;
		User user = null;
		try {
			// dyanamically load a class at runtime
			Class.forName("com.mysql.jdbc.Driver");
			// connect with the driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost/EventSC?user=root&password=dudu19960618&useSSL=false");
			user = createUserObject(username, conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			// always close connection to database
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return user;
	}

	public Event getEventObject(int eventID) {
		Connection conn = null;
		Event event = null;
		try {
			// dyanamically load a class at runtime
			Class.forName("com.mysql.jdbc.Driver");
			// connect with the driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost/EventSC?user=root&password=dudu19960618&useSSL=false");
			event = createEventObject(eventID, conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage() + "sdsd");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			// always close connection to database
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return event;
	}

	// get all events connected to a user
	public ArrayList<Event> getEvents(String username, boolean create) {
		Connection conn = null;
		ArrayList<Event> events = null;
		try {
			// dyanamically load a class at runtime
			Class.forName("com.mysql.jdbc.Driver");
			// connect with the driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost/EventSC?user=root&password=dudu19960618&useSSL=false");
			events = getEventList(username, create, conn);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			// always close connection to database
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return events;
	}

	// get all of the current events
	public List<Event> requestAllEvents() {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		List<Event> events = new ArrayList<>();
		try {
			// dyanamically load a class at runtime
			Class.forName("com.mysql.jdbc.Driver");
			// connect with the driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost/EventSC?user=root&password=dudu19960618&useSSL=false");
			st = conn.createStatement();
			// mysql Select statement
			rs = st.executeQuery("SELECT * FROM EventTable;");
			while (rs.next()) {
				int eventID = rs.getInt("eventID");
				String title = rs.getString("title");
				String location = rs.getString("location");
				String description = rs.getString("description");
				String dateString = rs.getString("dateEvent");
				// parse date data
				SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy HH:mm");
				Date date = formatter.parse(dateString);
				int coordinateX = rs.getInt("coordinateX");
				int coordinateY = rs.getInt("coordinateY");
				int posterID = rs.getInt("posterID");
				String username = getUsername(posterID, conn);
				Event event = new Event(title, location, description, date, coordinateX, coordinateY, username);
				event.setEventID(eventID);
				events.add(event);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return events;
	}

	// add an event to the database
	public void addEvent(Event newEvent) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement preparedStmt = null;
		PreparedStatement preparedStmt2 = null;
		try {
			// dyanamically load a class at runtime
			Class.forName("com.mysql.jdbc.Driver");
			// connect with the driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost/EventSC?user=root&password=dudu19960618&useSSL=false");
			String title = newEvent.getTitle();
			String location = newEvent.getLocationString();
			String description = newEvent.getDescription();
			Date date = newEvent.getDate();
			SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy HH:mm");
			String dateString = formatter.format(date);
			float coordinateX = newEvent.getCoordinateX();
			float coordinateY = newEvent.getCoordinateY();
			String username = newEvent.getPoster();
			int posterID = getUserID(username, conn);
			// create the mysql insert statement
			String query = "INSERT INTO EventTable (title, location, description, dateEvent, coordinateX, coordinateY, posterID)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
			// create the insert preparedstatement
			preparedStmt = conn.prepareStatement(query);

			// input values
			String newTitle = title.toLowerCase();
			preparedStmt.setString(1, newTitle);
			preparedStmt.setString(2, location);
			preparedStmt.setString(3, description);
			preparedStmt.setString(4, dateString);
			preparedStmt.setFloat(5, coordinateX);
			preparedStmt.setFloat(6, coordinateY);
			preparedStmt.setInt(7, posterID);

			preparedStmt.executeUpdate();

			// get the ID of the newly created event
			st = conn.createStatement();
			// mysql Select statement
			rs = st.executeQuery("SELECT * FROM EventTable WHERE title='" + newTitle + "';");
			int eventID = -1;
			while (rs.next()) {
				eventID = rs.getInt("eventID");
			}
			// insert into event created table
			// create the mysql insert statement
			String query2 = "INSERT INTO User_Event_Created (userID, eventID)" + "VALUES (?, ?)";
			// create the insert preparedstatement
			preparedStmt2 = conn.prepareStatement(query2);

			// input values
			preparedStmt2.setInt(1, posterID);
			preparedStmt2.setInt(2, eventID);

			preparedStmt2.executeUpdate();

		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (preparedStmt != null) {
					preparedStmt.close();
				}
				if (preparedStmt2 != null) {
					preparedStmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
	}

	// add an event to the database
	public ArrayList<Event> searchByUser(String searchParameter) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<Event> events = new ArrayList<>();
		try {
			// dyanamically load a class at runtime
			Class.forName("com.mysql.jdbc.Driver");
			// connect with the driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost/EventSC?user=root&password=dudu19960618&useSSL=false");
			st = conn.createStatement();
			// mysql Select statement
			searchParameter.toLowerCase();
			int userID = getUserID(searchParameter, conn);
			rs = st.executeQuery("SELECT * FROM EventTable WHERE posterID='" + userID + "';");
			// get the list of eventIDS
			while (rs.next()) {
				int eventID = rs.getInt("eventID");
				Event event = createEventObject(eventID, conn);
				events.add(event);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage() + "sdsd");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return events;
	}

	// add an event to the database
	public ArrayList<Event> searchByEvent(String searchParameter) {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<Event> events = new ArrayList<>();
		try {
			// dyanamically load a class at runtime
			Class.forName("com.mysql.jdbc.Driver");
			// connect with the driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost/EventSC?user=root&password=dudu19960618&useSSL=false");
			st = conn.createStatement();
			// mysql Select statement
			searchParameter.toLowerCase();
			rs = st.executeQuery("SELECT * FROM EventTable WHERE title='" + searchParameter + "';");
			// get the list of eventIDS
			while (rs.next()) {
				int eventID = rs.getInt("eventID");
				Event event = createEventObject(eventID, conn);
				events.add(event);
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage() + "sdsd");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			// always close connection to database
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
		return events;
	}

	public void attendEvent(User user, Event event) {
		Connection conn = null;
		PreparedStatement preparedStmt = null;
		try {
			// dyanamically load a class at runtime
			Class.forName("com.mysql.jdbc.Driver");
			// connect with the driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost/EventSC?user=root&password=dudu19960618&useSSL=false");
			int userID = user.getUserID();
			int eventID = event.getEventID();
			// create the mysql insert statement
			String query = "INSERT INTO User_Event_Participated (userID, eventID)" + "VALUES (?, ?)";
			// create the insert preparedstatement
			preparedStmt = conn.prepareStatement(query);
			// input values
			preparedStmt.setInt(1, userID);
			preparedStmt.setInt(2, eventID);
			// execute
			preparedStmt.executeUpdate();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			System.out.println("sqle: " + sqle.getMessage() + "sdsd");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("cnfe: " + cnfe.getMessage());
		} finally {
			// always close connection to database
			try {
				if (preparedStmt != null) {
					preparedStmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle closing: " + sqle.getMessage());
			}
		}
	}
}
