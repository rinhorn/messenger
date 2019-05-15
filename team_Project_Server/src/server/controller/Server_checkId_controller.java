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
	//������ ��Ʈ�ѷ��� �⺻������ ������ ��ü
	AdminManager adminManager;
	Socket client;
	BufferedWriter buffw;
	
	//Ŭ���̾�Ʈ�� ������ ����(request)
	String user_id;
	
	//������ ������ ����(response)
	String flag;

	BufferedReader buffr;
	
	public Server_checkId_controller(AdminManager adminManager, Socket client) {
		this.adminManager = adminManager;
		this.client = client;
		System.out.println("Server_checkId_controller ���� ����ϴ� Connection ��ü : "+this.adminManager.con);
	}
	
	// ��  Ŭ���̾�Ʈ���� �Ѱܹ��� [requestType] JSON �Ľ�
	public void checkId_parser(JsonElement obj) {
		user_id = obj.getAsJsonObject().get("user_id").getAsString();
		System.out.println("zzzzz"+user_id);
		checkId_Query();
	}
	
	// SQL���� ���� ������ �Է��� ���̵� �ߺ����� Ȯ��
	public void checkId_Query() {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "select * from member where member_id=?";
		try {
			pstmt = adminManager.con.prepareStatement(sql);
			pstmt.setString(1, user_id);
			rs = pstmt.executeQuery();
			System.out.println(sql);
			if(rs.next()) { //�ش� ���̵� DB�� �����Ѵٸ�(��, �� ���̵�� ����� �� ����)
				flag = "true";
			}else {
				flag = "false";
			}
			checkId_response(); //sql�������� ���䳻���� ������ �޼��� ȣ��
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
			System.out.println("Server_checkId_controller�� : client���� ���� ����� => flag : "+flag);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
