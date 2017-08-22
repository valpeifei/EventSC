package Managers;
import Message.LoginMessage;

public class LoginManager {
	DatabaseManager dm;
	String username;
	String password;
	
	public LoginManager(String mUsername, String mPassword){
		dm = new DatabaseManager();
		username = mUsername;
		password = mPassword;
	}
	public LoginMessage login(){
		boolean success = dm.checkLoginValidation(username, password);
		LoginMessage lm = new LoginMessage(success);
		return lm;
	}
	 
}
