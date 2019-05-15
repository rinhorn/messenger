package server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import server_DBConnection.AdminManager;

// 로그인 성공시 친구로 등록된 친구들의 데이터를 DB에 접근하여 가져오는 작업을 처리하는 Class 
public class Server_friendsList_controller {
	AdminManager adminManager;
	Socket client;
	
	//클라이언트가 보내준 정보(request)
	String user_pk;
	
	//서버가 보내줄 정보(response)
	String flag;
	List<String> friends_name = new ArrayList<String>();
	List<String> friends_pk = new ArrayList<String>();
	
	public Server_friendsList_controller(AdminManager adminManager, Socket client) {
		this.adminManager = adminManager;
		this.client = client;
	}
	
	// ★  클라이언트부터 넘겨받은 [requestType] JSON 파싱
	public void friendsList_parser(JsonElement obj) {
		user_pk = obj.getAsJsonObject().get("user_pk").getAsString();
		friendsList_Query();
	}
	
	public void friendsList_Query() {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql = "select m.member_name , m.seq_member from friends fr, member m "
				+ "where fr.member_me_fk = "+user_pk+" and fr.member_myfriend_fk = m.seq_member";
		try {
			pstmt = adminManager.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			System.out.println(sql);
			
			//친구 목록 초기화!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			friends_name.clear();
			friends_pk.clear();
			
			while (rs.next()) {
				friends_name.add(rs.getString("member_name"));
				friends_pk.add(rs.getString("seq_member"));
			}
			if(rs != null) {
				System.out.println("▶ 친구 목록 출력 가능");
				flag = "true";	
				for(int i=0; i<friends_name.size(); i++) {				
					System.out.println("모든 친구의 PK는 "+friends_pk.get(i).toString());
					System.out.println("모든 친구의 이름은 "+friends_name.get(i));
				}
			}else { 
				flag = "false";
				System.out.println("▶ 친구 목록 출력 불가능");
			}

			friendsList_response(); //sql수행후의 응답내용을 전달할 메서드 호출
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
	public void friendsList_response() {
		BufferedWriter buffw=null;
		StringBuffer sb = new StringBuffer();
		Gson gson = new Gson();
		String friends_pk_data = gson.toJson(friends_pk);
		String friends_name_data = gson.toJson(friends_name);
		System.out.println("gson을 통해 배열을 가져오기 : friends_pk_data ->"+friends_pk_data+", friends_name_data->"+friends_name_data);
		sb.append("{");
		sb.append("\"responseType\": \"friendsList\",");
		sb.append("\"flag\": \""+flag+"\",");
		sb.append("\"friends_pk\": "+friends_pk_data+",");
		sb.append("\"friends_name\": "+friends_name_data+"");
		sb.append("}");
		
		try {
			buffw = new BufferedWriter(new OutputStreamWriter (client.getOutputStream()));
			buffw.write(sb.toString()+"\n");
			buffw.flush();
			System.out.println("Server_freindsList_controller曰 : client에게 보낼 결과값 => flag : "+flag+", friends_pk : "+friends_pk_data+", friends_name : "+friends_name_data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<String> getFriends_pk(){
		return friends_pk;
	}
}
