package Message;

public class LoginMessage extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean ifSuccess;
	public LoginMessage(boolean ifSuccess){
		this.action = "LoginMessage";
		this.ifSuccess = ifSuccess;
	}
}
