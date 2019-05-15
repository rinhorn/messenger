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

//����ڰ� �Է������� �Ѱ��ָ� �ش� ������ DB�� ������ �ִ���
//sql���� ���� Ȯ�����ִ� ��Ʈ�ѷ�

public class Server_FindId_controller {

	// ���� ���� ���� ����
	String user_id;
	String user_name;
	String user_email;
	String user_birth;

	Socket client;
	AdminManager adminManager;
	ServerThread serverThread;

	// ȸ������ ��ġ ���� ����� ���� ����
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

		// �����κ��� �޾ƿ� �̸�, ����, ������ ȸ��DB�� ��ġ�ϴٸ� ���̵��� ��������
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
			System.out.println("Server_FindId_controller : ���� �߼� ����");
		}else {
			System.out.println("Server_FindId_controller : ���� �߼� ����");
		}
	}
	// ������� true��� Ŭ���̾�Ʈ ������� ����� �����ϱ�����
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
