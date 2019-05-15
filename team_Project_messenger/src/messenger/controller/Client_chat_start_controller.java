package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.JsonElement;

import messenger.MainFrame;
import messenger.main.Main;

public class Client_chat_start_controller {
   Socket client;
   MainFrame mainFrame;
   String request_msg;
   String user_pk, chat_friend_pk, chat_friend_name;
   
   public Client_chat_start_controller(Socket client, MainFrame mainFrame) {
      this.client = client;
      this.mainFrame = mainFrame;
      
   }
   
   boolean flag;
   String room_num;
   
   public void chat_start_request(String chat_type, String user_pk ,String friend_pk, String friend_name) {
      StringBuffer sb = new StringBuffer();
      sb.append("{");
      sb.append("\"requestType\" : \"chat_start\",");
      sb.append("\"chat_type\" : \""+chat_type+"\",");
      sb.append("\"user_pk\" : \""+user_pk+"\",");
      sb.append("\"friend_pk\" : \""+friend_pk+"\",");
      sb.append("\"friend_name\" : \""+friend_name+"\"");
      sb.append("}");
      
      request_msg = sb.toString();
      System.out.println("Client_Chat_controller : Server 로 보내는 JSON : "+request_msg);
      
      try {
         BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
         System.out.println("Client_Chat_controller : Server 로 JSON 을 전송합니다.");
         buffw.write(request_msg+"\n");
         buffw.flush();
         System.out.println("Client_Chat_controller : Server 로 JSON 을 전송완료.");
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
 //★ 서버로부터 넘겨받은 [responseType] JSON 파싱
	
 	public void chat_start_parser(JsonElement obj) {
 		flag = obj.getAsJsonObject().get("flag").getAsBoolean();
 		user_pk = obj.getAsJsonObject().get("user_pk").getAsString();
 		chat_friend_pk = obj.getAsJsonObject().get("chat_friend_pk").getAsString();
 		chat_friend_name = obj.getAsJsonObject().get("chat_friend_name").getAsString();
 		room_num = obj.getAsJsonObject().get("room_num").getAsString();
 		System.out.println("client_chat_start_controller曰 : 클라인트로 결과 전송=> flag : "+flag+", room_num : "+room_num);
 		mainFrame.main.address_book.responseChatStart(flag, user_pk, chat_friend_pk, chat_friend_name, room_num);
 	}
}
   
