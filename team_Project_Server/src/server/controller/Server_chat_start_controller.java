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

public class Server_chat_start_controller {
	AdminManager adminManager;
	Socket client;
	ServerThread serverThread;
	
	String  chat_type, user_pk, friend_pk, friend_name;
	
	//������ Ŭ���̾�Ʈ�� ������ ���
	boolean flag,room_flag, party_flag;
	
	boolean room_check=true;//true��� room�� �����ؾ���!!!
	
	String room_num; 
	
	BufferedWriter buffw;
	
	public Server_chat_start_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager =adminManager;
		this.client =client;
		this.serverThread = serverThread;
	}
	
	// ��  Ŭ���̾�Ʈ���� �Ѱܹ��� [requestType] JSON �Ľ�
		public void chat_parser(JsonElement obj) {
			chat_type = obj.getAsJsonObject().get("chat_type").getAsString();
			user_pk = obj.getAsJsonObject().get("user_pk").getAsString();
			friend_pk = obj.getAsJsonObject().get("friend_pk").getAsString();
			friend_name = obj.getAsJsonObject().get("friend_name").getAsString();	

			create_room_Query();
			find_room_num_Query();
			create_party_user_Query();
			create_party_friend_Query();
			
			if(room_flag == true && party_flag == true) {
				flag = true;
				chat_start_response();
			}else {
				flag = false;
			}
		}
		
//		//SQL���� ���� ������ ������ ä���� �õ��ϰ��� �ϴ� ģ���� ��ȭ���� ������ �̷��� �ִ��� ��ȸ�ϴ� ����
//		public void check_room() {
//			PreparedStatement pstmt = null;
//			ResultSet rs=null;
//			
//			String sql = "select seq_room, chat_table_chat_type_fk from room, party"; 
//			sql += " where seq_room in";
//			sql += " (select room_room_num_fk from party where member_room_member_fk = "+user_pk+") and seq_room in (select room_room_num_fk from party where member_room_member_fk = "+friend_pk+")";
//			System.out.println("Server_chat_controller : room���̺� ���� ���� : "+sql);
//			
//			try {
//				pstmt = adminManager.con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//				rs = pstmt.executeQuery();
//				
//				//rs.last();
//				//int total = rs.getRow();
//				//System.out.println("���� �����ϴ� room���̺��� �� ���� : "+total);
//				
//				if(rs.next()) {
//					//������ ������ ä���� �õ��ϰ��� �ϴ� ģ���� ��ȭ���� ������ �̷��� �־���
//					room_check = false;
//					room_num = rs.getString("room_room_num_fk");
//					System.out.println("Server_chat_start_controller : ������ ������ ģ���� ä�ù��� �ִٸ� room_num : "+room_num);
//				}else { //������ ������ ä���� �õ��ϰ��� �ϴ� ģ���� ��ȭ���� ������ �̷��� ������
//					System.out.println("������ ������ ä���� �õ��ϰ��� �ϴ� ģ���� ��ȭ���� ������ �̷��� ������");
//					room_check = true; //true��� room�� �����ؾ���!!!
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}finally {
//				if(rs!=null) {
//					try {
//						rs.close();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//				if(pstmt!=null) {
//					try {
//						pstmt.close();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
		
		// SQL���� ���� ä�� �õ�
		//���̺� 3�� ����
		//room, party
		public void create_room_Query() {
			PreparedStatement pstmt = null;
			
			String sql ="insert into room(seq_room, chat_table_chat_type_fk, room_title)";
			sql+=" values(seq_room.nextval, "+chat_type+", '"+friend_name+"')"; 
			System.out.println("Server_chat_controller : room���̺� ���� ���� : "+sql);
			
			try {
				pstmt = adminManager.con.prepareStatement(sql); 
				boolean result = pstmt.execute();
				if(result) { //���̸� select�� ����
					System.out.println("Server_chat_controller : room���̺� insert�� ���� ����");
					room_flag = false;
				}else { //�����̸� DML����
					System.out.println("Server_chat_controller : room���̺� insert�� ���� ����");
					room_flag = true;
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
		
		//room_num�� �˱����� SQL
		public void find_room_num_Query() {
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			String sql = "select * from room";
			try {
				pstmt = adminManager.con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = pstmt.executeQuery();
				rs.last();
				room_num = rs.getString("seq_room");
				System.out.println("room���̺�κ��� ������ room_num : "+room_num);
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
		
		public void create_party_user_Query() {
			
			PreparedStatement pstmt = null;
			
			String sql = "insert into party(seq_party, room_room_num_fk, member_room_member_fk)";
			sql+=" values(seq_party.nextval,"+room_num+","+user_pk+")";
			
			System.out.println("Server_chat_controller : party_user �Է� ���� : "+sql);
			
			try {
				pstmt = adminManager.con.prepareStatement(sql); 
				boolean result = pstmt.execute();
				if(result) { //���̸� select�� ����
					System.out.println("Server_chat_controller : party_user �Է� insert�� ���� ����");
					party_flag = false;
				}else { //�����̸� DML����
					System.out.println("Server_chat_controller : party_user �Է� insert�� ���� ����");
					party_flag = true;
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
		
		public void create_party_friend_Query() {
			
			PreparedStatement pstmt = null;
			
			String sql = "insert into party(seq_party, room_room_num_fk, member_room_member_fk)";
			sql+=" values(seq_party.nextval,"+room_num+","+friend_pk+")";
			
			System.out.println("Server_chat_controller : party_friend �Է� ���� : "+sql);
			
			try {
				pstmt = adminManager.con.prepareStatement(sql); 
				boolean result = pstmt.execute();
				if(result) { //���̸� select�� ����
					System.out.println("Server_chat_controller : party_friend �Է� ���� ����");
					party_flag = false;
				}else { //�����̸� DML����
					System.out.println("Server_chat_controller : party_friend �Է� ���� ����");
					party_flag = true;
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
	
		
		// �� ������ DB�κ��� ���� ����� ������ [responseType] JSON�ۼ��Ͽ� Ŭ���̾�Ʈ�� ���� 
		public void chat_start_response() {
			System.out.println("Server_chat_controller : flag "+flag+", room_num"+room_num);
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("\"responseType\": \"chat_start\",");
			sb.append("\"flag\": \""+flag+"\",");
			sb.append("\"user_pk\": \""+user_pk+"\",");
			sb.append("\"chat_friend_pk\": \""+friend_pk+"\",");
			sb.append("\"chat_friend_name\": \""+friend_name+"\",");
			sb.append("\"room_num\": \""+room_num+"\"");
			sb.append("}");
			try {
				buffw = new BufferedWriter(new OutputStreamWriter (client.getOutputStream()));
				buffw.write(sb.toString()+"\n");
				buffw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
