package server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import server.ServerThread;
import server_DBConnection.AdminManager;

public class Server_GetAllMemberForAdd_controller {
	String user_pk;
	List<String> friends_pk = new ArrayList<String>();
	
	AdminManager adminManager;
	Socket client;
	String[][] data;

	BufferedWriter buffw;
	ServerThread serverThread;

	public Server_GetAllMemberForAdd_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;
	}

	public void gettingMemberQuery() {
		user_pk =serverThread.user_pk;
		friends_pk = serverThread.friends_pk;
		System.out.println("Server_GetAllMemberForAdd_controller : user_pk : "+user_pk);
		System.out.println("Server_GetAllMemberForAdd_controller : friends_pk : "+friends_pk);
		
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<friends_pk.size();i++) {
			sb.append(" and");
			sb.append(" seq_member!= ");
			sb.append("'"+friends_pk.get(i)+"'");
		}
		System.out.println(sb.toString());
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select seq_member, member_id, member_name from member where seq_member != "+"'"+user_pk+"'"+sb.toString()+"";
		System.out.println("Server_GetAllMemberForAdd_controller : sql : "+sql);

		try {
			pstmt = adminManager.con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			ResultSetMetaData meta = rs.getMetaData();

			int col = meta.getColumnCount();

			rs.last();
			int row = rs.getRow();
			rs.beforeFirst();

			data = new String[row][col];
			for (int i = 0; i < row; i++) {
				rs.next();
				for (int k = 0; k < col; k++) {
					data[i][k] = rs.getString(k + 1);
				}
			}
		} catch (SQLException e) {
			System.out.println("오류위치" + this.getClass().getName());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// json 의 requestType 의 키값이 addFriends 일 경우 수행하는 메서드!
	public void getAllMeber_parsing(JsonElement obj) {
		StringBuffer get_all_member_processing_json = new StringBuffer();
		String result = null;

		Gson gson = new Gson();
		String data_json = gson.toJson(data);

		
		System.out.println(data_json);

		get_all_member_processing_json.append("{");
		get_all_member_processing_json.append("\"responseType\":\"memberList\",");
		get_all_member_processing_json.append("\"tableModel\":" + data_json);
		get_all_member_processing_json.append("}");
		result = get_all_member_processing_json.toString();

		try {
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(result + "\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}