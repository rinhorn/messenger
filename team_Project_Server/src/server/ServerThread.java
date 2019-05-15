
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import server.controller.Server_AddFriend_controller;
import server.controller.Server_FindId_controller;
import server.controller.Server_GetAllMemberForAdd_controller;
import server.controller.Server_changePW_controller;
import server.controller.Server_chat_controller;
import server.controller.Server_chat_start_controller;
import server.controller.Server_checkId_controller;
import server.controller.Server_deleteFriend_controller;
import server.controller.Server_friendsList_controller;
import server.controller.Server_login_controller;
import server.controller.Server_regist_controller;
import server.controller.Server_resetPW_controller;
import server.sendingEmail.SendingEmail;
import server_DBConnection.AdminManager;

public class ServerThread extends Thread {
	AdminManager adminManager;// DB�� �����ϱ� ���� ��ü

	public String user_pk;
	public List<String> friends_pk;

	// ������ ��Ʈ�ѷ� ��ü
	Server_login_controller server_login_controller;
	Server_checkId_controller server_checkId_controller;
	Server_friendsList_controller server_friendsList_controller;
	Server_GetAllMemberForAdd_controller server_getAllMemberForAdd_controller;
	Server_AddFriend_controller server_AddFriend_controller;
	Server_regist_controller server_regist_controller;
	Server_FindId_controller server_FindId_controller;
	public Server_resetPW_controller server_resetPW_controller;
	Server_changePW_controller server_changePW_controller;
	Server_chat_controller server_chat_controller;
	Server_chat_start_controller server_chat_start_controller;

	public SendingEmail sendingEmail;

	String TAG = getClass().getName();
	public MessengerServer messengerServer;
	public Socket client;
	BufferedReader buffr;
	BufferedWriter buffw;
	boolean flag = true;

	// �α��� ���μ��� �Ӹ� �ƴ϶�, ģ�������� ���� �ش� Ŭ������ FriendID�� �������� ���ؼ� ��������� ����
	Server_friendsList_controller get_friends_controller;

	// delete_controller�� �޼��带 �����ϱ� ���� ��ü
	Server_deleteFriend_controller server_deleteFriend_controller;

	String entered_seq_member; // deleteFriend_controller�� �α����� ����� PK_seq�� �Ѱܹޱ� ���� ��������� ����

	public ServerThread(MessengerServer messengerServer, Socket client) {
		this.messengerServer = messengerServer;
		this.client = client;

		adminManager = new AdminManager();

		server_login_controller = new Server_login_controller(adminManager, client, this);
		server_checkId_controller = new Server_checkId_controller(adminManager, client);
		server_friendsList_controller = new Server_friendsList_controller(adminManager, client);
		server_deleteFriend_controller = new Server_deleteFriend_controller(adminManager, client);
		server_getAllMemberForAdd_controller = new Server_GetAllMemberForAdd_controller(adminManager, client, this);
		server_AddFriend_controller = new Server_AddFriend_controller(adminManager, client, this);
		server_regist_controller = new Server_regist_controller(adminManager, client, this);
		server_FindId_controller = new Server_FindId_controller(adminManager, client, this);
		server_resetPW_controller = new Server_resetPW_controller(adminManager, client, this);
		server_changePW_controller = new Server_changePW_controller(adminManager, client, this);
		sendingEmail = new SendingEmail(this);
		server_chat_start_controller = new Server_chat_start_controller(adminManager, client, this);
		server_chat_controller = new Server_chat_controller(adminManager, client, this);

		try {
			buffr = new BufferedReader(new InputStreamReader(client.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// client�� ��ȯ���ִ� getter�޼���
	public Socket getClient() {
		return client;
	}

	public void listen() {
		try {
			System.out.println("�� serverThread listening~~~");
			String msg = buffr.readLine();
			System.out.println("ServerThread : Client �� ���� ���� request_msg : " + msg);

			// msg �� json �϶� parsing �� �����ϴ� �ڵ�
			JsonParser parser = new JsonParser();
			JsonElement obj = parser.parse(msg);
			JsonElement requestTypeElement = obj.getAsJsonObject().get("requestType");
			String requestType = requestTypeElement.getAsString();

			// �� Ŭ���̾�Ʈ�κ��� �� [requestType]�� ���� �ش� ���� ��Ʈ�ѷ��� ȣ��
			// ���⼭�� ������ requestType�����ؼ��� �����Ͽ� �ش� ���� ��Ʈ�ѷ��� ȣ�����ش�.
			// ������ �Ľ��� ���� ��Ʈ�ѷ����� ����.

			if (requestType.equals("login")) { // �α��� ����
				System.out.println("ServerThread : requestType login �� ���� ó���� �����մϴ�.");
				server_login_controller.login_parser(obj);
			} else if (requestType.equals("checkId")) { // ���̵� �ߺ�üũ ����
				server_checkId_controller.checkId_parser(obj);
				System.out.println("ServerThread : requestType checkId �� ���� ó���� �����մϴ�.");
			} else if (requestType.equals("friendsList")) {
				System.out.println("ServerThread : requestType friendsList �� ���� ó���� �����մϴ�.");
				server_friendsList_controller.friendsList_parser(obj);
				friends_pk = server_friendsList_controller.getFriends_pk();
				/////////////////////// 2019-02-13 �̻��� �ۼ�//////////////////////////////
			} else if (requestType.equals("getMembers")) {
				System.out.println("ServerThread : requestType getMembers �� ���� ó���� �����մϴ�.");
				server_getAllMemberForAdd_controller.gettingMemberQuery();
				server_getAllMemberForAdd_controller.getAllMeber_parsing(obj);
				///////////////////////////// ��üȸ���ҷ��������/////////////////////////////
			} else if (requestType.equals("addFriend")) {
				System.out.println("ServerThread : requestType addFriend �� ���� ó���� �����մϴ�.");
				server_AddFriend_controller.addFriend_parser(obj);
				System.out.println("addfriend ���������� ����!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				server_AddFriend_controller.addFriend_query();
				server_AddFriend_controller.addFriend_response();
				///////////////////////////////// ģ�� �߰� ����/////////////////////////
			}

			/*
			 * else if (requestType.equals("getFriends")) { // client �� ���� ���� json ��
			 * requestType �� getFriends �� ��� ���� ������ ���� getFriends_process(obj); }
			 */

			else if (requestType.equals("delete")) { // ģ�� ���� ����
				System.out.println("ServerThread : requestType delete �� ���� ó���� �����մϴ�.");
				server_deleteFriend_controller.delete_parser(obj);
			} else if (requestType.equals("regist")) {
				System.out.println("ServerThread : requestType regist �� ���� ó���� �����մϴ�.");
				server_regist_controller.regist_parser(obj);
			} else if (requestType.equals("findId")) {
				System.out.println("ServerThread : requestType findId �� ���� ó���� �����մϴ�.");
				server_FindId_controller.findId_query(obj);
			} else if (requestType.equals("resetPW")) {
				System.out.println("ServerThread : requestType resetPW �� ���� ó���� �����մϴ�.");
				server_resetPW_controller.resetPW_query(obj);
			} else if (requestType.equals("certify")) {
				System.out.println("ServerThread : requestType certify �� ���� ó���� �����մϴ�.");
				server_resetPW_controller.certify_parser(obj);
			} else if (requestType.equals("changePW")) {
				System.out.println("ServerThread : requestType changePW �� ���� ó���� �����մϴ�.");
				server_changePW_controller.changePW_parser(obj);
			} // ģ���� ��ȭâ ����
			else if (requestType.equals("chat_start")) {
				server_chat_start_controller.chat_parser(obj);
			}
			// ���̾�α׿� ��ȭ���� insurt
			else if (requestType.equals("chat")) {
				server_chat_controller.chat_parser(obj);
			}

			// ������ ������ ��� �������� send()�� ȣ��
			for (int i = 0; i < messengerServer.list.size(); i++) {
				ServerThread st = messengerServer.list.get(i);
				// st.send(msg);
			}
			messengerServer.area.append(msg + "\n"); // ������ �ؽ�Ʈ�ʵ忡 �޽��� �ٿ��ֱ�
			messengerServer.bar.setValue(messengerServer.bar.getMaximum()); // ��ũ�� �ڵ� ������
		} catch (IOException e) {
			System.out.println("TAG : " + TAG);
			// �������� ���� ip �������ֱ�
			messengerServer.list.remove(this);
			messengerServer.area.append("�����ϼ̽��ϴ�.\n");
			flag = false; // while�� ���ѷ��� ���߱�

			e.printStackTrace();
		}
	}

	public void send(String msg) {
		try {
			buffw.write(msg + "\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (flag) {
			listen();
		}
	}

//		// ����� ������ Ȯ���Ͽ��ٸ� �α����� ������� ģ������� �ҷ��´�.
//		get_friends_controller = new GetFriends_controller(login_controller);
//		boolean result_get_friends = get_friends_controller.getFriendsInfomation();
//		System.out.println("�� ģ�� ���� ���� : " + result_get_friends);
//		
//		Gson gson = new Gson();
//		String friends_data_json = gson.toJson(get_friends_controller.getFriends());
//		System.out.println(friends_data_json);
//
//		// ģ���� PK_ID �Ѱ��ֱ� ���ؼ�..
//		String AllFriendID = gson.toJson(get_friends_controller.getAllFriendID());
//		System.out.println("��� ģ���� PK_ID: "+AllFriendID);
//		
//		
//		if (login_check_result) {
//			login_result_json.append("{");
//			login_result_json.append("\"answerType\":\"ok\",");
//			login_result_json.append("\"entered_name\":\"" + entered_name + "\",");
//			login_result_json.append("\"entered_seq_member\":\"" + entered_seq_member + "\",");
//			login_result_json.append("\"friends_data\":"+friends_data_json+",");
//			login_result_json.append("\"AllFriendID\":"+AllFriendID);
//			login_result_json.append("}");
//			result = login_result_json.toString();
//			System.out.println("��� ģ���� pk_ID�� �� �Ѿ����? "+AllFriendID);
//		} else {
//			login_result_json.append("{");
//			login_result_json.append("\"answerType\":\"denied\"");
//			login_result_json.append("}");
//			result = login_result_json.toString();
//		}
//		try {
//			System.out.println("�� ServerThread ���� login requestType �� ���� ����� �����մϴ�.");
//			buffw.write(result + "\n");
//			buffw.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	
//	}

	/*
	 * // json �� requestType ������ getFriends �� ��� �����ϴ� method public void
	 * getFriends_process(JsonElement obj) { GetFriends_controller
	 * get_friends_controller = new GetFriends_controller(login_controller); boolean
	 * result = get_friends_controller.getFriendsInfomation();
	 * System.out.println("�� ģ�� ���� ���� : " + result); StringBuffer
	 * getFriends_process_json = new StringBuffer(); String answerType = null;
	 * 
	 * // GetFriends_controller �� �ִ� Friends list �� �����´� for(int i=0;
	 * i<get_friends_controller.friends.size(); i++) {
	 * System.out.println("�� ServerThread �� GetFriends_controller �� ���� ������ ģ�� : "
	 * +get_friends_controller.friends.get(i)); }
	 * 
	 * Gson gson = new Gson(); String friends_data_json =
	 * gson.toJson(get_friends_controller.getFriends());
	 * System.out.println(friends_data_json);
	 * 
	 * if(result) { answerType = "friends_exist";
	 * getFriends_process_json.append("{");
	 * getFriends_process_json.append("\"answerType\":\""+answerType+"\",");
	 * getFriends_process_json.append("\"friends_data\":\""+friends_data_json+"\"");
	 * getFriends_process_json.append("}"); String getFriends_json =
	 * getFriends_process_json.toString();
	 * System.out.println("�� ServerThread ���� ������ getFriends_json\n"+getFriends_json)
	 * ;
	 * 
	 * try {
	 * System.out.println("�� ServerThread ���� getFriends requestType �� ���� ����� �����մϴ�."
	 * ); buffw.write(getFriends_json+"\n"); buffw.flush(); } catch (IOException e)
	 * { e.printStackTrace(); } } }
	 */
}
