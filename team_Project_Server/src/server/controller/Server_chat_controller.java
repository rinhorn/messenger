package server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.print.attribute.standard.Severity;

import com.google.gson.JsonElement;

import server.ServerThread;
import server_DBConnection.AdminManager;

public class Server_chat_controller {
	
	AdminManager adminManager;
	Socket client;
	ServerThread serverThread;
	BufferedWriter buffw;
	
	//Ŭ���̾�Ʈ�� ������ ����(request)
	String user_pk;
	String friend_pk;
	String room_num;
	String content_type;
	String content;
	
	//������ ������ ����(response)
	boolean flag;
	
	String talker_name; //ȭ�� �̸�
	String send_time;
		
	public Server_chat_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;
	}
	// ��  Ŭ���̾�Ʈ���� �Ѱܹ��� [requestType] JSON �Ľ�
		public void chat_parser(JsonElement obj) {
			user_pk = obj.getAsJsonObject().get("user_pk").getAsString();
			friend_pk = obj.getAsJsonObject().get("friend_pk").getAsString();
			room_num = obj.getAsJsonObject().get("room_num").getAsString();
			content_type = obj.getAsJsonObject().get("content_type").getAsString();
			content = obj.getAsJsonObject().get("content").getAsString();
			//if(flag) {
				get_user_name_Query(); //ģ������ ������ ���� ���̸� ��ȸ����ȣ��
				get_send_time_Query(); //ģ������ ������  ���� �ð� ��ȸ����ȣ��
			//}
				chat_Query();
		}
		
	// SQL���� dialogue ���̺� ����
	public void chat_Query() {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql = "insert into dialogue(seq_dialogue, member_talker_fk, send_time, room_room_num_fk, content_table_content_type_fk, content)"; 
		sql+=" values(seq_dialogue.nextval,"+user_pk+",systimestamp,"+room_num+","+content_type+",'"+content+"')";

		try {
			pstmt = adminManager.con.prepareStatement(sql); 
			boolean result = pstmt.execute();
			if(result) { //���̸� select�� ����
				System.out.println("chat_Query : insert�� ���� ����");
				flag = false;
			}else { //�����̸� DML���त
				System.out.println("chat_Query : insert�� ���� ����");
				flag = true;
				chat_response(); //������ ���� ��
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
	
	public void get_user_name_Query() {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql = "select member_name from member where seq_member = '"+user_pk+"'";
		try {
			pstmt = adminManager.con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				talker_name = rs.getString("member_name");
				System.out.println("�� Server_chat_controller :  ���� user_name �� : "+talker_name);
				send_friend(); //ģ������ ������ ����Ʈ �����
			}else { 
				System.out.println("�� Server_chat_controller :  ���� user_name �������� ����");
			}
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
	
	public void get_send_time_Query() {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql = "select send_time from dialogue";
		try {
			pstmt = adminManager.con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();
			rs.last();
			int total = rs.getRow();
			rs.beforeFirst();
			while(rs.next()){
				int i = rs.getRow();
				if(i==total) {
					send_time = rs.getString("send_time");
					send_time = send_time.substring(11,16);
					System.out.println("Server_chat_controller : ���� ������ dialogue���̺��� send_time�� : "+send_time);
				}
			}
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
	
	// �� ������ DB�κ��� ���� ����� ������ [responseType] JSON�ۼ��Ͽ� Ŭ���̾�Ʈ�� ���� (�ѹ��� ��ȭ�� ������ ����)
	public void chat_response() {
		System.out.println("Server_chat_controller : ���� chat_responseȣ����..");
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"responseType\": \"chat\",");
		sb.append("\"flag\": \""+flag+"\",");
		sb.append("\"user_pk\": \""+user_pk+"\",");
		sb.append("\"room_num\": \""+room_num+"\",");
		sb.append("\"talker_name\": \""+talker_name+"\",");
		sb.append("\"content_type\": \""+content_type+"\",");
		sb.append("\"content\": \""+content+"\",");
		sb.append("\"send_time\": \""+send_time+"\"");
		sb.append("}");
		try {
			buffw = new BufferedWriter(new OutputStreamWriter (client.getOutputStream()));
			buffw.write(sb.toString()+"\n");
			buffw.flush();
			System.out.println("Server_chat_controller�� : client���� ���� ����� => flag : "+flag+", user_pk : "+user_pk+"room_num"+room_num+",send_time : "+send_time+"talker_name : "+talker_name+", content_type : "+content_type+", content : "+content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//����� ����� ģ������ ������ content(�ѹ��̶� ��ȭ�� ����)
	public void send_friend() {
		System.out.println("Server_chat_controller : send_friend() �޼��� ȣ����!!!");
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"responseType\": \"alarm\",");
		sb.append("\"room_num\": \""+room_num+"\",");
		sb.append("\"talker_pk\": \""+user_pk+"\",");
		sb.append("\"talker_name\": \""+talker_name+"\",");
		sb.append("\"content_type\": \""+content_type+"\",");
		sb.append("\"content\": \""+content+"\",");
		sb.append("\"send_time\": \""+send_time+"\"");
		sb.append("}");
		System.out.println("friend_pk (for�� �ۿ�) : " + friend_pk);
		System.out.println("serverThread.messengerServer : "+serverThread.messengerServer);
		System.out.println("serverThread.messengerServer.list.size() : "+serverThread.messengerServer.list.size());
		for(int i=0; i<serverThread.messengerServer.list.size(); i++) {
			String list_friend_pk = serverThread.messengerServer.list.get(i).user_pk;
			if(list_friend_pk.equals(friend_pk)) {
				System.out.println("friend_pk (for�� �ȿ�) : " + friend_pk);
				System.out.println("Server_chat_controller : serverThread.messengerServer.list.size : "+serverThread.messengerServer.list.size()+", friend_pk : "+friend_pk);
				System.out.println("Server_chat_controller : Chatting �� �����մϴ�");
				try {
					buffw = new BufferedWriter(new OutputStreamWriter (serverThread.messengerServer.list.get(i).client.getOutputStream()));
					buffw.write(sb.toString()+"\n");
					buffw.flush();
					System.out.println("Server_chat_controller�� : ģ������ ���� ���̽� �ļ�����~~ =>  : " + sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
