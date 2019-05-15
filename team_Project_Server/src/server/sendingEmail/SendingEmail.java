package server.sendingEmail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import server.ServerThread;

public class SendingEmail {
	ServerThread serverThread;
	
	final String FROM = "AvoCa@talk.com";		//���� ��� �����ּ�
	String user_email;		//�޴� ��� �����ּ�
	String user_id;
	String user_name;
	int certify_key;
	
	String subject = "AvoCaTALK ���� ã��";
	
	String content;
	
	public SendingEmail(ServerThread serverThread) {
		this.serverThread = serverThread;
	}
	
	public void sendMailForID(String user_id, String user_name, String user_email) {
		this.user_email = user_email;
		this.user_id = user_id;
		this.user_name = user_name;
		
		//���� ����
		subject = "AvoCaTALK ���� ã�� --- ID";
		
		/*���� ���� ����*/
		content = String.join(System.getProperty("line.separator"),
				"<h1>AvoCaTALK ����</h1>",
				"<br>",
				"<p>�ݰ����ϴ�, AvoCaTALK �Դϴ�.<br>",
				"'"+user_name+"' ���� ��û���, ID�� ���Ϸ� �����帳�ϴ�.</p>",
				"<br>",
				"<p>'"+user_name+"' ���� ID�� : '"+user_id+"' �Դϴ�.</p><br>",
				"<p><a href='https://www.naver.com'>Go To Naver</a><br>",
				
				"<h1> Developed By 3 corp</h1>"
				);
		/*���� ���� ����*/
		
		Properties props = System.getProperties();

		Session session = Session.getDefaultInstance(props);
		
		session.setDebug(false);
		
		try {
			System.out.println("Sending----");
			Message msg = new MimeMessage(session);
			
			msg.setFrom(new InternetAddress(FROM));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(user_email));
			msg.setSubject(subject);
			msg.setContent(content, "text/html; charset=UTF-8");
			Transport.send(msg);
			
		} catch (AddressException e) {
			System.out.println("--���� ������ ����--\n ���� : AddressException");
		} catch (MessagingException e) {
			System.out.println("--���� ������ ����--\n ���� : MessagingException");
		} finally {
			System.out.println("Success----");
		}
		
	}
	public void sendMailForCertify(String user_name, String user_email,int certify_key) {
		this.user_name = user_name;
		this.user_email = user_email;
		this.certify_key = certify_key;
		
		//���� ����
		subject = "AvoCaTALK ���� ã�� --- ������ȣ";
		
		/*���� ���� ����*/
		content = String.join(System.getProperty("line.separator"),
				"<h1>AvoCaTALK ��й�ȣ ���� ������ȣ</h1>",
				"<br>",
				"<p>�ݰ����ϴ�, AvoCaTALK �Դϴ�.<br>",
				"'"+user_name+"' ���� ��û���, ������ȣ�� ���Ϸ� �����帳�ϴ�.</p>",
				"<br>",
				"<p>'"+user_name+"' ���� ������ȣ�� : '"+certify_key+"' �Դϴ�.</p><br>",
				"<p><a href='https://www.naver.com'>Go To Naver</a><br>",
				
				"<h1> Developed By 3 corp</h1>"
				);
		/*���� ���� ����*/
		
		Properties props = System.getProperties();

		Session session = Session.getDefaultInstance(props);
		
		session.setDebug(false);
		
		try {
			System.out.println("Sending----");
			Message msg = new MimeMessage(session);
			
			msg.setFrom(new InternetAddress(FROM));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(user_email));
			msg.setSubject(subject);
			msg.setContent(content, "text/html; charset=UTF-8");
			Transport.send(msg);
			
		} catch (AddressException e) {
			System.out.println("--���� ������ ����--\n ���� : AddressException");
		} catch (MessagingException e) {
			System.out.println("--���� ������ ����--\n ���� : MessagingException");
		} finally {
			System.out.println("Success----");
		}
		
	}
}
