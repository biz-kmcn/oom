package jp.co.bizreach.camp;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author david.genesis.cruz
 *
 */
@Component
public class SshLogic {
	
	private static final Log log = LogFactory.getLog(SshLogic.class);

	private static final String hostname = "192.168.100.101";
	private static final String userid   = "david.genesis.cruz";
	private static final String password = System.getProperty("sshPasswd");
	private static final String knownhost = "/home/pi/.ssh/known_hosts";
	
	public void countDown() {
		JSch jsch = new JSch();
	
		try {
			jsch.setKnownHosts(knownhost);
			
			// connect session
			Session session = jsch.getSession(userid, hostname, 22);
			session.setPassword(password);
			session.connect();
			
			// exec command remotely
			String command="say 3;sleep 0.5;say 2; sleep 0.5;say 1;sleep 0.5;say cheese";
			ChannelExec channel=(ChannelExec)session.openChannel("exec");
			channel.setCommand(command);
			channel.connect();
			
			channel.disconnect();
			session.disconnect();
		} catch (JSchException e) {
			log.error("failed to execute countdown.", e);
		}
	}
}
