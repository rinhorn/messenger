/*
 * 역할
 * 1. 서버에 보낼 JSON 작성해서 
 * 2. 서버에서 받은 JSON 파싱해서 결과값을 address_book에 보내주기
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
	// 서버에 보낼 JSON  작성하기
	// address_book이 보유한 '친구삭제 버튼'을 누르면 호출할 것
	public void delete_request(String user_pk, String friend_pk) {
		this.user_pk = user_pk;
		this.friend_pk= friend_pk;
		
		System.out.println("Client_deleteFriend_controller에서 받은 유저PK: "+user_pk);
		System.out.println("Client_deleteFriend_controller에서 받은 친구PK: "+friend_pk);
		StringBuffer sb= new StringBuffer();
		
		sb.append("{");
		sb.append("\"requestType\": \"delete\",");
		sb.append("\"user_pk\": \""+user_pk+"\",");
		sb.append("\"friend_pk\": \""+friend_pk+"\"");
		sb.append("}");
		
		String msg= sb.toString();
		System.out.println("Client_deleteFriend_controller에서 찍은 Json문장: "+msg);
		
		try {
			BufferedWriter buffw =new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(msg+"\n");
			buffw.flush();
			System.out.println("client 컨트롤러 -- 친구삭제 json 전송요청");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("client 컨트롤러 -- 친구삭제 json 전송요청 실패");
		}
	}
	
	// client_thread에서 파싱한 request값이 'delete'일 경우 호출하기
	// json 파싱해서 넘어온 값 해석
	public void delete_response(JsonElement obj) {
		
		String flag = obj.getAsJsonObject().get("flag").getAsString();
		System.out.println("client : 서버에서 넘어온 삭제 성공 여부= "+flag);
		
		if(flag.equals(flag)) {
			System.out.println("친구 삭제 여부는 "+flag+", 잘 넘어가고 있는지 확인중");
			JOptionPane.showMessageDialog(mainFrame.address_book, "친구 삭제 성공");
			
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
			
			System.out.println("프렌드pk 리스트 배열(clear 후) 크기 : "+mainFrame.getFriends_pk_array().size());
			System.out.println("프렌드name 리스트 배열(clear 후) 크기 : "+mainFrame.getFriends_name_array().size());
			//////////////////////////////////////////////////////////////////////////
			// 친구 목록 출력하는 메서드 호출!!!!
			
			/////////////////////////////////////////////////////////////////////////
		}else {
			System.out.println("친구 삭제 여부는 "+flag+", 잘 넘어가고 있는지 확인중");
			JOptionPane.showMessageDialog(mainFrame.address_book, "친구 삭제 실패");
		}
	}
}














