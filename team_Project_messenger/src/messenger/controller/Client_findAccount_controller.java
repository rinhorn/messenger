package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.google.gson.JsonElement;

import messenger.MainFrame;
import messenger.loginForm.FindId;
import messenger.loginForm.Register;

public class Client_findAccount_controller {
	Socket client;
	MainFrame mainFrame;
	FindId findId;
	public Client_findAccount_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
		this.findId = mainFrame.findId;
	}
	
	//★ 클라이언트부터 요청받은 [requestType] JSOM작성하여 서버로 전송
	
		public void findID_request(String user_name, String user_birth,String user_email) {			
			StringBuffer sb = new StringBuffer();
			
			sb.append("{");
			sb.append("\"requestType\":\"findId\",");
			sb.append("\"user_name\":\""+user_name+"\",");
			sb.append("\"user_birth\":\""+user_birth+"\",");
			sb.append("\"user_email\":\""+user_email+"\"");
			sb.append("}");
			
			String request_msg = sb.toString();
			
			System.out.println("Client_findAccount_controller曰 : request_msg : "+request_msg);	
			try {
				BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				buffw.write(request_msg+"\n");
				buffw.flush();
				System.out.println("Client_findAccount_controller : request_msg 전송 완료");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//★ 서버로부터 넘겨받은 [responseType] JSON 파싱
		
		public void findID_parser(JsonElement obj) {
			System.out.println("Client_findAccount_controller : findID_parser() method 호출");
			String flag = obj.getAsJsonObject().get("flag").getAsString();
			System.out.println("Client_findAccount_controller曰 : findID 결과 전송=> flag : "+flag);
			if(flag.equals("true")) {
				JOptionPane.showMessageDialog(mainFrame.findId, "계정을 메일로 보냈습니다.");
			}else {
				JOptionPane.showMessageDialog(mainFrame.findId, "일치하는 회원 정보가 없습니다.");
			}
		}
}
