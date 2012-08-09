import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import co.ntier.sshtunnel.Configuration;
import co.ntier.sshtunnel.ConfigurationForm;
import co.ntier.sshtunnel.Tunneler;
import co.ntier.sshtunnel.telnet.PlatformConfig;
import co.ntier.sshtunnel.telnet.PlatformFactory;
import co.ntier.util.PropertiesUtil;

import com.jcraft.jsch.JSchException;

public class SshTunnel {

	public static void main(String[] args) throws JSchException, IOException,
		InterruptedException {
		SshTunnel st = new SshTunnel();
		st.setupToolbar();
	}
	
	private static PropertiesUtil util = new PropertiesUtil();

	private static final PlatformConfig platform = PlatformFactory.createConfig();
	private Tunneler tunneler;

	// these are bad?
	private String remoteHost;
	private int localPort;
	
	// *********** Preferences Tools *********************
	
	private Configuration getConfiguration(){
		
		File file = new File(System.getProperty("user.dir") + "/sshTunnel.cfg");
		ConfigurationForm form = new ConfigurationForm();
		
			
		if(file.exists()){
			Configuration config = new Configuration();
			try {
				util.load(config, file);
				form.setConfiguration(config);
			} catch(Exception e){
				e.printStackTrace();
				showError("Couldn't load configuration:\n" + e, "Error");
			}
		}
		
		Configuration config = null;
		while(config == null){
			int result = JOptionPane.showConfirmDialog(null, form, "Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
			if(result == JOptionPane.CANCEL_OPTION){
				System.exit(0);
			}
			try{
				config = form.getConfiguration();
			}catch(Exception e){
				e.printStackTrace();
				showError("Failed to create configuration:\n" + e.getMessage(), "Error");
			}
		}
		
		try{
			util.save(config, file);
			System.out.println("Saved to " + file);
		}catch(Exception e){
			e.printStackTrace();
			showError("Couldn't save configuration:\n" + e.getMessage(), "Error");
		}
		
		return config;
	}
	
	private void showError(String message, String title){
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.OK_OPTION);
	}
	
	
	
	// ********** Public Methods *************************
	
	public void connect() throws JSchException, IOException, InterruptedException{
		Configuration config = getConfiguration();
		tunneler = new Tunneler(config.getUser(), config.getHost(), config.getPort());
		tunneler.setDefaultPassword(config.getPass());
		
		tunneler.connect();
		tunneler.forwardPort(config.getLocalPort(), config.getRemoteHost(), config.getRemotePort());
		
		remoteHost = config.getRemoteHost();
		localPort = config.getLocalPort();
		
		launchTelnet();
	}
	
	public void launchTelnet(){
		try{
			File file = getTempFile(platform.getSuffix(), platform.getTelnet());
			String path = file.toString();
			Runtime.getRuntime().exec(platform.getPath() + path);
			System.out.println("Successfully launched telnet client");
		}catch( IOException e ){
			e.printStackTrace();
			System.out.println("Could not load Telnet client");
		}
	}
	
	private File getTempFile(String suffix, String telnet) throws IOException{
		File file = File.createTempFile("sshtunnel", suffix);
		file.deleteOnExit();
		file.setExecutable(true);
		
		BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(telnet + " " + remoteHost + " " + localPort);
		out.close();
		return file;
	}
	

	// ********* TOOLBAR *****************************
	
	private MenuItem connectItem = new MenuItem("Connect");
	private MenuItem exitItem = new MenuItem("Exit");
	
	private Image image = Toolkit.getDefaultToolkit().getImage(SshTunnel.class.getResource("images/sshtunnel.png"));
	private void setupToolbar() throws IOException {
		final ActionListener listener = new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				try{
					if(tunneler == null){
						try {
							connect();
							connectItem.setLabel("Disconnect");
						} catch (Exception ex) {
							ex.printStackTrace();
							tunneler = null;
						}
					}else{
						connectItem.setLabel("Connect");
						tunneler.disconnect();
						tunneler = null;
					}
				}catch(Exception e){
					e.printStackTrace();
					showError("Failed to connect:\n" + e.getMessage(), "Error");
				}
			}
		};
		
		final ActionListener exitListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent event) {
				if(tunneler != null) tunneler.disconnect();
				System.exit(0);
			}
		};
		
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();

			PopupMenu popup = new PopupMenu();
			popup.add(connectItem);
			popup.addSeparator();
			popup.add(exitItem);
			
			TrayIcon trayIcon = new TrayIcon(image, "SshTunnel", popup);
			
			connectItem.addActionListener(listener);
			exitItem.addActionListener(exitListener);
			
			trayIcon.setImageAutoSize(true);
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println("TrayIcon could not be added.");
			}
		}else{
			JButton button = new JButton("Shutdown Connection");
			button.addActionListener(listener);
			
			JFrame frame = new JFrame("Integris Connection");
			frame.add(button);
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					listener.actionPerformed(null);
				}
			});
			frame.setResizable(false);
			frame.setSize(100, 50);
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
		}
	}
}
