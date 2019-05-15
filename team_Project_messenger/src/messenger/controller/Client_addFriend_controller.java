/*
 * Client �� ģ�� ��� ���� ��Ʈ�ѷ� ���� 		--	2019-02-13
 */
package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.google.gson.JsonElement;

import messenger.MainFrame;

public class Client_addFriend_controller {
	
	// User �� Friend ����
	String friend_pk;
	
	// JSON ����
	String request_msg;
	BufferedWriter buffw;

	Socket client;
	MainFrame mainFrame;
	
	//�����κ��� ���� �����
	Boolean flag;
	
	// �� ������
	public Client_addFriend_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	// �� Client �� Server�� JSON ����!!
	public void addFriend_reqeust() {
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"requestType\":\"addFriend\",");
		sb.append("\"friend_pk\":\""+friend_pk+"\"");
		sb.append("}");
		
		request_msg = sb.toString();
		
		System.out.println("�� Client_addFriend_controller : "+request_msg);
		
		try {
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(request_msg + "\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	// �� JSON Parsing ���� �޼���
	public void addFriend_parser(JsonElement obj) {
		flag = obj.getAsJsonObject().get("flag").getAsBoolean();
		System.out.println("�ڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡڡ�client_login_controller�� : Ŭ����Ʈ�� ��� ����=> flag : "+flag);
		if(flag) {
			mainFrame.client_friendsList_controller.friendsList_request(mainFrame.getUser_pk());
			JOptionPane.showMessageDialog(mainFrame.main, "�� ģ�� �߰� ���� ��");
		}else {
			JOptionPane.showMessageDialog(mainFrame.main, "�� ģ�� �߰� ���� ��");
		}
		////////////////////ģ����� ������ ��///////////////
	}
	
	// Setting
	public void setFriendPk(String friend_pk) {
		this.friend_pk = friend_pk;
	}
	
	
}
