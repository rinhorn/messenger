package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.google.gson.JsonElement;

import messenger.MainFrame;
import messenger.loginForm.Register;

public class Client_regist_controller {
	Socket client;
	MainFrame mainFrame;
	Register register;
	public Client_regist_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
		this.register = mainFrame.register;
	}
	
	//★ 클라이언트부터 요청받은 [requestType] JSOM작성하여 서버로 전송
	
		public void regist_request(String member_id, String member_name, String pw, String birth, String email, String profile) {			
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("\"requestType\" : \"regist\",");
			sb.append("\"member_id\" : \""+member_id+"\",");
			sb.append("\"member_name\" : \""+member_name+"\",");
			sb.append("\"pw\" : \""+pw+"\",");
			sb.append("\"birth\" : \""+birth+"\",");
			sb.append("\"email\" : \""+email+"\",");
			sb.append("\"profile\" : \""+profile+"\"");
			sb.append("}");
			String regist_request_msg = sb.toString();
			System.out.println("Client_regist_controller曰 : regist_request_msg : "+regist_request_msg);	
			try {
				BufferedWriter buffr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				buffr.write(regist_request_msg+"\n");
				buffr.flush();
				System.out.println("Client_regist_controller : regist_request_msg 전송 완료");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//★ 서버로부터 넘겨받은 [responseType] JSON 파싱
		
		public void regist_parser(JsonElement obj) {
			System.out.println("Client_regist_controller : regist_parser() method 호출");
			String flag = obj.getAsJsonObject().get("flag").getAsString();
			System.out.println("client_checkId_controller曰 : checkId 결과 전송=> flag : "+flag);
			if(flag.equals("true")) {
				JOptionPane.showMessageDialog(mainFrame, "회원 가입이 성공적으로 완료 되었습니다.");
			}else {
				JOptionPane.showMessageDialog(mainFrame, "회원 가입에 실패하었습니다.");
			}
			//login.response_checkId(flag);
		}
}
