/*클라이언트 쓰레드의 정의 목적!
 * 
 * 클라이언트가 서버로부터 메시지를 받는 타이밍은 메시지를 보낼때뿐만 아니라,
 * 가만히 있어도 알아서 메시지를 받아야 하므로 (항상 듣고 있어야 한다)
 * 무한루프 및 비동기 방식으로 처리해야 한다. */
package messenger;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import messenger.addressbook.Address_book;
import messenger.chat.Chat_Window;
import messenger.controller.Client_checkId_controller;
import messenger.controller.Client_deleteFriend_controller;
import messenger.controller.Client_login_controller;
import messenger.controller.Client_regist_controller;
import messenger.loginForm.Register;
import messenger.main.Main;

// controller 에서 처리된 결과를 듣고 반응하는 클래스
public class Client_Thread extends Thread {
	String TAG = this.getClass().getName();
	MainFrame mainFrame;
	Register register;
	Chat_Window chat_window;
	Socket client; // 대화용 소켓
	BufferedReader buffr = null;
	BufferedWriter buffw = null;
	Main main;
	Address_book address_book;

	// 컨트롤러측 컨트롤러 관련 변수
	public Client_login_controller client_login_controller;
	public Client_checkId_controller client_checkId_controller;
	public Client_deleteFriend_controller client_deleteFriend_controller;
	public Client_regist_controller client_regist_controller;

	Object[][] model; // server 로 부터 받은 전체 member 이차원 배열 data 를 담아 놓을 Object 이차원배열 변수
	Object[] friends; // server 로 부터 받은 friends 배열을 담아 놓을 Object 배열 변수

	// 생성자
	public Client_Thread(MainFrame mainFrame, Socket client) {
		this.mainFrame = mainFrame;
		this.client = client;

		// 스트림 뽑아서 입출력 연결하기
		try {
			buffr = new BufferedReader(new InputStreamReader(client.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listen() {
		// System.out.println(address_book.loginForm.entered_name+" 리슨중");

		// 서버에서 온 메세지 받기 (입력)

		try {
			String message = buffr.readLine();
			System.out.println("▣ Client 에서 받은 체크 결과값 : " + message);
			System.out.println("▶ ClientThread listening~~~");

			JsonParser parser = new JsonParser();

			JsonElement obj = parser.parse(message);
			System.out.println("현재 처리중인 responseType : " + obj.getAsJsonObject().get("responseType").getAsString());
			JsonElement responseTypeElement = obj.getAsJsonObject().get("responseType");
			String responseType = responseTypeElement.getAsString();

			// ★ 서버로부터 온 [responseType]에 따라 해당 클라이언트 컨트롤러를 호출
			// 여기서는 보내온 responseType관련해서만 구분하여 해당 클라이언트 컨트롤러를 호출해준다.
			// 나머지 파싱은 클라이언트 컨트롤러에서 진행.

			// 로그인 관련
			if (responseType.equals("login")) {
				mainFrame.client_login_controller.login_parser(obj);
				System.out.println("clientThread : 나는 클라이언트 컨트롤러를 호출중...");
				mainFrame.getContentPane().setPreferredSize(new Dimension(490, 630));
			}
			// 아이디 중복 확인 관련
			if (responseType.equals("checkId")) {
				System.out.println("컨트롤러로 넘겨줄 obj값 : " + obj);
				mainFrame.client_checkId_controller.checkId_parser(obj);
				System.out.println("clientThread : 나는 클라이언트 컨트롤러를 호출중...");
			}
			// 친구 목록 출력
			if (responseType.equals("friendsList")) {
				System.out.println("컨트롤러로 넘겨줄 obj값 : " + obj);
				mainFrame.client_friendsList_controller.friendsList_parser(obj);
				System.out.println("clientThread : 나는 클라이언트 컨트롤러를 호출중...");
				mainFrame.main.address_book.showAllFriends();
			}

			// 친구 추가 버튼 누르고 전체 회원 목록 출력해주기
			if (responseType.equals("memberList")) {
				System.out.println("컨트롤러로 넘겨줄 obj값 : " + obj);
				mainFrame.client_memberList_controller.memberList_parser(obj);
				System.out.println("clientThread : Call that controller······");
				// 파싱후 테이블 갱신 메서드 호출
				mainFrame.search_Add_Friends.uploadTable();
			}

			// 친구 등록 관련 -- 2019-02-13
			if (responseType.equals("acceptAdd")) {
				System.out.println("컨트롤러로 넘겨줄 obj값 : " + obj);
				mainFrame.client_addFriend_controller.addFriend_parser(obj);
				System.out.println("clientThread : Call that controller······");
			}

			// 친구 삭제시 삭제 결과를 받기
			if (responseType.equals("delete")) {
				System.out.println("컨트롤러로 넘겨줄 obj값 : " + obj);
				mainFrame.client_deleteFriend_controller.delete_response(obj);
			}
			// 회원 등록 결과 받기
			if (responseType.equals("regist")) {
				System.out.println("Client_Thread : regist 결과 obj : " + obj);
				mainFrame.client_regist_controller.regist_parser(obj);
			}
			// 아이디 찾기 결과 받기
			if (responseType.equals("findId")) {
				System.out.println("Client_Thread : findId 결과 obj : " + obj);
				mainFrame.client_findAccount_controller.findID_parser(obj);
			}
			// 비밀번호 변경시 인증번호 받기전 회원정보 검증
			if (responseType.equals("resetPW")) {
				System.out.println("Client_Thread : resetPW 결과 obj : " + obj);
				mainFrame.client_resetPW_controller.resetPW_parser(obj);
			}
			// 비밀번호 변경시 인증번호 검증
			if (responseType.equals("certify")) {
				System.out.println("Client_Thread : resetPW 결과 obj : " + obj);
				mainFrame.client_resetPW_controller.certify_parser(obj);
			}
			// 비밀번호 변경!!
			if (responseType.equals("changePW")) {
				System.out.println("Client_Thread : changePW 결과 obj : " + obj);
				mainFrame.client_changePW_controller.changePW_parser(obj);
			} // 채팅창 띄우기
			if (responseType.equals("chat_start")) {
				System.out.println("컨트롤러로 넘겨줄 obj값 : " + obj);
				mainFrame.client_chat_start_controller.chat_start_parser(obj);
			}
			// 채팅 진행하기
			if (responseType.equals("chat")) {
				System.out.println("컨트롤러로 넘겨줄 obj값 : " + obj);
				mainFrame.client_chat_controller.chat_parser(obj);
			}
			if (responseType.equals("alarm")) {
				System.out.println("Client_Thread : alarm 컨트롤러로 넘겨줄 obj값 : " + obj);
				mainFrame.client_alarm_controller.alarm_parser(obj);
			}

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("더블클릭해서 창 열었니? "+select_opend_window);

		/*
		 * // buffr.readLine method 를 통해 들은 메세지가 있을때 동작하는 메서드 // 메시지 박스를 생성해서 텍스트 내용을
		 * 입히고 채팅창에 출력하는(붙이는) 동작
		 * 
		 * if(select_opend_window == false) { if(window_exist_flag == false) {
		 * chat_window = new Chat_Window(address_book, PersonBox.selectedBox);
		 * chat_window.setTitle(" 님 과의 채팅"); MessageBox message_box = new MessageBox();
		 * message_box.setText(message+"\n");
		 * chat_window.tp_chat_box.insertComponent(message_box);
		 * chat_window.tp_chat_box.updateUI(); window_exist_flag = true;
		 * 
		 * chat_window.scroll.getVerticalScrollBar().setValue(chat_window.scroll.
		 * getVerticalScrollBar().getMaximum()); }else { MessageBox message_box = new
		 * MessageBox(); message_box.setText(message+"\n");
		 * chat_window.tp_chat_box.insertComponent(message_box);
		 * chat_window.tp_chat_box.updateUI();
		 * chat_window.scroll.getVerticalScrollBar().setValue(chat_window.scroll.
		 * getVerticalScrollBar().getMaximum()); } }else if(select_opend_window) {
		 * MessageBox message_box = new MessageBox(); message_box.setText(message+"\n");
		 * PersonBox.chat_window.tp_chat_box.insertComponent(message_box);
		 * PersonBox.chat_window.tp_chat_box.updateUI();
		 * //PersonBox.chat_window.scroll.getVerticalScrollBar().setValue(chat_window.
		 * scroll.getVerticalScrollBar().getMaximum()); }
		 */
	}

	// 서버측으로 JSON 메세지 보내기

	public void run() {
		System.out.println("==== Client_thread() start ====");
		while (true) {
			listen();
		}
	}

//	// 로그인 성공시 동작에 대한 method
//	public void login_ok_process(JsonElement obj) {
//		// 입력한 아이디와 패스워드가 데이터베이스에 존재하는 회원의 데이터와 일치한다면
//		System.out.println("===== Login_OK answer recived=====");
//		entered_name = obj.getAsJsonObject().get("entered_name").getAsString();
//		entered_seq_member = obj.getAsJsonObject().get("entered_seq_member").getAsString();
//		
//		// 로그인한 사용자의 계정에 등록된 친구들의 목록을 불러온다.
//		System.out.println("==== get_friends recived ====");
//		JsonElement friends_dataElement = obj.getAsJsonObject().get("friends_data");
//		JsonElement friend_ID_Element = obj.getAsJsonObject().get("AllFriendID");
//		JsonArray friends_data = friends_dataElement.getAsJsonArray();
//		JsonArray friend_ID_data = friend_ID_Element.getAsJsonArray();
//		Gson gson = new Gson();
//		friends = gson.fromJson(friends_data, Object[].class);
//		allFriendID = gson.fromJson(friend_ID_data, Object[].class);
//		System.out.println("▶ ClientThread 에서 받은 friends_data 제이슨값 : " + friends_data);
//		System.out.println(allFriendID +"friends_data Object 변환");
//
//		main = new Main(client,this, friends, allFriendID);
//		main.setTitle(entered_name);
//		loginForm.dispose();
//	}

	// 친구추가 버튼 클릭시 모든 멤버를 출력하기 위한 메서드
	/*
	 * public void get_all_member(JsonElement obj) {
	 * System.out.println("===== Get ALL Member answer recived====="); JsonElement
	 * dataElement = obj.getAsJsonObject().get("tableModel"); JsonArray data =
	 * dataElement.getAsJsonArray(); Gson gson = new Gson(); model =
	 * gson.fromJson(data, Object[][].class);
	 * 
	 * System.out.println("▶ ClientThread 가 받은 get_all_member data 제이슨값 : " + data);
	 * System.out.println(model +"get_all_member data Object 변환");
	 * System.out.println(model[0][0]); System.out.println(model[0][1]);
	 * System.out.println(model[1][0]);
	 * 
	 * }
	 */
}
