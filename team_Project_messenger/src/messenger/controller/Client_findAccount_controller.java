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
	
	//�� Ŭ���̾�Ʈ���� ��û���� [requestType] JSOM�ۼ��Ͽ� ������ ����
	
		public void findID_request(String user_name, String user_birth,String user_email) {			
			StringBuffer sb = new StringBuffer();
			
			sb.append("{");
			sb.append("\"requestType\":\"findId\",");
			sb.append("\"user_name\":\""+user_name+"\",");
			sb.append("\"user_birth\":\""+user_birth+"\",");
			sb.append("\"user_email\":\""+user_email+"\"");
			sb.append("}");
			
			String request_msg = sb.toString();
			
			System.out.println("Client_findAccount_controller�� : request_msg : "+request_msg);	
			try {
				BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				buffw.write(request_msg+"\n");
				buffw.flush();
				System.out.println("Client_findAccount_controller : request_msg ���� �Ϸ�");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//�� �����κ��� �Ѱܹ��� [responseType] JSON �Ľ�
		
		public void findID_parser(JsonElement obj) {
			System.out.println("Client_findAccount_controller : findID_parser() method ȣ��");
			String flag = obj.getAsJsonObject().get("flag").getAsString();
			System.out.println("Client_findAccount_controller�� : findID ��� ����=> flag : "+flag);
			if(flag.equals("true")) {
				JOptionPane.showMessageDialog(mainFrame.findId, "������ ���Ϸ� ���½��ϴ�.");
			}else {
				JOptionPane.showMessageDialog(mainFrame.findId, "��ġ�ϴ� ȸ�� ������ �����ϴ�.");
			}
		}
}
