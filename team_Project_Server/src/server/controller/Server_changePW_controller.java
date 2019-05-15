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
		
//�α��� ���� ������ ��Ʈ�ѷ�
//�ϴ� ���� : json �޼����� [requestType]�� ���λ��� �Ľ� �� DB�� SQL�� ����

public class Server_changePW_controller {
	//������ ��Ʈ�ѷ��� �⺻������ ������ ��ü
	AdminManager adminManager;
	ServerThread serverThread;
	Socket client;
	BufferedWriter buffw;
	
	//Ŭ���̾�Ʈ�� ������ ����(request)
	String user_id;
	String user_pw;
	
	//������ ������ ����(response)
	boolean flag;

	public Server_changePW_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;
	}
	
	// ��  Ŭ���̾�Ʈ���� �Ѱܹ��� [requestType] JSON �Ľ�
	public void changePW_parser(JsonElement obj) {
		user_id = obj.getAsJsonObject().get("user_id").getAsString();
		user_pw = obj.getAsJsonObject().get("user_pw").getAsString();
		changePW_Query();
	}
	
	// SQL���� ���� ���̵�, ��й�ȣ üũ
	public void changePW_Query() {
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		
		String sql = "update member set pw='"+user_pw+"' where member_id='"+user_id+"'";
		
		try {
			pstmt = adminManager.con.prepareStatement(sql);
			int result = pstmt.executeUpdate();
			
			if(result==0) {
				System.out.println("Server_changePW_controller : changePW_Query() : SQL������Ʈ ����");
				flag = false;
			}else {
				System.out.println("Server_changePW_controller : changePW_Query() : SQL������Ʈ ����");
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
	
	// �� ������ DB�κ��� ���� ����� ������ [responseType] JSON�ۼ��Ͽ� Ŭ���̾�Ʈ�� ���� 
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
			System.out.println("Server_changePW_controller�� : client���� ���� ����� => flag : "+flag+"");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}








