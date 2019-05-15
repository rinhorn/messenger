/*
 * 해야할 것
 * 1. 리턴값 넘겨주기
 * 
 * */

package server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import com.google.gson.JsonElement;

import server.ServerThread;
import server_DBConnection.AdminManager;

public class Server_deleteFriend_controller {
	
	AdminManager adminManager; // db에 접속할 때 쓸 connection 객체
	String user_pk; // 로그인한 사람
	String friend_pk; // 넘겨받은 친구의 아이디
	
	// client의 outputStrem을 이용해 삭제 결과 여부를 전송하기 위해 필요한 객체
	Socket client;
	
	boolean deleteFriend_flag; // 친구 삭제 여부를 알려주는 flag
	
	// 친구 data 를 GUI, 리스트, DB에서 삭제하는 method
	// 친구 삭제 controller 로 이동할 method
	public Server_deleteFriend_controller(AdminManager adminManager, Socket client) {
		this.adminManager= adminManager;
		this.client= client;
	}
	
	// 클라이언트에서 보낸 Json 파싱하기
	// 서버쓰레드에서 requestType이 delete라면 호출해주면서 매개변수로 obj 넘겨주기
	public void delete_parser(JsonElement obj){
		user_pk = obj.getAsJsonObject().get("user_pk").getAsString();
		friend_pk = obj.getAsJsonObject().get("friend_pk").getAsString();
		deleteFriend_sql();
		System.out.println("Server: 클라이언트에서 넘어온 user_PK: "+user_pk);
		System.out.println("Server: 클라이언트에서 넘어온 friend_pk: "+friend_pk);
	}
	
	// 파싱한 값을 가지고 DB에서 친구 삭제하기
	public void deleteFriend_sql() {
		// DB 에서 선택한 친구 data를 지움
		PreparedStatement pstmt = null;
		int result=0;
		String sql =  "delete from friends where member_me_fk="+user_pk+" and member_myFriend_fk="+friend_pk; 
		// friends 테이블에서 나컬럼명='user_id'가 같고 친구컬럼명 = '선택한 친구 라벨'이라면 ...... 
		System.out.println("DB에 친구 삭제 요청 직전입니다!");	
		try {
			pstmt= adminManager.con.prepareStatement(sql);
			result=pstmt.executeUpdate();
			System.out.println("친구 삭제 요청  SQL문은 "+sql);
			if(result!=0) {
				deleteFriend_flag=true;
				System.out.println("DB에서 친구 삭제 성공");
			}else {
				deleteFriend_flag=false;
				System.out.println("DB에서 친구 삭제 실패....ㅜㅜ");
			}
			delete_response();
			System.out.println("친구삭제 결과 보내는 delete_response() 호출중....");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	// 친구 삭제 성공 여부를 클라이언트로 보내줄 Json 작성
	public void delete_response() {
		StringBuffer sb= new StringBuffer();
		sb.append("{");
			sb.append("\"responseType\":\"delete\",");
			sb.append("\"flag\":\""+deleteFriend_flag+"\"");
		sb.append("}");
		
		String msg= sb.toString();
		System.out.println("deleteFriend의 Json은  "+msg);
		try {
			BufferedWriter buffw= new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(msg+"\n");
			buffw.flush();
			System.out.println("server : responseType 전송 성공!!!!");
			System.out.println("server : 클라이언트에 보낸 Json은 "+"responseType: delete, flag: "+deleteFriend_flag+" 입니다.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("server : responseType 전송 실패ㅜㅜㅜㅜ");
		}
	}
	
}	
	
	
	
	

