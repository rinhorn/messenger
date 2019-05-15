package server.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.print.attribute.standard.Severity;
import javax.swing.JOptionPane;

import com.google.gson.JsonElement;

import server.ServerThread;
//import messenger.main.Main;
import server_DBConnection.AdminManager;
		
//로그인 관련 서버측 컨트롤러
//하는 역할 : json 메세지의 [requestType]의 세부사항 파싱 및 DB로 SQL문 전송

public class Server_login_controller {
	//서버측 컨트롤러가 기본적으로 보유할 객체
	AdminManager adminManager;
	ServerThread serverThread;
	Socket client;
	BufferedWriter buffw;
	
	//클라이언트가 보내준 정보(request)
	String user_id;
	String user_pw;
	
	//서버가 보내줄 정보(response)
	String user_pk;	
	String user_name;	
	String flag;	

	public Server_login_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;
	}
	
	// ★  클라이언트부터 넘겨받은 [requestType] JSON 파싱
	public void login_parser(JsonElement obj) {
		user_id = obj.getAsJsonObject().get("user_id").getAsString();
		user_pw = obj.getAsJsonObject().get("user_pw").getAsString();
		login_Query();
	}
	
	// SQL문을 통해 아이디, 비밀번호 체크
	public void login_Query() {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql = "select * from member where member_id='"+user_id+"' and pw='"+user_pw+"'";
		try {
			pstmt = adminManager.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user_pk = rs.getString("seq_member");
				user_name = rs.getString("member_name");
				
				//로그인 수행이 가능하다면
				System.out.println("▶ 아이디,비밀번호 일치");
				flag = "true";
				//서버스레드의 고유값을 분류하기 위해 로그인 성공시 user_pk를 서버스레드가 보유하게끔!!
				serverThread.user_pk = user_pk;
				System.out.println("■■■■■■■ "+user_name + " 님 로그인 (아이디 : " + user_id +") ■■■■■■■");
			}else { //로그인 수행이 불가능하다면
				System.out.println("▶ 아이디와 비밀번호 불일치");
				flag = "false";
			}
			login_response(); //sql수행후의 응답내용을 전달할 메서드 호출
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// ★ 서버가 DB로부터 받은 결과를 보내줄 [responseType] JSON작성하여 클라이언트로 전송 
	public void login_response() {
		System.out.println("serverThread.user_pk : "+serverThread.user_pk);
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"responseType\": \"login\",");
		sb.append("\"flag\": \""+flag+"\",");
		sb.append("\"user_pk\": \""+user_pk+"\",");
		sb.append("\"user_name\": \""+user_name+"\"");
		sb.append("}");
		try {
			buffw = new BufferedWriter(new OutputStreamWriter (client.getOutputStream()));
			buffw.write(sb.toString()+"\n");
			buffw.flush();
			System.out.println("Server_login_controller曰 : client에게 보낼 결과값 => flag : "+flag+", user_pk : "+user_pk+", user_name : "+user_name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}








