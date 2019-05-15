package messenger.controller;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import messenger.MainFrame;

public class Client_friendsList_controller {
	MainFrame mainFrame;
	Socket client;
	
	String request_msg;
	
	BufferedWriter buffr;
	
	//Ŭ���̾�Ʈ�� ������ request ����
	String user_pk;
	
	//������ ������ response ����
	String flag;
	Object[] friends_pk;
	Object[] friends_name;
	
	public Client_friendsList_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	//�� Ŭ���̾�Ʈ���� ��û���� [requestType] JSOM�ۼ��Ͽ� ������ ����
	
	public void friendsList_request(String user_pk) {
		this.user_pk = user_pk;
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\":\"friendsList\",");
		sb.append("\"user_pk\":"+"\""+user_pk+"\"");
		sb.append("}");
		
		request_msg = sb.toString();
		System.out.println("Client_friendsList_controller�� : request_msg : "+request_msg);
		try {
			buffr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffr.write(request_msg+"\n");
			buffr.flush();
			System.out.println("client_friendsList_controller�� : ������ ���� => user_pk : "+user_pk);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//	System.out.println("==== get_friends recived ====");
//	JsonElement friends_dataElement = obj.getAsJsonObject().get("friends_data");
//	JsonElement friend_ID_Element = obj.getAsJsonObject().get("AllFriendID");
//	JsonArray friends_data = friends_dataElement.getAsJsonArray();
//	JsonArray friend_ID_data = friend_ID_Element.getAsJsonArray();
//	Gson gson = new Gson();
//	friends = gson.fromJson(friends_data, Object[].class);
//	allFriendID = gson.fromJson(friend_ID_data, Object[].class);
//	System.out.println("�� ClientThread ���� ���� friends_data ���̽��� : " + friends_data);
//	System.out.println(allFriendID +"friends_data Object ��ȯ");
//
//	main = new Main(client,this, friends, allFriendID);
//	main.setTitle(entered_name);
//	loginForm.dispose()
	//�� �����κ��� �Ѱܹ��� [responseType] JSON �Ľ�
	public void friendsList_parser(JsonElement obj) {
		flag = obj.getAsJsonObject().get("flag").getAsString();
		JsonElement friends_pk_json = obj.getAsJsonObject().get("friends_pk");
		JsonElement friends_name_json = obj.getAsJsonObject().get("friends_name");
		//JsonElement�� > JsonArray�� �ٲ�
		JsonArray friends_pk_data = friends_pk_json.getAsJsonArray();
		JsonArray friends_name_data = friends_name_json.getAsJsonArray();
		Gson gson = new Gson();
		friends_pk = gson.fromJson(friends_pk_data, Object[].class);
		friends_name = gson.fromJson(friends_name_data, Object[].class);
		//System.out.println("client_friendsList_controller�� : Ŭ����Ʈ�� ��� ����=> flag : "+flag+", friends_pk : "+friends_pk[0]+", friends_name : "+friends_name[0].toString());
		
		List friends_pk_array = Arrays.asList(friends_pk);
		List friends_name_array = Arrays.asList(friends_name);
		
		
		mainFrame.setFriends_pk_array(friends_pk_array);
		mainFrame.setFriends_name_array(friends_name_array);	
		System.out.println("�������Ʈ �ļ����� ����Ʈ �׽�Ʈ : "+mainFrame.getFriends_pk_array().size());
		System.out.println("�������Ʈ �ļ����� ����Ʈ �׽�Ʈ : "+mainFrame.getFriends_name_array().size());
		
	}
}
