package co.ntier.sshtunnel.telnet;

public class UnixConfig implements PlatformConfig{

	public String getPath() {
		return "/Applications/Utilities/Terminal.app/Contents/MacOS/Terminal ";
	}

	public String getSuffix() {
		return ".sh";
	}

	public String getTelnet() {
		return "/usr/bin/telnet";
	}

}
