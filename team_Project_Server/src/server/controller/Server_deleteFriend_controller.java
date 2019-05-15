/*
 * �ؾ��� ��
 * 1. ���ϰ� �Ѱ��ֱ�
 * 
 * */

package server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;

import com.google.gson.JsonElement;

import server.ServerThread;
import server_DBConnection.AdminManager;

public class Server_deleteFriend_controller {
	
	AdminManager adminManager; // db�� ������ �� �� connection ��ü
	String user_pk; // �α����� ���
	String friend_pk; // �Ѱܹ��� ģ���� ���̵�
	
	// client�� outputStrem�� �̿��� ���� ��� ���θ� �����ϱ� ���� �ʿ��� ��ü
	Socket client;
	
	boolean deleteFriend_flag; // ģ�� ���� ���θ� �˷��ִ� flag
	
	// ģ�� data �� GUI, ����Ʈ, DB���� �����ϴ� method
	// ģ�� ���� controller �� �̵��� method
	public Server_deleteFriend_controller(AdminManager adminManager, Socket client) {
		this.adminManager= adminManager;
		this.client= client;
	}
	
	// Ŭ���̾�Ʈ���� ���� Json �Ľ��ϱ�
	// ���������忡�� requestType�� delete��� ȣ�����ָ鼭 �Ű������� obj �Ѱ��ֱ�
	public void delete_parser(JsonElement obj){
		user_pk = obj.getAsJsonObject().get("user_pk").getAsString();
		friend_pk = obj.getAsJsonObject().get("friend_pk").getAsString();
		deleteFriend_sql();
		System.out.println("Server: Ŭ���̾�Ʈ���� �Ѿ�� user_PK: "+user_pk);
		System.out.println("Server: Ŭ���̾�Ʈ���� �Ѿ�� friend_pk: "+friend_pk);
	}
	
	// �Ľ��� ���� ������ DB���� ģ�� �����ϱ�
	public void deleteFriend_sql() {
		// DB ���� ������ ģ�� data�� ����
		PreparedStatement pstmt = null;
		int result=0;
		String sql =  "delete from friends where member_me_fk="+user_pk+" and member_myFriend_fk="+friend_pk; 
		// friends ���̺��� ���÷���='user_id'�� ���� ģ���÷��� = '������ ģ�� ��'�̶�� ...... 
		System.out.println("DB�� ģ�� ���� ��û �����Դϴ�!");	
		try {
			pstmt= adminManager.con.prepareStatement(sql);
			result=pstmt.executeUpdate();
			System.out.println("ģ�� ���� ��û  SQL���� "+sql);
			if(result!=0) {
				deleteFriend_flag=true;
				System.out.println("DB���� ģ�� ���� ����");
			}else {
				deleteFriend_flag=false;
				System.out.println("DB���� ģ�� ���� ����....�̤�");
			}
			delete_response();
			System.out.println("ģ������ ��� ������ delete_response() ȣ����....");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	// ģ�� ���� ���� ���θ� Ŭ���̾�Ʈ�� ������ Json �ۼ�
	public void delete_response() {
		StringBuffer sb= new StringBuffer();
		sb.append("{");
			sb.append("\"responseType\":\"delete\",");
			sb.append("\"flag\":\""+deleteFriend_flag+"\"");
		sb.append("}");
		
		String msg= sb.toString();
		System.out.println("deleteFriend�� Json��  "+msg);
		try {
			BufferedWriter buffw= new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(msg+"\n");
			buffw.flush();
			System.out.println("server : responseType ���� ����!!!!");
			System.out.println("server : Ŭ���̾�Ʈ�� ���� Json�� "+"responseType: delete, flag: "+deleteFriend_flag+" �Դϴ�.");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("server : responseType ���� ���Ф̤̤̤�");
		}
	}
	
}	
	
	
	
	

