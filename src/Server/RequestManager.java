package Server;
import Managers.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import Message.*;
public class RequestManager{
	String currentUser = "";
	
	public RequestManager(){
	}
	public void listen(HttpServletRequest request, HttpServletResponse response){
		BufferedReader br;
		PrintWriter pw=null;
		String st="";
		JsonObject receiveObj;
		Message sendMessage = null;
		try {
			br = request.getReader();
			pw = response.getWriter();
			st = br.readLine();
			System.out.println(st);
			receiveObj = new JsonParser().parse(st).getAsJsonObject();
			String requestType = receiveObj.get("requestType").getAsString();
			System.out.println(requestType);
			switch(requestType){
			case "Login":
				LoginManager lm = new LoginManager(receiveObj.get("username").getAsString(), receiveObj.get("password").getAsString());
				LoginMessage tempLM= lm.login();
				sendMessage = tempLM;
				if(tempLM.ifSuccess){
					currentUser = receiveObj.get("username").getAsString();
				}
				break;
			case "SignUp":
				SignupManager sm = new SignupManager(receiveObj.get("username").getAsString(), receiveObj.get("password").getAsString(), receiveObj.get("fname").getAsString(),receiveObj.get("lname").getAsString());
				if(sm.signup()){
					sendMessage = new LoginMessage(true);
					currentUser = receiveObj.get("username").getAsString();
				}
				else{
					sendMessage = new LoginMessage(false);
				}
				break;
			case "Post":
				System.out.println(currentUser);
				Date date = new Date(receiveObj.get("year").getAsInt(), receiveObj.get("month").getAsInt(),receiveObj.get("date").getAsInt(),receiveObj.get("hours").getAsInt(),receiveObj.get("minutes").getAsInt());
				PostManager postManager = new PostManager(currentUser, date, receiveObj.get("locationX").getAsFloat(), receiveObj.get("locationY").getAsFloat());
				//Need more
				postManager.setDescription(receiveObj.get("description").getAsString());
				postManager.setTitle(receiveObj.get("title").getAsString());
				postManager.setLocation(receiveObj.get("location").getAsString());
				sendMessage = postManager.post();
				break;
			case "Map":
				MapManager mapManager = new MapManager(currentUser);
				sendMessage = mapManager.populateMap();
				break;
			case "Search":
				SearchManager searchManager = new SearchManager(receiveObj.get("searchType").getAsString(), receiveObj.get("searchParameter").getAsString());
				sendMessage = searchManager.search();
				SearchMessage smm = searchManager.search();
				break;
			case "Profile":
				ProfileManager profileManager = new ProfileManager(currentUser);
				sendMessage = profileManager.getProfile();
				ProfileMessage pm = profileManager.getProfile();
				break;
			case "Event":
				Managers.RequestManager requestManager = new Managers.RequestManager(receiveObj.get("eventID").getAsInt());
				sendMessage = requestManager.request();
				break;
			case "RSVP":
				AttendManager attendManager = new AttendManager(this.currentUser, receiveObj.get("eventID").getAsInt());
				attendManager.attend();
				break;
			case "User":
				ProfileManager profileManager2 = new ProfileManager(receiveObj.get("username").getAsString());
				sendMessage = profileManager2.getProfile();
			}
			Gson gson = new Gson();
			String sendString = gson.toJson(sendMessage);
			System.out.println(sendString);
			response.setContentType("application/json");
			pw.print(sendString);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
