/*
 * ����
 * 1. ������ ���� JSON �ۼ��ؼ� 
 * 2. �������� ���� JSON �Ľ��ؼ� ������� address_book�� �����ֱ�
 * */

package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.JsonElement;

import messenger.MainFrame;

public class Client_deleteFriend_controller {
	MainFrame mainFrame;
	Socket client;
	
	String user_pk; 
	String friend_pk;
	List temp_pk = new ArrayList();
	List temp_name = new ArrayList();
	
	public Client_deleteFriend_controller(Socket client, MainFrame mainFrame) {
		this.client= client;
		this.mainFrame= mainFrame;
	}
	// ������ ���� JSON  �ۼ��ϱ�
	// address_book�� ������ 'ģ������ ��ư'�� ������ ȣ���� ��
	public void delete_request(String user_pk, String friend_pk) {
		this.user_pk = user_pk;
		this.friend_pk= friend_pk;
		
		System.out.println("Client_deleteFriend_controller���� ���� ����PK: "+user_pk);
		System.out.println("Client_deleteFriend_controller���� ���� ģ��PK: "+friend_pk);
		StringBuffer sb= new StringBuffer();
		
		sb.append("{");
		sb.append("\"requestType\": \"delete\",");
		sb.append("\"user_pk\": \""+user_pk+"\",");
		sb.append("\"friend_pk\": \""+friend_pk+"\"");
		sb.append("}");
		
		String msg= sb.toString();
		System.out.println("Client_deleteFriend_controller���� ���� Json����: "+msg);
		
		try {
			BufferedWriter buffw =new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(msg+"\n");
			buffw.flush();
			System.out.println("client ��Ʈ�ѷ� -- ģ������ json ���ۿ�û");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("client ��Ʈ�ѷ� -- ģ������ json ���ۿ�û ����");
		}
	}
	
	// client_thread���� �Ľ��� request���� 'delete'�� ��� ȣ���ϱ�
	// json �Ľ��ؼ� �Ѿ�� �� �ؼ�
	public void delete_response(JsonElement obj) {
		
		String flag = obj.getAsJsonObject().get("flag").getAsString();
		System.out.println("client : �������� �Ѿ�� ���� ���� ����= "+flag);
		
		if(flag.equals(flag)) {
			System.out.println("ģ�� ���� ���δ� "+flag+", �� �Ѿ�� �ִ��� Ȯ����");
			JOptionPane.showMessageDialog(mainFrame.address_book, "ģ�� ���� ����");
			
			mainFrame.client_friendsList_controller.friendsList_request(mainFrame.getUser_pk());
			
//			if(temp_pk.size() != 0) {
//				for(int i=0; i<mainFrame.getFriends_pk_array().size(); i++) {
//					temp_pk.remove(i);
//					temp_name.remove(i);
//				}
//			}
//			
//			for(int i=0; i<mainFrame.getFriends_pk_array().size(); i++) {
//				temp_pk.add(mainFrame.getFriends_pk_array().get(i));
//				temp_name.add(mainFrame.getFriends_name_array().get(i));
//			}
//			
//			mainFrame.setFriends_pk_array(temp_pk);
//			mainFrame.setFriends_name_array(temp_name);
			
			System.out.println("������pk ����Ʈ �迭(clear ��) ũ�� : "+mainFrame.getFriends_pk_array().size());
			System.out.println("������name ����Ʈ �迭(clear ��) ũ�� : "+mainFrame.getFriends_name_array().size());
			//////////////////////////////////////////////////////////////////////////
			// ģ�� ��� ����ϴ� �޼��� ȣ��!!!!
			
			/////////////////////////////////////////////////////////////////////////
		}else {
			System.out.println("ģ�� ���� ���δ� "+flag+", �� �Ѿ�� �ִ��� Ȯ����");
			JOptionPane.showMessageDialog(mainFrame.address_book, "ģ�� ���� ����");
		}
	}
}














