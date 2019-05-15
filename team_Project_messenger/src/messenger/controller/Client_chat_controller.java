package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.JsonElement;

import messenger.MainFrame;

public class Client_chat_controller {
	//Ŭ���̾�Ʈ�� ������ request ����
	String user_pk;
	String friend_pk;
	String room_num;
	String content_type;
	String content;
	
	//������ ������ response ����
	boolean flag;
	String talker_name;
	String send_time;
	
	//JSON���� ����
	String request_msg;
	BufferedWriter buffr;
	
	Socket client;
	MainFrame mainFrame;
	
	String talker_pk;
	
	public Client_chat_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	
	//�� Ŭ���̾�Ʈ���� ��û���� [requestType] JSOM�ۼ��Ͽ� ������ ����
	
	public void chat_request(String user_pk, String friend_pk, String room_num, String content_type, String content) {
		this.user_pk = user_pk;
		this.friend_pk = friend_pk;
		this.room_num = room_num;
		this.content_type = content_type;
		this.content = content;
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\":\"chat\",");
		sb.append("\"user_pk\":"+"\""+user_pk+"\",");
		sb.append("\"friend_pk\":"+"\""+friend_pk+"\",");
		sb.append("\"room_num\":"+"\""+room_num+"\",");
		sb.append("\"content_type\":"+"\""+content_type+"\",");
		sb.append("\"content\":"+"\""+content+"\"");
		sb.append("}");
		
		request_msg = sb.toString();
		System.out.println("Client_chat_controller�� : request_msg : "+request_msg);
		try {
			buffr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffr.write(request_msg+"\n");
			buffr.flush();
			System.out.println("client_chat_controller�� :������ ���� => user_pk : "+user_pk+", friend_pk : "+friend_pk+", room_num : "+room_num+", content_type : "+content_type+", content : "+content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//�� �����κ��� �Ѱܹ��� [responseType] JSON �Ľ�
	
	public void chat_parser(JsonElement obj) {
		flag = obj.getAsJsonObject().get("flag").getAsBoolean();
		room_num = obj.getAsJsonObject().get("room_num").getAsString();
		user_pk = obj.getAsJsonObject().get("user_pk").getAsString();
		talker_name = obj.getAsJsonObject().get("talker_name").getAsString();
		content_type = obj.getAsJsonObject().get("content_type").getAsString();
		content = obj.getAsJsonObject().get("content").getAsString();
		send_time = obj.getAsJsonObject().get("send_time").getAsString();
		System.out.println("client_chat_controller�� : Ŭ����Ʈ�� ��� ����=> flag : "+flag +", room_num"+room_num+",send_time : "+send_time+"talker_name : "+talker_name+", content_type : "+content_type+", content : "+content);
		//Ŭ���̾�Ʈ�� ����� �����ϴ� �ڵ� �����ϱ�!!!!!!!!!!!!!!!!!!!!!!!!!!
		mainFrame.main.address_book.responseChat(room_num, user_pk, talker_name, content_type, content, send_time);
		//System.out.println("Client_chat_controller : address_book�� �ִ� responseChatȣ�� �Ϸ�!!!!!!!");
	}
}
