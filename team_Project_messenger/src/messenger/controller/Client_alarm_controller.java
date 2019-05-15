package messenger.controller;

import java.io.BufferedWriter;
import java.net.Socket;

import com.google.gson.JsonElement;

import messenger.MainFrame;

//Server_chat_controller로부터 친구에게 보내줄 responseType을 파싱해주는 메서드

public class Client_alarm_controller {
	
	//서버가 보내준 response 정보
	String talker_name; //말하는 화자의 이름
	String send_time;
	String room_num;
	String content_type;
	String content;
	String talker_pk;
	
	//JSON관련 변수
	String request_msg;
	BufferedWriter buffr;
	
	Socket client;
	MainFrame mainFrame;
	int chat_count;
	public Client_alarm_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	//★ 서버로부터 넘겨받은 [responseType] JSON 파싱
	
	public void alarm_parser(JsonElement obj) {
		room_num = obj.getAsJsonObject().get("room_num").getAsString();
		talker_pk = obj.getAsJsonObject().get("talker_pk").getAsString();
		talker_name = obj.getAsJsonObject().get("talker_name").getAsString();
		content_type = obj.getAsJsonObject().get("content_type").getAsString();
		content = obj.getAsJsonObject().get("content").getAsString();
		send_time = obj.getAsJsonObject().get("send_time").getAsString();
		System.out.println("client_alarm_controller曰 : 서버로부터 받은 내용=> room_num"+room_num+", user_pk :"+talker_pk+", send_time : "+send_time+"talker_name : "+talker_name+", content_type : "+content_type+", content : "+content);
		//클라이언트로 결과를 전송하는 코드 삽입하기!!!!!!!!!!!!!!!!!!!!!!!!!!
		sending_chat();
	}
	
	public void sending_chat() {
		//화자 구분하기
		//if(talker_pk == mainFrame.getUser_pk()) { //화자 --> 로그인한사람
		//	mainFrame.main.address_book.chat
		//}
		chat_count++;
		if(chat_count==0) {
			mainFrame.main.address_book.responseChat(room_num, talker_pk, talker_name, content_type, content, send_time);
			System.out.println("Client_alarm_controller : address_book에 있는 responseChat호출 완료!!!!!!!");
		}else {
			for(int i=0; i<mainFrame.main.address_book.chat_window_array.size(); i++) {
				if(mainFrame.main.address_book.chat_window_array.get(i).room_num == mainFrame.main.address_book.personBox_array.get(i).room_num) {
					mainFrame.main.address_book.chat_window_array.get(i).ta_chat_box.append(content);
				};
			}
		}
	}
}
