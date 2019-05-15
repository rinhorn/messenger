package server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonElement;

import server.ServerThread;
import server_DBConnection.AdminManager;

//서버측 '가입'관련 컨트롤러

public class Server_regist_controller {
	AdminManager adminManager;
	Socket client;
	ServerThread serverThread;
	String member_id;
	String member_name;
	String pw;
	String birth;
	String email;
	String profile;
	
	Connection conn;
	
	public Server_regist_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;
	}
	
	
	public void regist_parser(JsonElement obj) {
		member_id = obj.getAsJsonObject().get("member_id").getAsString();
		member_name = obj.getAsJsonObject().get("member_name").getAsString();
		pw = obj.getAsJsonObject().get("pw").getAsString();
		birth = obj.getAsJsonObject().get("birth").getAsString();
		email = obj.getAsJsonObject().get("email").getAsString();
		profile = obj.getAsJsonObject().get("profile").getAsString();
		System.out.println("Server_regist_controller : obj parsing member_id : "+member_id);
		System.out.println("Server_regist_controller : obj parsing member_name : "+member_name);
		System.out.println("Server_regist_controller : obj parsing pw : "+pw);
		System.out.println("Server_regist_controller : obj parsing birth : "+birth);
		System.out.println("Server_regist_controller : obj parsing email : "+email);
		regist_Query();
	}
	
	public void regist_Query() {
		PreparedStatement pstmt;
		String sql = "insert into member(seq_member, member_id, member_name, pw, birth, email, profile) values(seq_member.nextval, ?, ?, ?, ?, ?, ?)";
		
		System.out.println("Server_regist_controller : adminManager 에서 얻어온 Conection 객체 : "+adminManager.con);
		conn = adminManager.con;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member_id);
			pstmt.setString(2, member_name);
			pstmt.setString(3, pw);
			pstmt.setString(4, birth);
			pstmt.setString(5, email);
			pstmt.setString(6, profile);
			
			System.out.println("Server_regist_controller : DataBase 에 회원등록 Insert 문을 수행합니다.");
			int result = pstmt.executeUpdate();
			
			if(result != 0) {
				System.out.println("Server_regist_controller : DataBase 전송완료");
				regist_response(true);
			}else {
				System.out.println("Server_regist_controller : DataBase 전송실패");
				regist_response(false);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	// ★ 서버가 DB로부터 받은 결과를 보내줄 [responseType] JSON작성하여 클라이언트로 전송 
		public void regist_response(boolean flag) {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("\"responseType\" : \"regist\",");
			sb.append("\"flag\" : \""+flag+"\"");
			sb.append("}");
			try {
				BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
				buffw.write(sb.toString()+"\n");
				buffw.flush();
				System.out.println("Server_checkId_controller曰 : client에게 보낼 결과값 => flag : "+flag);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
