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
	
	final String FROM = "AvoCa@talk.com";		//보낼 사람 메일주소
	String user_email;		//받는 사람 메일주소
	String user_id;
	String user_name;
	int certify_key;
	
	String subject = "AvoCaTALK 계정 찾기";
	
	String content;
	
	public SendingEmail(ServerThread serverThread) {
		this.serverThread = serverThread;
	}
	
	public void sendMailForID(String user_id, String user_name, String user_email) {
		this.user_email = user_email;
		this.user_id = user_id;
		this.user_name = user_name;
		
		//메일 제목
		subject = "AvoCaTALK 계정 찾기 --- ID";
		
		/*보낼 메일 내용*/
		content = String.join(System.getProperty("line.separator"),
				"<h1>AvoCaTALK 계정</h1>",
				"<br>",
				"<p>반갑습니다, AvoCaTALK 입니다.<br>",
				"'"+user_name+"' 님의 요청대로, ID를 메일로 보내드립니다.</p>",
				"<br>",
				"<p>'"+user_name+"' 님의 ID는 : '"+user_id+"' 입니다.</p><br>",
				"<p><a href='https://www.naver.com'>Go To Naver</a><br>",
				
				"<h1> Developed By 3 corp</h1>"
				);
		/*보낼 메일 내용*/
		
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
			System.out.println("--메일 보내기 실패--\n 사유 : AddressException");
		} catch (MessagingException e) {
			System.out.println("--메일 보내기 실패--\n 사유 : MessagingException");
		} finally {
			System.out.println("Success----");
		}
		
	}
	public void sendMailForCertify(String user_name, String user_email,int certify_key) {
		this.user_name = user_name;
		this.user_email = user_email;
		this.certify_key = certify_key;
		
		//메일 제목
		subject = "AvoCaTALK 계정 찾기 --- 인증번호";
		
		/*보낼 메일 내용*/
		content = String.join(System.getProperty("line.separator"),
				"<h1>AvoCaTALK 비밀번호 변경 인증번호</h1>",
				"<br>",
				"<p>반갑습니다, AvoCaTALK 입니다.<br>",
				"'"+user_name+"' 님의 요청대로, 인증번호를 메일로 보내드립니다.</p>",
				"<br>",
				"<p>'"+user_name+"' 님의 인증번호는 : '"+certify_key+"' 입니다.</p><br>",
				"<p><a href='https://www.naver.com'>Go To Naver</a><br>",
				
				"<h1> Developed By 3 corp</h1>"
				);
		/*보낼 메일 내용*/
		
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
			System.out.println("--메일 보내기 실패--\n 사유 : AddressException");
		} catch (MessagingException e) {
			System.out.println("--메일 보내기 실패--\n 사유 : MessagingException");
		} finally {
			System.out.println("Success----");
		}
		
	}
}
