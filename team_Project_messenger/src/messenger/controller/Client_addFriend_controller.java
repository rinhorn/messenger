/*
 * Client 내 친구 등록 관련 컨트롤러 정의 		--	2019-02-13
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
	
	// User 및 Friend 정보
	String friend_pk;
	
	// JSON 관련
	String request_msg;
	BufferedWriter buffw;

	Socket client;
	MainFrame mainFrame;
	
	//서버로부터 받은 결과값
	Boolean flag;
	
	// ♣ 생성자
	public Client_addFriend_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	// ♣ Client → Server로 JSON 전송!!
	public void addFriend_reqeust() {
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"requestType\":\"addFriend\",");
		sb.append("\"friend_pk\":\""+friend_pk+"\"");
		sb.append("}");
		
		request_msg = sb.toString();
		
		System.out.println("▶ Client_addFriend_controller : "+request_msg);
		
		try {
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(request_msg + "\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	// ♣ JSON Parsing 관련 메서드
	public void addFriend_parser(JsonElement obj) {
		flag = obj.getAsJsonObject().get("flag").getAsBoolean();
		System.out.println("★★★★★★★★★★★★★★★★★★★★★★★★★client_login_controller曰 : 클라인트로 결과 전송=> flag : "+flag);
		if(flag) {
			mainFrame.client_friendsList_controller.friendsList_request(mainFrame.getUser_pk());
			JOptionPane.showMessageDialog(mainFrame.main, "※ 친구 추가 성공 ※");
		}else {
			JOptionPane.showMessageDialog(mainFrame.main, "※ 친구 추가 실패 ※");
		}
		////////////////////친구목록 갱신할 곳///////////////
	}
	
	// Setting
	public void setFriendPk(String friend_pk) {
		this.friend_pk = friend_pk;
	}
	
	
}
