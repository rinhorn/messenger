/*
 * Server 측 친구 등록 관련 컨트롤러 정의 		--	2019-02-13
 */
package server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonElement;

import server.ServerThread;
import server_DBConnection.AdminManager;

public class Server_AddFriend_controller {
	
	//접속자와 요청사항에 대해 검사를 위한 변수 ( PK 담을 변수 )
	String user_pk;
	String friend_pk;
	
	//서버가 보내줄 정보(response)
	Boolean flag;
	
	AdminManager adminManager;
	Socket client;
	ServerThread serverThread;
	
	BufferedWriter buffw;
	
	// ♣ 생성자
	public Server_AddFriend_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;
	}
	
	// ♣ Client 측에서 받은 PK값 Json Parsing 후 변수 초기화! 
	public void addFriend_parser(JsonElement obj) {
		friend_pk = obj.getAsJsonObject().get("friend_pk").getAsString();
		user_pk = serverThread.user_pk;
	}
	
	// ♣ DB측 관련 쿼리
	public void addFriend_query() {
		PreparedStatement pstmt = null;
		
		String sql = "insert into friends(seq_friends, member_me_fk, member_myfriend_fk)";
		sql+=" values(seq_friends.nextval,"+user_pk+","+friend_pk+")";
		
		
		try {
			pstmt = adminManager.con.prepareStatement(sql); 
			boolean result = pstmt.execute();
			if(result) { //참이면 select문 실행
				System.out.println("addFriend_parser : insert문 쿼리 실패");
				flag = false;
			}else { //거짓이면 DML실행
				System.out.println("addFriend_parser : insert문 쿼리 성공");
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	// ♣ 모든걸 처리후 Client 측에 보낼 JSON
	public void addFriend_response() {
		String response_msg;
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"responseType\":\"acceptAdd\",");
		sb.append("\"flag\":"+flag+"");
		sb.append("}");
		
		response_msg = sb.toString();
		
		System.out.println("▶ Server_AddFriend_controller : "+response_msg);
		
		try {
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(response_msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
