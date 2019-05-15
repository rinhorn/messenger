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

public class Server_login_controller {
	//������ ��Ʈ�ѷ��� �⺻������ ������ ��ü
	AdminManager adminManager;
	ServerThread serverThread;
	Socket client;
	BufferedWriter buffw;
	
	//Ŭ���̾�Ʈ�� ������ ����(request)
	String user_id;
	String user_pw;
	
	//������ ������ ����(response)
	String user_pk;	
	String user_name;	
	String flag;	

	public Server_login_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;
	}
	
	// ��  Ŭ���̾�Ʈ���� �Ѱܹ��� [requestType] JSON �Ľ�
	public void login_parser(JsonElement obj) {
		user_id = obj.getAsJsonObject().get("user_id").getAsString();
		user_pw = obj.getAsJsonObject().get("user_pw").getAsString();
		login_Query();
	}
	
	// SQL���� ���� ���̵�, ��й�ȣ üũ
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
				
				//�α��� ������ �����ϴٸ�
				System.out.println("�� ���̵�,��й�ȣ ��ġ");
				flag = "true";
				//������������ �������� �з��ϱ� ���� �α��� ������ user_pk�� ���������尡 �����ϰԲ�!!
				serverThread.user_pk = user_pk;
				System.out.println("�������� "+user_name + " �� �α��� (���̵� : " + user_id +") ��������");
			}else { //�α��� ������ �Ұ����ϴٸ�
				System.out.println("�� ���̵�� ��й�ȣ ����ġ");
				flag = "false";
			}
			login_response(); //sql�������� ���䳻���� ������ �޼��� ȣ��
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
			System.out.println("Server_login_controller�� : client���� ���� ����� => flag : "+flag+", user_pk : "+user_pk+", user_name : "+user_name);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}








