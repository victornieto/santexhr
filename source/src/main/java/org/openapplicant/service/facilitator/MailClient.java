package org.openapplicant.service.facilitator;

import java.io.IOException;

import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;

public class MailClient {
	
	private static final Log log = LogFactory.getLog(MailClient.class);
	
	private String user;
	
	private String password = "";
	
	private String host;
	
	private String protocol;
	
	private String folderName;
	
	private boolean debug = false;
	
	private FacilitatorService facilitatorService;
	
	@Required
	public void setUser(String value) {
		user = value;
	}
	
	public void setPassword(String value) {
		password = value;
	}
	
	@Required
	public void setHost(String value) {
		host = value;
	}
	
	@Required
	public void setProtocol(String value) {
		protocol = value;
	}
	
	@Required
	public void setDebug(boolean value) {
		debug = value;
	}
	
	@Required
	public void setFolderName(String value) {
		folderName = value;
	}
	
	@Required 
	public void setFacilitatorService(FacilitatorService value) {
		facilitatorService = value;
	}
	
	public void processMail() {
		Session session = Session.getInstance(System.getProperties(), new Auth(user, password));
		session.setDebug(debug);
		Store store = null;
		Folder folder = null;
		try {
			store = session.getStore(protocol);
			store.connect(host, user, password);
			folder = store.getFolder(folderName);
			folder.open(Folder.READ_WRITE);
			for(Message each : folder.getMessages()) {
				if(each.getFlags().contains(Flags.Flag.DELETED)){
					continue;
				}
				facilitatorService.facilitateCandidate(each);
				each.setFlag(Flags.Flag.DELETED, true);
			}
		}
		catch(NoSuchProviderException e) {
			throw new RuntimeException(e);
		} 
		catch(MessagingException e) {
			throw new RuntimeException(e);
		}
		catch(IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			expungeQuietly(folder);
			closeQuietly(store);
		}
	}
	
	private void expungeQuietly(Folder folder) {
		if(folder == null) {
			return;
		}
		try {
			folder.close(true);
		} catch(Throwable t) {
			log.error(t);
		}
	}
	
	private void closeQuietly(Store store) {
		if(store == null) {
			return;
		}
		try {
			store.close();
		} catch(Throwable t) {
			log.error(t);
		}
	}
	
	private static class Auth extends Authenticator {
		
		private PasswordAuthentication passwordAuthentication;
		
		public Auth(String username, String password) {
			passwordAuthentication = new PasswordAuthentication(username, password);
		}
		
		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return passwordAuthentication;
		}
	}

}
