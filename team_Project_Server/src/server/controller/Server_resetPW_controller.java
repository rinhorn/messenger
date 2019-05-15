package server.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.JOptionPane;

import com.google.gson.JsonElement;

import server.ServerThread;
import server.sendingEmail.SendingEmail;
import server_DBConnection.AdminManager;

//����ڰ� �Է������� �Ѱ��ָ� �ش� ������ DB�� ������ �ִ���
//sql���� ���� Ȯ�����ִ� ��Ʈ�ѷ�

public class Server_resetPW_controller implements Runnable {

	// ���� ���� ���� ����
	String user_id;
	String user_name;
	String user_email;
	String user_birth;

	// ������ȣ ����
	Random random;
	int certify_key;
	int keyFromUser;

	// ������ȣ ��ġ ����
	boolean key_flag;

	Socket client;
	AdminManager adminManager;
	ServerThread serverThread;

	// ȸ������ ��ġ ���� ����� ���� ����
	boolean flag;
	
	// Timer Thread ��� ���� ����
	boolean thread_flag = true;

	public Server_resetPW_controller(AdminManager adminManager, Socket client, ServerThread serverThread) {
		this.adminManager = adminManager;
		this.client = client;
		this.serverThread = serverThread;

		random = new Random();
	}

	public void resetPW_query(JsonElement obj) {

		user_id = obj.getAsJsonObject().get("user_id").getAsString();
		user_birth = obj.getAsJsonObject().get("user_birth").getAsString();

		// �����κ��� �޾ƿ� �̸�, ����, ������ ȸ��DB�� ��ġ�ϴٸ� ������ ��������
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = "select * from member where member_id='" + user_id + "' and birth='" + user_birth + "'";
		System.out.println(sql);

		try {
			pstmt = adminManager.con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs = pstmt.executeQuery();

			rs.beforeFirst();

			while (rs.next()) {
				if (user_id.equals(rs.getString("member_id"))) {
					if (user_birth.equals(rs.getString("birth"))) {
						flag = true;
						user_email = rs.getString("email");
						user_name = rs.getString("member_name");
						new Thread(serverThread.server_resetPW_controller).start();
					}
				} else {
					flag = false;
					user_email = null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		resetPW_response();
		

		if (flag) {
			certify_key = random.nextInt(999999);

			System.out.println("Server_resetPW_controller :	������ȣ : "+certify_key);

			serverThread.sendingEmail.sendMailForCertify(user_name, user_email, certify_key);
			System.out.println("Server_resetPW_controller : ���� �߼� ����");
			
			
		} else {
			System.out.println("Server_resetPW_controller : ���� �߼� ����");
		}
	}

	// ������� true��� Ŭ���̾�Ʈ ������� ����� �����ϱ�����
	public void resetPW_response() {

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"responseType\": \"resetPW\",");
		sb.append("\"flag\": \"" + flag + "\"");
		sb.append("}");

		String response_msg = sb.toString();

		try {
			BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(response_msg + "\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void certify_parser(JsonElement obj) {
		keyFromUser = obj.getAsJsonObject().get("key").getAsInt();
		
		if (keyFromUser == certify_key) {
			key_flag = true;
			thread_flag = false;
			System.out.println("Server_resetPW_controller : ���� ������ȣ ���� ���� : " + certify_key);
			certify_response();
		} else {
			key_flag = false;
			System.out.println("Server_resetPW_controller : ���� ������ȣ ���� ���� : " + certify_key);
			certify_response();
		}
	}

	public void certify_response() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"responseType\": \"certify\",");
		sb.append("\"flag\": \"" + key_flag + "\"");
		sb.append("}");

		String response_msg = sb.toString();

		try {
			BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(response_msg + "\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Ŭ���̾�Ʈ���� ������ȣ�� ��û�ϰԵǸ� ������������ Ÿ�̸� ���� �� ������ȣ �ʱ�ȭ
	@Override
	public void run() {
		// Ÿ�̸� ����
		int timer = 180;
		int min = 0;
		int sec = 0;

		while (thread_flag) {
			try {
				timer--;
				min = timer / 60;
				sec = timer - min * 60;

				Thread.sleep(1000);
				System.out.println("Server_resetPW_controller : run() : " + min + ":" + sec);
				
				if (min == 0 && sec == 0) {
					System.out.println("Server_resetPW_controller : run() : �ð� �ʰ�!");
					certify_key = 0;
					key_flag = false;
					thread_flag = false;
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
