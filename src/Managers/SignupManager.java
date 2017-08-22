package Managers;

public class SignupManager {
	DatabaseManager dm;
	String username;
	String password;
	String fname;
	String lname;
	
	public SignupManager(String mUsername, String mPassword, String mFname, String mLname){
		dm = new DatabaseManager();
		username = mUsername;
		password = mPassword;
		fname = mFname;
		lname = mLname;
	}
	public boolean signup(){
		boolean success = false;
		/*add code here*/
		success = dm.signUpUser(username, password, fname, lname);
		return success;
	}
	 
}
