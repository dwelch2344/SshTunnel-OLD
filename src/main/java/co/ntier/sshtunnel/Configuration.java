package co.ntier.sshtunnel;

public class Configuration {

	private String user, pass, host, remoteHost;
	private int port, localPort, remotePort;
	
	public Configuration() {}

	public Configuration(String user, String pass, int port) {
		this.user = user;
		this.pass = pass;
		this.port = port;
	}
	
	public Configuration(String user, String pass, String host,
			String remoteHost, int port, int localPort, int remotePort) {
		super();
		this.user = user;
		this.pass = pass;
		this.host = host;
		this.remoteHost = remoteHost;
		this.port = port;
		this.localPort = localPort;
		this.remotePort = remotePort;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}

	public String getRemoteHost() {
		return remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}
	
}
