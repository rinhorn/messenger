package server.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonElement;

import server_DBConnection.AdminManager;

public class Server_checkId_controller {
	//서버측 컨트롤러가 기본적으로 보유할 객체
	AdminManager adminManager;
	Socket client;
	BufferedWriter buffw;
	
	//클라이언트가 보내준 정보(request)
	String user_id;
	
	//서버가 보내줄 정보(response)
	String flag;

	BufferedReader buffr;
	
	public Server_checkId_controller(AdminManager adminManager, Socket client) {
		this.adminManager = adminManager;
		this.client = client;
		System.out.println("Server_checkId_controller 에서 사용하는 Connection 객체 : "+this.adminManager.con);
	}
	
	// ★  클라이언트부터 넘겨받은 [requestType] JSON 파싱
	public void checkId_parser(JsonElement obj) {
		user_id = obj.getAsJsonObject().get("user_id").getAsString();
		System.out.println("zzzzz"+user_id);
		checkId_Query();
	}
	
	// SQL문을 통해 유저가 입력한 아이디가 중복인지 확인
	public void checkId_Query() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from member where member_id=?";
		try {
			pstmt = adminManager.con.prepareStatement(sql);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();
			System.out.println(sql);
			if(rs.next()) { //해당 아이디를 DB가 보유한다면(즉, 이 아이디는 사용할 수 없음)
				flag = "true";
			}else {
				flag = "false";
			}
			checkId_response(); //sql수행후의 응답내용을 전달할 메서드 호출
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
	public void checkId_response() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"responseType\" : \"checkId\",");
		sb.append("\"flag\" : \""+flag+"\"");
		sb.append("}");
		try {
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(sb.toString()+"\n");
			buffw.flush();
			System.out.println("Server_checkId_controller曰 : client에게 보낼 결과값 => flag : "+flag);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
