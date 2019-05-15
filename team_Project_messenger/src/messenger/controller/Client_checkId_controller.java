package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import messenger.MainFrame;
import messenger.loginForm.LoginForm;
import messenger.loginForm.Register;

import com.google.gson.JsonElement;

//클라이언트측 '아이디중복'확인 관련 컨트롤러

public class Client_checkId_controller {
	//클라이언트로부터 넘겨받은 변수
	String user_id;
	
	//서버가 보내준 response 정보
	String flag;
	
	//JSON관련 변수
	String request_msg;
	BufferedWriter buffr;
	
	Socket client;
	Register register;
	MainFrame mainFrame;
	
	public Client_checkId_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
		
		System.out.println("Client_checkId_controller : regster 변수의 값 : "+this.register);
	}
	
	//★ 클라이언트부터 요청받은 [requestType] JSOM작성하여 서버로 전송
	
	public void checkId_request(String user_id) {
		this.user_id = user_id;
		this.register = mainFrame.register;
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\" : \"checkId\",");
		sb.append("\"user_id\" : \""+user_id+"\"");
		sb.append("}");
		request_msg = sb.toString();
		System.out.println("Client_checkId_controller曰 : request_msg : "+request_msg);	
		try {
			buffr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffr.write(request_msg+"\n");
			buffr.flush();
			System.out.println("전송 완료");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//★ 서버로부터 넘겨받은 [responseType] JSON 파싱
	
	public void checkId_parser(JsonElement obj) {
		System.out.println("checkId_parser메서드 호출");
		flag = obj.getAsJsonObject().get("flag").getAsString();
		System.out.println("client_checkId_controller曰 : checkId 결과 전송=> flag : "+flag);
		System.out.println("Client_checkId_controller : regster 변수의 값 : "+register);
		register.response_checkId(flag);
		//login.response_checkId(flag);
	}
}
