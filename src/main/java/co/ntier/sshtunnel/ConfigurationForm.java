package co.ntier.sshtunnel;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import co.ntier.util.PropertiesUtil;

public class ConfigurationForm extends JPanel{

	private JTextField 
		user = getField(false),
		pass = getField(true),
		host = getField(false),
		port = getField(false),
		remoteHost = getField(false),
		localPort = getField(false),
		remotePort = getField(false);
	
	public ConfigurationForm() {
		
		JPanel form = this;
		
		int h = port.getPreferredSize().height;
		

        form.setLayout(new GridBagLayout());
        FormUtility formUtility = new FormUtility();

        // Add some sample fields
        formUtility.addLabel("User: ", form);
        formUtility.addLastField(user, form);

        formUtility.addLabel("Password: ", form);
        formUtility.addLastField(pass, form);

        
        formUtility.addLabel("Host", form);
        host.setPreferredSize(new Dimension(300, h));
        formUtility.addMiddleField(host, form);
        
        
        
        JPanel serverPanel = new JPanel(new GridBagLayout());
        formUtility.addLabel("Port", serverPanel);
        formUtility.addLastField(port, serverPanel);
        formUtility.addLastField(serverPanel, form);
        
        formUtility.addLabel("Destination: ", form);
        formUtility.addMiddleField(remoteHost, form);

        JPanel localRemotePorts = new JPanel();
        localRemotePorts.setLayout(new GridBagLayout());
        formUtility.addLabel(" Local: ", localRemotePorts);
        Dimension localSize = localPort.getPreferredSize();
        localSize.width = 50;
        localPort.setPreferredSize(localSize);
        formUtility.addLabel(localPort, localRemotePorts);
        formUtility.addLabel(" Remote: ", localRemotePorts);
        Dimension remoteSize = remotePort.getPreferredSize();
        remoteSize.width = 50;
        remotePort.setPreferredSize(remoteSize);
        formUtility.addLabel(remotePort, localRemotePorts);

        formUtility.addLabel(localRemotePorts, form);
        formUtility.addLastField(new JPanel(), form);

        // Add an little padding around the form
        form.setBorder(new EmptyBorder(2, 2, 2, 2));
	}
	
	public Configuration getConfiguration(){
		
		String user, pass, host, remoteHost;
		int port, localPort, remotePort;
		
		try{
			port = Integer.valueOf(this.port.getText());
			localPort = Integer.valueOf(this.localPort.getText());
			remotePort = Integer.valueOf(this.remotePort.getText());
			user = this.user.getText();
			pass = this.pass.getText();
			host = this.host.getText();
			remoteHost = this.remoteHost.getText();
			
			return new Configuration(user, pass, host, remoteHost, port, localPort, remotePort);
			
		}catch(NumberFormatException e){
			throw new IllegalArgumentException("Ports can only contain numberic numbers");
		}
	}
	
	public void setConfiguration(Configuration config){
		user.setText(config.getUser());
		pass.setText(config.getPass());
		host.setText(config.getHost());
		port.setText(String.valueOf(config.getPort()));
		remoteHost.setText(config.getRemoteHost());
		localPort.setText(String.valueOf(config.getLocalPort()));
		remotePort.setText(String.valueOf(config.getRemotePort()));
	}
	

	private static JTextField getField(boolean password){
		if(password){
			return new JPasswordField();
		}
		return new JTextField();
	}
	
	public static void main(String[] args) throws Exception{
		ConfigurationForm form = new ConfigurationForm();
		JOptionPane.showConfirmDialog(null, form, "Configuration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);
		PropertiesUtil util = new PropertiesUtil();
		
		Map<String, Object> map = util.mapFields(form.getConfiguration());
		for(String key : map.keySet()){
			System.out.println(key + " : " + map.get(key));
		}
		
	}
}
