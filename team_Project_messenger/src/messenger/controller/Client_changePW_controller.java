package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.google.gson.JsonElement;

import messenger.MainFrame;

//'login'을 요청하는 클라이언트측 컨트롤러
public class Client_changePW_controller {
	//클라이언트가 보내온 request 정보
	String user_id;
	String user_pw;
	
	//JSON관련 변수
	String request_msg;
	BufferedWriter buffw;
	
	Socket client;
	MainFrame mainFrame;
	
	//Server에서 받은 JSON관련
	boolean flag;
	
	public Client_changePW_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	//★ 클라이언트부터 요청받은 [requestType] JSOM작성하여 서버로 전송
	
	public void changePW_request(String user_id,String user_pw) {
		this.user_id = user_id;
		this.user_pw = user_pw;
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\":\"changePW\",");
		sb.append("\"user_id\":"+"\""+user_id+"\",");
		sb.append("\"user_pw\":"+"\""+user_pw+"\"");
		sb.append("}");
		System.out.println("Client_change_controller : 클라이언트가 보내온 user_id : "+user_id);
		request_msg = sb.toString();
		System.out.println("Client_changePW_controller曰 : request_msg : "+request_msg);
		try {
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(request_msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//★ 서버로부터 넘겨받은 [responseType] JSON 파싱
	
	public void changePW_parser(JsonElement obj) {
		flag = obj.getAsJsonObject().get("flag").getAsBoolean();
		System.out.println("Client_changePW_controller : changePW_parser : Server에서 넘어온 Flag : "+flag);
		
		if(flag) {
			JOptionPane.showMessageDialog(mainFrame, "비밀번호 변경 성공");
			mainFrame.resetPw.btn_change.setEnabled(false);
			mainFrame.checkThread.thread_flag = false;
		}else {
			JOptionPane.showMessageDialog(mainFrame, "비밀번호 변경 실패");
		}
	}
}






