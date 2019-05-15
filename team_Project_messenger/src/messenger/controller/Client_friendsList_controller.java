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
	
	//클라이언트가 보내온 request 정보
	String user_pk;
	
	//서버가 보내준 response 정보
	String flag;
	Object[] friends_pk;
	Object[] friends_name;
	
	public Client_friendsList_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	//★ 클라이언트부터 요청받은 [requestType] JSOM작성하여 서버로 전송
	
	public void friendsList_request(String user_pk) {
		this.user_pk = user_pk;
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\":\"friendsList\",");
		sb.append("\"user_pk\":"+"\""+user_pk+"\"");
		sb.append("}");
		
		request_msg = sb.toString();
		System.out.println("Client_friendsList_controller曰 : request_msg : "+request_msg);
		try {
			buffr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffr.write(request_msg+"\n");
			buffr.flush();
			System.out.println("client_friendsList_controller曰 : 서버로 전송 => user_pk : "+user_pk);
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
//	System.out.println("▶ ClientThread 에서 받은 friends_data 제이슨값 : " + friends_data);
//	System.out.println(allFriendID +"friends_data Object 변환");
//
//	main = new Main(client,this, friends, allFriendID);
//	main.setTitle(entered_name);
//	loginForm.dispose()
	//★ 서버로부터 넘겨받은 [responseType] JSON 파싱
	public void friendsList_parser(JsonElement obj) {
		flag = obj.getAsJsonObject().get("flag").getAsString();
		JsonElement friends_pk_json = obj.getAsJsonObject().get("friends_pk");
		JsonElement friends_name_json = obj.getAsJsonObject().get("friends_name");
		//JsonElement형 > JsonArray로 바꿈
		JsonArray friends_pk_data = friends_pk_json.getAsJsonArray();
		JsonArray friends_name_data = friends_name_json.getAsJsonArray();
		Gson gson = new Gson();
		friends_pk = gson.fromJson(friends_pk_data, Object[].class);
		friends_name = gson.fromJson(friends_name_data, Object[].class);
		//System.out.println("client_friendsList_controller曰 : 클라인트로 결과 전송=> flag : "+flag+", friends_pk : "+friends_pk[0]+", friends_name : "+friends_name[0].toString());
		
		List friends_pk_array = Arrays.asList(friends_pk);
		List friends_name_array = Arrays.asList(friends_name);
		
		
		mainFrame.setFriends_pk_array(friends_pk_array);
		mainFrame.setFriends_name_array(friends_name_array);	
		System.out.println("프랜즈리스트 파서에서 리스트 테스트 : "+mainFrame.getFriends_pk_array().size());
		System.out.println("프랜즈리스트 파서에서 리스트 테스트 : "+mainFrame.getFriends_name_array().size());
		
	}
}
