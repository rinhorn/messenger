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
	
	//서버가 클라이언트로 보내줄 결과
	boolean flag,room_flag, party_flag;
	
	boolean room_check=true;//true라면 room을 생성해야함!!!
	
	String room_num; 
	
	BufferedWriter buffw;
	
	public Server_chat_start_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager =adminManager;
		this.client =client;
		this.serverThread = serverThread;
	}
	
	// ★  클라이언트부터 넘겨받은 [requestType] JSON 파싱
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
		
//		//SQL문을 통해 기존에 유저가 채팅을 시도하고자 하는 친구와 대화방을 생성한 이력이 있는지 조회하는 쿼리
//		public void check_room() {
//			PreparedStatement pstmt = null;
//			ResultSet rs=null;
//			
//			String sql = "select seq_room, chat_table_chat_type_fk from room, party"; 
//			sql += " where seq_room in";
//			sql += " (select room_room_num_fk from party where member_room_member_fk = "+user_pk+") and seq_room in (select room_room_num_fk from party where member_room_member_fk = "+friend_pk+")";
//			System.out.println("Server_chat_controller : room테이블 생성 쿼리 : "+sql);
//			
//			try {
//				pstmt = adminManager.con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//				rs = pstmt.executeQuery();
//				
//				//rs.last();
//				//int total = rs.getRow();
//				//System.out.println("현재 존재하는 room테이블의 총 갯수 : "+total);
//				
//				if(rs.next()) {
//					//기존에 유저가 채팅을 시도하고자 하는 친구와 대화방을 생성한 이력이 있었음
//					room_check = false;
//					room_num = rs.getString("room_room_num_fk");
//					System.out.println("Server_chat_start_controller : 기존에 유저가 친구와 채팅방이 있다면 room_num : "+room_num);
//				}else { //기존에 유저가 채팅을 시도하고자 하는 친구와 대화방을 생성한 이력이 없었음
//					System.out.println("기존에 유저가 채팅을 시도하고자 하는 친구와 대화방을 생성한 이력이 없었음");
//					room_check = true; //true라면 room을 생성해야함!!!
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
		
		// SQL문을 통해 채팅 시도
		//테이블 3개 갱신
		//room, party
		public void create_room_Query() {
			PreparedStatement pstmt = null;
			
			String sql ="insert into room(seq_room, chat_table_chat_type_fk, room_title)";
			sql+=" values(seq_room.nextval, "+chat_type+", '"+friend_name+"')"; 
			System.out.println("Server_chat_controller : room테이블 생성 쿼리 : "+sql);
			
			try {
				pstmt = adminManager.con.prepareStatement(sql); 
				boolean result = pstmt.execute();
				if(result) { //참이면 select문 실행
					System.out.println("Server_chat_controller : room테이블 insert문 쿼리 실패");
					room_flag = false;
				}else { //거짓이면 DML실행
					System.out.println("Server_chat_controller : room테이블 insert문 쿼리 성공");
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
		
		//room_num을 알기위한 SQL
		public void find_room_num_Query() {
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			String sql = "select * from room";
			try {
				pstmt = adminManager.con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				rs = pstmt.executeQuery();
				rs.last();
				room_num = rs.getString("seq_room");
				System.out.println("room테이블로부터 가져온 room_num : "+room_num);
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
			
			System.out.println("Server_chat_controller : party_user 입력 쿼리 : "+sql);
			
			try {
				pstmt = adminManager.con.prepareStatement(sql); 
				boolean result = pstmt.execute();
				if(result) { //참이면 select문 실행
					System.out.println("Server_chat_controller : party_user 입력 insert문 쿼리 실패");
					party_flag = false;
				}else { //거짓이면 DML실행
					System.out.println("Server_chat_controller : party_user 입력 insert문 쿼리 성공");
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
			
			System.out.println("Server_chat_controller : party_friend 입력 쿼리 : "+sql);
			
			try {
				pstmt = adminManager.con.prepareStatement(sql); 
				boolean result = pstmt.execute();
				if(result) { //참이면 select문 실행
					System.out.println("Server_chat_controller : party_friend 입력 쿼리 실패");
					party_flag = false;
				}else { //거짓이면 DML실행
					System.out.println("Server_chat_controller : party_friend 입력 쿼리 성공");
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
	
		
		// ★ 서버가 DB로부터 받은 결과를 보내줄 [responseType] JSON작성하여 클라이언트로 전송 
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
