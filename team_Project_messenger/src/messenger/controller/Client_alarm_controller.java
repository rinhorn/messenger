package messenger.controller;

import java.io.BufferedWriter;
import java.net.Socket;

import com.google.gson.JsonElement;

import messenger.MainFrame;

//Server_chat_controller�κ��� ģ������ ������ responseType�� �Ľ����ִ� �޼���

public class Client_alarm_controller {
	
	//������ ������ response ����
	String talker_name; //���ϴ� ȭ���� �̸�
	String send_time;
	String room_num;
	String content_type;
	String content;
	String talker_pk;
	
	//JSON���� ����
	String request_msg;
	BufferedWriter buffr;
	
	Socket client;
	MainFrame mainFrame;
	int chat_count;
	public Client_alarm_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	//�� �����κ��� �Ѱܹ��� [responseType] JSON �Ľ�
	
	public void alarm_parser(JsonElement obj) {
		room_num = obj.getAsJsonObject().get("room_num").getAsString();
		talker_pk = obj.getAsJsonObject().get("talker_pk").getAsString();
		talker_name = obj.getAsJsonObject().get("talker_name").getAsString();
		content_type = obj.getAsJsonObject().get("content_type").getAsString();
		content = obj.getAsJsonObject().get("content").getAsString();
		send_time = obj.getAsJsonObject().get("send_time").getAsString();
		System.out.println("client_alarm_controller�� : �����κ��� ���� ����=> room_num"+room_num+", user_pk :"+talker_pk+", send_time : "+send_time+"talker_name : "+talker_name+", content_type : "+content_type+", content : "+content);
		//Ŭ���̾�Ʈ�� ����� �����ϴ� �ڵ� �����ϱ�!!!!!!!!!!!!!!!!!!!!!!!!!!
		sending_chat();
	}
	
	public void sending_chat() {
		//ȭ�� �����ϱ�
		//if(talker_pk == mainFrame.getUser_pk()) { //ȭ�� --> �α����ѻ��
		//	mainFrame.main.address_book.chat
		//}
		chat_count++;
		if(chat_count==0) {
			mainFrame.main.address_book.responseChat(room_num, talker_pk, talker_name, content_type, content, send_time);
			System.out.println("Client_alarm_controller : address_book�� �ִ� responseChatȣ�� �Ϸ�!!!!!!!");
		}else {
			for(int i=0; i<mainFrame.main.address_book.chat_window_array.size(); i++) {
				if(mainFrame.main.address_book.chat_window_array.get(i).room_num == mainFrame.main.address_book.personBox_array.get(i).room_num) {
					mainFrame.main.address_book.chat_window_array.get(i).ta_chat_box.append(content);
				};
			}
		}
	}
}
