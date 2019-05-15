
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
	AdminManager adminManager;// DB와 연결하기 위한 객체

	public String user_pk;
	public List<String> friends_pk;

	// 서버측 컨트롤러 객체
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

	// 로그인 프로세스 뿐만 아니라, 친구삭제를 위해 해당 클래스의 FriendID를 가져오기 위해서 멤버변수로 선언
	Server_friendsList_controller get_friends_controller;

	// delete_controller의 메서드를 수행하기 위한 객체
	Server_deleteFriend_controller server_deleteFriend_controller;

	String entered_seq_member; // deleteFriend_controller도 로그인한 사람의 PK_seq를 넘겨받기 위해 멤버변수로 선언

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

	// client를 반환해주는 getter메서드
	public Socket getClient() {
		return client;
	}

	public void listen() {
		try {
			System.out.println("▶ serverThread listening~~~");
			String msg = buffr.readLine();
			System.out.println("ServerThread : Client 로 부터 받은 request_msg : " + msg);

			// msg 가 json 일때 parsing 을 진행하는 코드
			JsonParser parser = new JsonParser();
			JsonElement obj = parser.parse(msg);
			JsonElement requestTypeElement = obj.getAsJsonObject().get("requestType");
			String requestType = requestTypeElement.getAsString();

			// ★ 클라이언트로부터 온 [requestType]에 따라 해당 서버 컨트롤러를 호출
			// 여기서는 보내온 requestType관련해서만 구분하여 해당 서버 컨트롤러를 호출해준다.
			// 나머지 파싱은 서버 컨트롤러에서 진행.

			if (requestType.equals("login")) { // 로그인 관련
				System.out.println("ServerThread : requestType login 에 대한 처리를 진행합니다.");
				server_login_controller.login_parser(obj);
			} else if (requestType.equals("checkId")) { // 아이디 중복체크 관련
				server_checkId_controller.checkId_parser(obj);
				System.out.println("ServerThread : requestType checkId 에 대한 처리를 진행합니다.");
			} else if (requestType.equals("friendsList")) {
				System.out.println("ServerThread : requestType friendsList 에 대한 처리를 진행합니다.");
				server_friendsList_controller.friendsList_parser(obj);
				friends_pk = server_friendsList_controller.getFriends_pk();
				/////////////////////// 2019-02-13 이상훈 작성//////////////////////////////
			} else if (requestType.equals("getMembers")) {
				System.out.println("ServerThread : requestType getMembers 에 대한 처리를 진행합니다.");
				server_getAllMemberForAdd_controller.gettingMemberQuery();
				server_getAllMemberForAdd_controller.getAllMeber_parsing(obj);
				///////////////////////////// 전체회원불러오기관련/////////////////////////////
			} else if (requestType.equals("addFriend")) {
				System.out.println("ServerThread : requestType addFriend 에 대한 처리를 진행합니다.");
				server_AddFriend_controller.addFriend_parser(obj);
				System.out.println("addfriend 쿼리날리기 직전!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				server_AddFriend_controller.addFriend_query();
				server_AddFriend_controller.addFriend_response();
				///////////////////////////////// 친구 추가 관련/////////////////////////
			}

			/*
			 * else if (requestType.equals("getFriends")) { // client 로 부터 받은 json 의
			 * requestType 이 getFriends 일 경우 다음 내용을 수행 getFriends_process(obj); }
			 */

			else if (requestType.equals("delete")) { // 친구 삭제 관련
				System.out.println("ServerThread : requestType delete 에 대한 처리를 진행합니다.");
				server_deleteFriend_controller.delete_parser(obj);
			} else if (requestType.equals("regist")) {
				System.out.println("ServerThread : requestType regist 에 대한 처리를 진행합니다.");
				server_regist_controller.regist_parser(obj);
			} else if (requestType.equals("findId")) {
				System.out.println("ServerThread : requestType findId 에 대한 처리를 진행합니다.");
				server_FindId_controller.findId_query(obj);
			} else if (requestType.equals("resetPW")) {
				System.out.println("ServerThread : requestType resetPW 에 대한 처리를 진행합니다.");
				server_resetPW_controller.resetPW_query(obj);
			} else if (requestType.equals("certify")) {
				System.out.println("ServerThread : requestType certify 에 대한 처리를 진행합니다.");
				server_resetPW_controller.certify_parser(obj);
			} else if (requestType.equals("changePW")) {
				System.out.println("ServerThread : requestType changePW 에 대한 처리를 진행합니다.");
				server_changePW_controller.changePW_parser(obj);
			} // 친구와 대화창 띄우기
			else if (requestType.equals("chat_start")) {
				server_chat_start_controller.chat_parser(obj);
			}
			// 다이얼로그에 대화내용 insurt
			else if (requestType.equals("chat")) {
				server_chat_controller.chat_parser(obj);
			}

			// 서버에 접속한 모든 쓰레드의 send()를 호출
			for (int i = 0; i < messengerServer.list.size(); i++) {
				ServerThread st = messengerServer.list.get(i);
				// st.send(msg);
			}
			messengerServer.area.append(msg + "\n"); // 서버의 텍스트필드에 메시지 붙여주기
			messengerServer.bar.setValue(messengerServer.bar.getMaximum()); // 스크롤 자동 움직임
		} catch (IOException e) {
			System.out.println("TAG : " + TAG);
			// 서버에서 나간 ip 제거해주기
			messengerServer.list.remove(this);
			messengerServer.area.append("퇴장하셨습니다.\n");
			flag = false; // while문 무한루프 멈추기

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

//		// 사용자 정보를 확인하였다면 로그인한 사용자의 친구목록을 불러온다.
//		get_friends_controller = new GetFriends_controller(login_controller);
//		boolean result_get_friends = get_friends_controller.getFriendsInfomation();
//		System.out.println("▣ 친구 존재 여부 : " + result_get_friends);
//		
//		Gson gson = new Gson();
//		String friends_data_json = gson.toJson(get_friends_controller.getFriends());
//		System.out.println(friends_data_json);
//
//		// 친구의 PK_ID 넘겨주기 위해서..
//		String AllFriendID = gson.toJson(get_friends_controller.getAllFriendID());
//		System.out.println("모든 친구의 PK_ID: "+AllFriendID);
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
//			System.out.println("모든 친구의 pk_ID가 잘 넘어오나? "+AllFriendID);
//		} else {
//			login_result_json.append("{");
//			login_result_json.append("\"answerType\":\"denied\"");
//			login_result_json.append("}");
//			result = login_result_json.toString();
//		}
//		try {
//			System.out.println("▶ ServerThread 에서 login requestType 에 대한 결과를 전송합니다.");
//			buffw.write(result + "\n");
//			buffw.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	
//	}

	/*
	 * // json 의 requestType 문구가 getFriends 일 경우 수행하는 method public void
	 * getFriends_process(JsonElement obj) { GetFriends_controller
	 * get_friends_controller = new GetFriends_controller(login_controller); boolean
	 * result = get_friends_controller.getFriendsInfomation();
	 * System.out.println("▣ 친구 존재 여부 : " + result); StringBuffer
	 * getFriends_process_json = new StringBuffer(); String answerType = null;
	 * 
	 * // GetFriends_controller 에 있는 Friends list 를 가져온다 for(int i=0;
	 * i<get_friends_controller.friends.size(); i++) {
	 * System.out.println("▶ ServerThread 가 GetFriends_controller 로 부터 가져온 친구 : "
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
	 * System.out.println("▶ ServerThread 에서 보내는 getFriends_json\n"+getFriends_json)
	 * ;
	 * 
	 * try {
	 * System.out.println("▶ ServerThread 에서 getFriends requestType 에 대한 결과를 전송합니다."
	 * ); buffw.write(getFriends_json+"\n"); buffw.flush(); } catch (IOException e)
	 * { e.printStackTrace(); } } }
	 */
}
