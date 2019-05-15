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

// �α��� ������ ģ���� ��ϵ� ģ������ �����͸� DB�� �����Ͽ� �������� �۾��� ó���ϴ� Class 
public class Server_friendsList_controller {
	AdminManager adminManager;
	Socket client;
	
	//Ŭ���̾�Ʈ�� ������ ����(request)
	String user_pk;
	
	//������ ������ ����(response)
	String flag;
	List<String> friends_name = new ArrayList<String>();
	List<String> friends_pk = new ArrayList<String>();
	
	public Server_friendsList_controller(AdminManager adminManager, Socket client) {
		this.adminManager = adminManager;
		this.client = client;
	}
	
	// ��  Ŭ���̾�Ʈ���� �Ѱܹ��� [requestType] JSON �Ľ�
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
			
			//ģ�� ��� �ʱ�ȭ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			friends_name.clear();
			friends_pk.clear();
			
			while (rs.next()) {
				friends_name.add(rs.getString("member_name"));
				friends_pk.add(rs.getString("seq_member"));
			}
			if(rs != null) {
				System.out.println("�� ģ�� ��� ��� ����");
				flag = "true";	
				for(int i=0; i<friends_name.size(); i++) {				
					System.out.println("��� ģ���� PK�� "+friends_pk.get(i).toString());
					System.out.println("��� ģ���� �̸��� "+friends_name.get(i));
				}
			}else { 
				flag = "false";
				System.out.println("�� ģ�� ��� ��� �Ұ���");
			}

			friendsList_response(); //sql�������� ���䳻���� ������ �޼��� ȣ��
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
	// �� ������ DB�κ��� ���� ����� ������ [responseType] JSON�ۼ��Ͽ� Ŭ���̾�Ʈ�� ���� 
	public void friendsList_response() {
		BufferedWriter buffw=null;
		StringBuffer sb = new StringBuffer();
		Gson gson = new Gson();
		String friends_pk_data = gson.toJson(friends_pk);
		String friends_name_data = gson.toJson(friends_name);
		System.out.println("gson�� ���� �迭�� �������� : friends_pk_data ->"+friends_pk_data+", friends_name_data->"+friends_name_data);
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
			System.out.println("Server_freindsList_controller�� : client���� ���� ����� => flag : "+flag+", friends_pk : "+friends_pk_data+", friends_name : "+friends_name_data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public List<String> getFriends_pk(){
		return friends_pk;
	}
}
