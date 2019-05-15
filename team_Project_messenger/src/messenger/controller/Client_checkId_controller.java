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

//Ŭ���̾�Ʈ�� '���̵��ߺ�'Ȯ�� ���� ��Ʈ�ѷ�

public class Client_checkId_controller {
	//Ŭ���̾�Ʈ�κ��� �Ѱܹ��� ����
	String user_id;
	
	//������ ������ response ����
	String flag;
	
	//JSON���� ����
	String request_msg;
	BufferedWriter buffr;
	
	Socket client;
	Register register;
	MainFrame mainFrame;
	
	public Client_checkId_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
		
		System.out.println("Client_checkId_controller : regster ������ �� : "+this.register);
	}
	
	//�� Ŭ���̾�Ʈ���� ��û���� [requestType] JSOM�ۼ��Ͽ� ������ ����
	
	public void checkId_request(String user_id) {
		this.user_id = user_id;
		this.register = mainFrame.register;
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\" : \"checkId\",");
		sb.append("\"user_id\" : \""+user_id+"\"");
		sb.append("}");
		request_msg = sb.toString();
		System.out.println("Client_checkId_controller�� : request_msg : "+request_msg);	
		try {
			buffr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffr.write(request_msg+"\n");
			buffr.flush();
			System.out.println("���� �Ϸ�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//�� �����κ��� �Ѱܹ��� [responseType] JSON �Ľ�
	
	public void checkId_parser(JsonElement obj) {
		System.out.println("checkId_parser�޼��� ȣ��");
		flag = obj.getAsJsonObject().get("flag").getAsString();
		System.out.println("client_checkId_controller�� : checkId ��� ����=> flag : "+flag);
		System.out.println("Client_checkId_controller : regster ������ �� : "+register);
		register.response_checkId(flag);
		//login.response_checkId(flag);
	}
}
