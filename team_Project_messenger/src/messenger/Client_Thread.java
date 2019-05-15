/*Ŭ���̾�Ʈ �������� ���� ����!
 * 
 * Ŭ���̾�Ʈ�� �����κ��� �޽����� �޴� Ÿ�̹��� �޽����� �������Ӹ� �ƴ϶�,
 * ������ �־ �˾Ƽ� �޽����� �޾ƾ� �ϹǷ� (�׻� ��� �־�� �Ѵ�)
 * ���ѷ��� �� �񵿱� ������� ó���ؾ� �Ѵ�. */
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

// controller ���� ó���� ����� ��� �����ϴ� Ŭ����
public class Client_Thread extends Thread {
	String TAG = this.getClass().getName();
	MainFrame mainFrame;
	Register register;
	Chat_Window chat_window;
	Socket client; // ��ȭ�� ����
	BufferedReader buffr = null;
	BufferedWriter buffw = null;
	Main main;
	Address_book address_book;

	// ��Ʈ�ѷ��� ��Ʈ�ѷ� ���� ����
	public Client_login_controller client_login_controller;
	public Client_checkId_controller client_checkId_controller;
	public Client_deleteFriend_controller client_deleteFriend_controller;
	public Client_regist_controller client_regist_controller;

	Object[][] model; // server �� ���� ���� ��ü member ������ �迭 data �� ��� ���� Object �������迭 ����
	Object[] friends; // server �� ���� ���� friends �迭�� ��� ���� Object �迭 ����

	// ������
	public Client_Thread(MainFrame mainFrame, Socket client) {
		this.mainFrame = mainFrame;
		this.client = client;

		// ��Ʈ�� �̾Ƽ� ����� �����ϱ�
		try {
			buffr = new BufferedReader(new InputStreamReader(client.getInputStream()));
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listen() {
		// System.out.println(address_book.loginForm.entered_name+" ������");

		// �������� �� �޼��� �ޱ� (�Է�)

		try {
			String message = buffr.readLine();
			System.out.println("�� Client ���� ���� üũ ����� : " + message);
			System.out.println("�� ClientThread listening~~~");

			JsonParser parser = new JsonParser();

			JsonElement obj = parser.parse(message);
			System.out.println("���� ó������ responseType : " + obj.getAsJsonObject().get("responseType").getAsString());
			JsonElement responseTypeElement = obj.getAsJsonObject().get("responseType");
			String responseType = responseTypeElement.getAsString();

			// �� �����κ��� �� [responseType]�� ���� �ش� Ŭ���̾�Ʈ ��Ʈ�ѷ��� ȣ��
			// ���⼭�� ������ responseType�����ؼ��� �����Ͽ� �ش� Ŭ���̾�Ʈ ��Ʈ�ѷ��� ȣ�����ش�.
			// ������ �Ľ��� Ŭ���̾�Ʈ ��Ʈ�ѷ����� ����.

			// �α��� ����
			if (responseType.equals("login")) {
				mainFrame.client_login_controller.login_parser(obj);
				System.out.println("clientThread : ���� Ŭ���̾�Ʈ ��Ʈ�ѷ��� ȣ����...");
				mainFrame.getContentPane().setPreferredSize(new Dimension(490, 630));
			}
			// ���̵� �ߺ� Ȯ�� ����
			if (responseType.equals("checkId")) {
				System.out.println("��Ʈ�ѷ��� �Ѱ��� obj�� : " + obj);
				mainFrame.client_checkId_controller.checkId_parser(obj);
				System.out.println("clientThread : ���� Ŭ���̾�Ʈ ��Ʈ�ѷ��� ȣ����...");
			}
			// ģ�� ��� ���
			if (responseType.equals("friendsList")) {
				System.out.println("��Ʈ�ѷ��� �Ѱ��� obj�� : " + obj);
				mainFrame.client_friendsList_controller.friendsList_parser(obj);
				System.out.println("clientThread : ���� Ŭ���̾�Ʈ ��Ʈ�ѷ��� ȣ����...");
				mainFrame.main.address_book.showAllFriends();
			}

			// ģ�� �߰� ��ư ������ ��ü ȸ�� ��� ������ֱ�
			if (responseType.equals("memberList")) {
				System.out.println("��Ʈ�ѷ��� �Ѱ��� obj�� : " + obj);
				mainFrame.client_memberList_controller.memberList_parser(obj);
				System.out.println("clientThread : Call that controller������������");
				// �Ľ��� ���̺� ���� �޼��� ȣ��
				mainFrame.search_Add_Friends.uploadTable();
			}

			// ģ�� ��� ���� -- 2019-02-13
			if (responseType.equals("acceptAdd")) {
				System.out.println("��Ʈ�ѷ��� �Ѱ��� obj�� : " + obj);
				mainFrame.client_addFriend_controller.addFriend_parser(obj);
				System.out.println("clientThread : Call that controller������������");
			}

			// ģ�� ������ ���� ����� �ޱ�
			if (responseType.equals("delete")) {
				System.out.println("��Ʈ�ѷ��� �Ѱ��� obj�� : " + obj);
				mainFrame.client_deleteFriend_controller.delete_response(obj);
			}
			// ȸ�� ��� ��� �ޱ�
			if (responseType.equals("regist")) {
				System.out.println("Client_Thread : regist ��� obj : " + obj);
				mainFrame.client_regist_controller.regist_parser(obj);
			}
			// ���̵� ã�� ��� �ޱ�
			if (responseType.equals("findId")) {
				System.out.println("Client_Thread : findId ��� obj : " + obj);
				mainFrame.client_findAccount_controller.findID_parser(obj);
			}
			// ��й�ȣ ����� ������ȣ �ޱ��� ȸ������ ����
			if (responseType.equals("resetPW")) {
				System.out.println("Client_Thread : resetPW ��� obj : " + obj);
				mainFrame.client_resetPW_controller.resetPW_parser(obj);
			}
			// ��й�ȣ ����� ������ȣ ����
			if (responseType.equals("certify")) {
				System.out.println("Client_Thread : resetPW ��� obj : " + obj);
				mainFrame.client_resetPW_controller.certify_parser(obj);
			}
			// ��й�ȣ ����!!
			if (responseType.equals("changePW")) {
				System.out.println("Client_Thread : changePW ��� obj : " + obj);
				mainFrame.client_changePW_controller.changePW_parser(obj);
			} // ä��â ����
			if (responseType.equals("chat_start")) {
				System.out.println("��Ʈ�ѷ��� �Ѱ��� obj�� : " + obj);
				mainFrame.client_chat_start_controller.chat_start_parser(obj);
			}
			// ä�� �����ϱ�
			if (responseType.equals("chat")) {
				System.out.println("��Ʈ�ѷ��� �Ѱ��� obj�� : " + obj);
				mainFrame.client_chat_controller.chat_parser(obj);
			}
			if (responseType.equals("alarm")) {
				System.out.println("Client_Thread : alarm ��Ʈ�ѷ��� �Ѱ��� obj�� : " + obj);
				mainFrame.client_alarm_controller.alarm_parser(obj);
			}

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (HeadlessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println("����Ŭ���ؼ� â ������? "+select_opend_window);

		/*
		 * // buffr.readLine method �� ���� ���� �޼����� ������ �����ϴ� �޼��� // �޽��� �ڽ��� �����ؼ� �ؽ�Ʈ ������
		 * ������ ä��â�� ����ϴ�(���̴�) ����
		 * 
		 * if(select_opend_window == false) { if(window_exist_flag == false) {
		 * chat_window = new Chat_Window(address_book, PersonBox.selectedBox);
		 * chat_window.setTitle(" �� ���� ä��"); MessageBox message_box = new MessageBox();
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

	// ���������� JSON �޼��� ������

	public void run() {
		System.out.println("==== Client_thread() start ====");
		while (true) {
			listen();
		}
	}

//	// �α��� ������ ���ۿ� ���� method
//	public void login_ok_process(JsonElement obj) {
//		// �Է��� ���̵�� �н����尡 �����ͺ��̽��� �����ϴ� ȸ���� �����Ϳ� ��ġ�Ѵٸ�
//		System.out.println("===== Login_OK answer recived=====");
//		entered_name = obj.getAsJsonObject().get("entered_name").getAsString();
//		entered_seq_member = obj.getAsJsonObject().get("entered_seq_member").getAsString();
//		
//		// �α����� ������� ������ ��ϵ� ģ������ ����� �ҷ��´�.
//		System.out.println("==== get_friends recived ====");
//		JsonElement friends_dataElement = obj.getAsJsonObject().get("friends_data");
//		JsonElement friend_ID_Element = obj.getAsJsonObject().get("AllFriendID");
//		JsonArray friends_data = friends_dataElement.getAsJsonArray();
//		JsonArray friend_ID_data = friend_ID_Element.getAsJsonArray();
//		Gson gson = new Gson();
//		friends = gson.fromJson(friends_data, Object[].class);
//		allFriendID = gson.fromJson(friend_ID_data, Object[].class);
//		System.out.println("�� ClientThread ���� ���� friends_data ���̽��� : " + friends_data);
//		System.out.println(allFriendID +"friends_data Object ��ȯ");
//
//		main = new Main(client,this, friends, allFriendID);
//		main.setTitle(entered_name);
//		loginForm.dispose();
//	}

	// ģ���߰� ��ư Ŭ���� ��� ����� ����ϱ� ���� �޼���
	/*
	 * public void get_all_member(JsonElement obj) {
	 * System.out.println("===== Get ALL Member answer recived====="); JsonElement
	 * dataElement = obj.getAsJsonObject().get("tableModel"); JsonArray data =
	 * dataElement.getAsJsonArray(); Gson gson = new Gson(); model =
	 * gson.fromJson(data, Object[][].class);
	 * 
	 * System.out.println("�� ClientThread �� ���� get_all_member data ���̽��� : " + data);
	 * System.out.println(model +"get_all_member data Object ��ȯ");
	 * System.out.println(model[0][0]); System.out.println(model[0][1]);
	 * System.out.println(model[1][0]);
	 * 
	 * }
	 */
}
