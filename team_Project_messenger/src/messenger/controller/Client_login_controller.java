package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.JsonElement;

import messenger.MainFrame;
import messenger.loginForm.LoginForm;

//'login'을 요청하는 클라이언트측 컨트롤러
public class Client_login_controller {
	//클라이언트가 보내온 request 정보
	String user_id;
	String user_pw;
	
	//서버가 보내준 response 정보
	public String user_pk;
	String user_name;
	String flag; 
	
	//JSON관련 변수
	String request_msg;
	BufferedWriter buffr;
	
	Socket client;
	MainFrame mainFrame;
	
	public Client_login_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	//★ 클라이언트부터 요청받은 [requestType] JSOM작성하여 서버로 전송
	
	public void login_request(String user_id, String user_pw) {
		this.user_id = user_id;
		this.user_pw = user_pw;
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\":\"login\",");
		sb.append("\"user_id\":"+"\""+user_id+"\",");
		sb.append("\"user_pw\":"+"\""+user_pw+"\"");
		sb.append("}");
		
		request_msg = sb.toString();
		System.out.println("Client_login_controller曰 : request_msg : "+request_msg);
		try {
			buffr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffr.write(request_msg+"\n");
			buffr.flush();
			System.out.println("client_login_controller曰 : 로그인 정보 서버로 전송 => user_id : "+user_id+", user_pw : "+user_pw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//★ 서버로부터 넘겨받은 [responseType] JSON 파싱
	
	public void login_parser(JsonElement obj) {
		flag = obj.getAsJsonObject().get("flag").getAsString();
		user_pk = obj.getAsJsonObject().get("user_pk").getAsString();
		user_name = obj.getAsJsonObject().get("user_name").getAsString();
		System.out.println("client_login_controller曰 : 클라인트로 결과 전송=> flag : "+flag+", user_name : "+user_name+", user_pk : "+user_pk);
		mainFrame.loginForm.response_Login(flag, user_name, user_pk);
	}
}






