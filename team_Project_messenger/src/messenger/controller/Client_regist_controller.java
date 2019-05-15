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
	
	//�� Ŭ���̾�Ʈ���� ��û���� [requestType] JSOM�ۼ��Ͽ� ������ ����
	
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
			System.out.println("Client_regist_controller�� : regist_request_msg : "+regist_request_msg);	
			try {
				BufferedWriter buffr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				buffr.write(regist_request_msg+"\n");
				buffr.flush();
				System.out.println("Client_regist_controller : regist_request_msg ���� �Ϸ�");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//�� �����κ��� �Ѱܹ��� [responseType] JSON �Ľ�
		
		public void regist_parser(JsonElement obj) {
			System.out.println("Client_regist_controller : regist_parser() method ȣ��");
			String flag = obj.getAsJsonObject().get("flag").getAsString();
			System.out.println("client_checkId_controller�� : checkId ��� ����=> flag : "+flag);
			if(flag.equals("true")) {
				JOptionPane.showMessageDialog(mainFrame, "ȸ�� ������ ���������� �Ϸ� �Ǿ����ϴ�.");
			}else {
				JOptionPane.showMessageDialog(mainFrame, "ȸ�� ���Կ� �����Ͼ����ϴ�.");
			}
			//login.response_checkId(flag);
		}
}
