package com.gordon.gmail_sender.utils;

public class EmailUtils {
	public static String getEmailMessage(String name, String host, String token) {
		return 
				"Hello "+name+",\n\nYour new Account has been creted. Please click the link below to verify your account.\n\n"+getVerificationUrl(host, token)+"\n\n The Support team";
	}

	public static String getVerificationUrl(String host, String token) {
		// TODO Auto-generated method stub
		return host+"/api/users?token="+token;
	}

}
