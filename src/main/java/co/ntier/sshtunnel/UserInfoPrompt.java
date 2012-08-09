package co.ntier.sshtunnel;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.jcraft.jsch.UserInfo;

public class UserInfoPrompt implements UserInfo{

	private String defaultPassword;
	private String password;
	
	public void setDefaultPassword(String defaultPassword) {
		this.defaultPassword = defaultPassword;
	}
	
	public String getPassword() {
		if(defaultPassword != null){
			return defaultPassword;
		}
		return password;
	}

	public boolean promptPassword(String message) {
		if(defaultPassword == null){
			JPasswordField passField = new JPasswordField();
			switch(JOptionPane.showConfirmDialog(null, passField, "Password Required", JOptionPane.OK_CANCEL_OPTION)){
			case JOptionPane.OK_OPTION:
				password = String.valueOf(passField.getPassword());
				passField.setText(null);
				return true;
				
			case JOptionPane.CANCEL_OPTION:
				return false;
			}
		
			throw new IllegalStateException();
		}
		return true;
	}

	public boolean promptYesNo(String message) {
		switch(JOptionPane.showConfirmDialog(null, message, "Action Required", JOptionPane.YES_NO_OPTION)){
		case JOptionPane.YES_OPTION:
			return true;
			
		case JOptionPane.NO_OPTION:
			return false;
		}
		throw new IllegalStateException();
	}

	public void showMessage(String message) {
		JOptionPane.showMessageDialog(null, message, "Notice", JOptionPane.INFORMATION_MESSAGE);
	}
	
	// ********* Unsupported Methods **********
	
	public boolean promptPassphrase(String message) {
		throw new UnsupportedOperationException("This method is not supported");
	}
	
	public String getPassphrase() {
		throw new UnsupportedOperationException("This method is not supported");
	}
}
