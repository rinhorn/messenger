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
import server.sendingEmail.SendingEmail;
import server_DBConnection.AdminManager;

//사용자가 입력정보를 넘겨주면 해당 정보를 DB가 가지고 있는지
//sql문을 통해 확인해주는 컨트롤러

public class Server_FindId_controller {

	// 유저 정보 관련 변수
	String user_id;
	String user_name;
	String user_email;
	String user_birth;

	Socket client;
	AdminManager adminManager;
	ServerThread serverThread;

	// 회원정보 일치 여부 결과를 담을 변수
	boolean flag;

	public Server_FindId_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;
	}

	public void findId_query(JsonElement obj) {

		user_name = obj.getAsJsonObject().get("user_name").getAsString();
		user_birth = obj.getAsJsonObject().get("user_birth").getAsString();
		user_email = obj.getAsJsonObject().get("user_email").getAsString();

		// 유저로부터 받아온 이름, 생일, 메일이 회원DB와 일치하다면 아이디값을 가져오기
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql = "select * from member where member_name='" + user_name + "' and birth='" + user_birth
				+ "' and email='" + user_email + "'";
		System.out.println(sql);
		
		try {
			pstmt = adminManager.con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			
			rs.beforeFirst();
			
			if(rs.next()) {
				flag = true;
				user_id = rs.getString("member_id");
			}else {
				flag = false;
				user_id = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		findId_response();
		
		if(flag) {
			serverThread.sendingEmail.sendMailForID(user_id, user_name, user_email);
			System.out.println("Server_FindId_controller : 메일 발송 성공");
		}else {
			System.out.println("Server_FindId_controller : 메일 발송 실패");
		}
	}
	// 결과물이 true라면 클라이언트 쓰레드로 결과물 전송하기위함
	public void findId_response() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"responseType\": \"findId\",");
		sb.append("\"flag\": \""+flag+"\"");
		sb.append("}");
		
		String response_msg = sb.toString();
		
		try {
			BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(response_msg +"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
