package co.ntier.sshtunnel.telnet;



public class PlatformFactory {

	private PlatformFactory(){}
	
	public static PlatformConfig createConfig(){
		String start = System.getProperty("os.name").split(" ")[0].toLowerCase();
		if(start.startsWith("win")){
			return new WindowsConfig();
		}
		return new UnixConfig();
	}
	
}
