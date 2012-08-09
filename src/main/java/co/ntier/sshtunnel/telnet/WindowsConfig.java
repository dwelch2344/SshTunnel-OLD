package co.ntier.sshtunnel.telnet;

public class WindowsConfig implements PlatformConfig{

	public String getPath() {
		return "cmd.exe /c start ";
	}

	public String getSuffix() {
		return ".bat";
	}

	public String getTelnet() {
		return "telnet";
	}

}
