/*
 * Server �� ģ�� ��� ���� ��Ʈ�ѷ� ���� 		--	2019-02-13
 */
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
import server_DBConnection.AdminManager;

public class Server_AddFriend_controller {
	
	//�����ڿ� ��û���׿� ���� �˻縦 ���� ���� ( PK ���� ���� )
	String user_pk;
	String friend_pk;
	
	//������ ������ ����(response)
	Boolean flag;
	
	AdminManager adminManager;
	Socket client;
	ServerThread serverThread;
	
	BufferedWriter buffw;
	
	// �� ������
	public Server_AddFriend_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;
	}
	
	// �� Client ������ ���� PK�� Json Parsing �� ���� �ʱ�ȭ! 
	public void addFriend_parser(JsonElement obj) {
		friend_pk = obj.getAsJsonObject().get("friend_pk").getAsString();
		user_pk = serverThread.user_pk;
	}
	
	// �� DB�� ���� ����
	public void addFriend_query() {
		PreparedStatement pstmt = null;
		
		String sql = "insert into friends(seq_friends, member_me_fk, member_myfriend_fk)";
		sql+=" values(seq_friends.nextval,"+user_pk+","+friend_pk+")";
		
		
		try {
			pstmt = adminManager.con.prepareStatement(sql); 
			boolean result = pstmt.execute();
			if(result) { //���̸� select�� ����
				System.out.println("addFriend_parser : insert�� ���� ����");
				flag = false;
			}else { //�����̸� DML����
				System.out.println("addFriend_parser : insert�� ���� ����");
				flag = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e);
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}		
	}
	
	// �� ���� ó���� Client ���� ���� JSON
	public void addFriend_response() {
		String response_msg;
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"responseType\":\"acceptAdd\",");
		sb.append("\"flag\":"+flag+"");
		sb.append("}");
		
		response_msg = sb.toString();
		
		System.out.println("�� Server_AddFriend_controller : "+response_msg);
		
		try {
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(response_msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
