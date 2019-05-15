package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.JsonElement;

import messenger.MainFrame;

public class Client_chat_controller {
	//클라이언트가 보내온 request 정보
	String user_pk;
	String friend_pk;
	String room_num;
	String content_type;
	String content;
	
	//서버가 보내준 response 정보
	boolean flag;
	String talker_name;
	String send_time;
	
	//JSON관련 변수
	String request_msg;
	BufferedWriter buffr;
	
	Socket client;
	MainFrame mainFrame;
	
	String talker_pk;
	
	public Client_chat_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	
	//★ 클라이언트부터 요청받은 [requestType] JSOM작성하여 서버로 전송
	
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
		System.out.println("Client_chat_controller曰 : request_msg : "+request_msg);
		try {
			buffr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffr.write(request_msg+"\n");
			buffr.flush();
			System.out.println("client_chat_controller曰 :서버로 전송 => user_pk : "+user_pk+", friend_pk : "+friend_pk+", room_num : "+room_num+", content_type : "+content_type+", content : "+content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//★ 서버로부터 넘겨받은 [responseType] JSON 파싱
	
	public void chat_parser(JsonElement obj) {
		flag = obj.getAsJsonObject().get("flag").getAsBoolean();
		room_num = obj.getAsJsonObject().get("room_num").getAsString();
		user_pk = obj.getAsJsonObject().get("user_pk").getAsString();
		talker_name = obj.getAsJsonObject().get("talker_name").getAsString();
		content_type = obj.getAsJsonObject().get("content_type").getAsString();
		content = obj.getAsJsonObject().get("content").getAsString();
		send_time = obj.getAsJsonObject().get("send_time").getAsString();
		System.out.println("client_chat_controller曰 : 클라인트로 결과 전송=> flag : "+flag +", room_num"+room_num+",send_time : "+send_time+"talker_name : "+talker_name+", content_type : "+content_type+", content : "+content);
		//클라이언트로 결과를 전송하는 코드 삽입하기!!!!!!!!!!!!!!!!!!!!!!!!!!
		mainFrame.main.address_book.responseChat(room_num, user_pk, talker_name, content_type, content, send_time);
		//System.out.println("Client_chat_controller : address_book에 있는 responseChat호출 완료!!!!!!!");
	}
}
