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

public class Server_changePW_controller {
	//서버측 컨트롤러가 기본적으로 보유할 객체
	AdminManager adminManager;
	ServerThread serverThread;
	Socket client;
	BufferedWriter buffw;
	
	//클라이언트가 보내준 정보(request)
	String user_id;
	String user_pw;
	
	//서버가 보내줄 정보(response)
	boolean flag;

	public Server_changePW_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;
	}
	
	// ★  클라이언트부터 넘겨받은 [requestType] JSON 파싱
	public void changePW_parser(JsonElement obj) {
		user_id = obj.getAsJsonObject().get("user_id").getAsString();
		user_pw = obj.getAsJsonObject().get("user_pw").getAsString();
		changePW_Query();
	}
	
	// SQL문을 통해 아이디, 비밀번호 체크
	public void changePW_Query() {
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql = "update member set pw='"+user_pw+"' where member_id='"+user_id+"'";
		
		try {
			pstmt = adminManager.con.prepareStatement(sql);
			int result = pstmt.executeUpdate();
			
			if(result==0) {
				System.out.println("Server_changePW_controller : changePW_Query() : SQL업데이트 실패");
				flag = false;
			}else {
				System.out.println("Server_changePW_controller : changePW_Query() : SQL업데이트 성공");
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt !=null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		changePW_response();
	} 
	
	// ★ 서버가 DB로부터 받은 결과를 보내줄 [responseType] JSON작성하여 클라이언트로 전송 
	public void changePW_response() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"responseType\": \"changePW\",");
		sb.append("\"flag\": \""+flag+"\",");
		sb.append("}");
		
		try {
			buffw = new BufferedWriter(new OutputStreamWriter (client.getOutputStream()));
			buffw.write(sb.toString()+"\n");
			buffw.flush();
			System.out.println("Server_changePW_controller曰 : client에게 보낼 결과값 => flag : "+flag+"");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}








