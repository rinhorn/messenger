package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import messenger.MainFrame;
import messenger.loginForm.LoginForm;
import messenger.loginForm.Register;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

//Ŭ���̾�Ʈ�� '���̵��ߺ�'Ȯ�� ���� ��Ʈ�ѷ�

public class Client_memberList_controller {
	
	// server �� ���� ���� ��ü member ������ �迭 data �� ��� ���� Object �������迭 ����
	Object[][] model;
	
	//������ ������ response ����
	String flag;
	
	//JSON���� ����
	String request_msg;
	BufferedWriter buffw;
	
	Socket client;
	Register register;
	MainFrame mainFrame;
	
	
	public Client_memberList_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	//�� Ŭ���̾�Ʈ���� ��û���� [requestType] JSOM�ۼ��Ͽ� ������ ����
	
	public void memberList_request() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\" : \"getMembers\"");
		sb.append("}");
		request_msg = sb.toString();
		System.out.println("Client_memberList_controller�� : request_msg : "+request_msg);	
		try {
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(request_msg+"\n");
			buffw.flush();
			System.out.println("���� �Ϸ�");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//�� �����κ��� �Ѱܹ��� [responseType] JSON �Ľ�
	
	public void memberList_parser(JsonElement obj) {
		// ģ���߰� ��ư Ŭ���� ��� ����� ����ϱ� ���� �޼���
			System.out.println("===== Get ALL Member answer recived=====");
			JsonElement dataElement = obj.getAsJsonObject().get("tableModel");
			JsonArray data = dataElement.getAsJsonArray();
			Gson gson = new Gson();
			model = gson.fromJson(data, Object[][].class);
			
			System.out.println("�� ClientThread �� ���� JSON : " + data);
			
			//��� ����� ���� Json �Ľ� �� �ٷ� �𵨿� �������ֱ�!
			mainFrame.search_Add_Friends.tableModel.data = model;
	}
	
	// TableModel �� ���Խ�ų ������ ���� �޼���
	public Object[][] getModel(){
		return model;
	}
}
