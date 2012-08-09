package co.ntier.sshtunnel;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class Tunneler {

	private int port;
	private String user, host;
	private Session session;
	private JSch jsch = new JSch();
	
	private static final UserInfoPrompt userInfoPrompt = new UserInfoPrompt();
	
	public Tunneler(String user, String host, int port){
		this.user = user;
		this.host = host;
		this.port = port;
	}
	
	public void connect() throws JSchException{
		session = jsch.getSession(user, host, port);
		session.setUserInfo(userInfoPrompt);
		session.connect();
	}
	
	public void setDefaultPassword(String password){
		userInfoPrompt.setDefaultPassword(password);
	}
	
	public void disconnect(){
		session.disconnect();
		log("Disconnected");
	}
	
	public void forwardPort(int localPort, String destination, int destinationPort) throws JSchException{
		int assigned = session.setPortForwardingL(localPort, destination, destinationPort);
		log("Port forwaring on " + assigned);
	}
	
	private void log(String msg){
		System.out.println(msg);
	}
	
}
