package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.JsonElement;

import messenger.MainFrame;
import messenger.loginForm.LoginForm;

//'login'�� ��û�ϴ� Ŭ���̾�Ʈ�� ��Ʈ�ѷ�
public class Client_login_controller {
	//Ŭ���̾�Ʈ�� ������ request ����
	String user_id;
	String user_pw;
	
	//������ ������ response ����
	public String user_pk;
	String user_name;
	String flag; 
	
	//JSON���� ����
	String request_msg;
	BufferedWriter buffr;
	
	Socket client;
	MainFrame mainFrame;
	
	public Client_login_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	//�� Ŭ���̾�Ʈ���� ��û���� [requestType] JSOM�ۼ��Ͽ� ������ ����
	
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
		System.out.println("Client_login_controller�� : request_msg : "+request_msg);
		try {
			buffr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffr.write(request_msg+"\n");
			buffr.flush();
			System.out.println("client_login_controller�� : �α��� ���� ������ ���� => user_id : "+user_id+", user_pw : "+user_pw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//�� �����κ��� �Ѱܹ��� [responseType] JSON �Ľ�
	
	public void login_parser(JsonElement obj) {
		flag = obj.getAsJsonObject().get("flag").getAsString();
		user_pk = obj.getAsJsonObject().get("user_pk").getAsString();
		user_name = obj.getAsJsonObject().get("user_name").getAsString();
		System.out.println("client_login_controller�� : Ŭ����Ʈ�� ��� ����=> flag : "+flag+", user_name : "+user_name+", user_pk : "+user_pk);
		mainFrame.loginForm.response_Login(flag, user_name, user_pk);
	}
}






