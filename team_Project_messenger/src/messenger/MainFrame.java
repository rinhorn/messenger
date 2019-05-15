package messenger;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import messenger.addressbook.Address_book;
import messenger.addressbook.DeleteFriends;
import messenger.addressbook.Search_Add_Friends;
import messenger.controller.Client_addFriend_controller;
import messenger.controller.Client_alarm_controller;
import messenger.controller.Client_changePW_controller;
import messenger.controller.Client_chat_controller;
import messenger.controller.Client_chat_start_controller;
import messenger.controller.Client_checkId_controller;
import messenger.controller.Client_deleteFriend_controller;
import messenger.controller.Client_findAccount_controller;
import messenger.controller.Client_friendsList_controller;
import messenger.controller.Client_login_controller;
import messenger.controller.Client_memberList_controller;
import messenger.controller.Client_regist_controller;
import messenger.controller.Client_resetPW_controller;
import messenger.loginForm.CheckThread;
import messenger.loginForm.FindId;
import messenger.loginForm.LoginForm;
import messenger.loginForm.Register;
import messenger.loginForm.ResetPw;
import messenger.main.Main;

// ���α׷��� ���������� ���� �ʴ� ���� ������(��� �г��� �θ���)

public class MainFrame extends JFrame {
	InetAddress inetAddess;
	String ip; // ���� ���� ip�ּҷ� ������ų ����
	int port = 7777;
	Socket client;
	public Client_Thread client_Thread;

	// ����) �ڽİ�ü�� Ŭ���̾�Ʈ �� Ŭ������ �߰��ϴ� ��쿡 ���������� ������ public����!
	// �ڽİ�ü new ���Ŀ� �гο� �ٰ� �ϱ� ���� add�� ���ּ���!
	// �ڽİ�ü ����
	// messenger.loginFrom
	public LoginForm loginForm;
	public FindId findId;
	public ResetPw resetPw;
	public Register register;
	public CheckThread checkThread;

	List<JPanel> login_pages = new ArrayList<JPanel>();

	// messenger.main
	public Main main;

	// messenger.addressbook
	public Address_book address_book;
	public DeleteFriends deleteFriends;
	public Search_Add_Friends search_Add_Friends;

	// Ŭ���̾�Ʈ �� ��Ʈ�ѷ� ����
	public Client_login_controller client_login_controller;
	public Client_checkId_controller client_checkId_controller;
	public Client_regist_controller client_regist_controller;
	public Client_findAccount_controller client_findAccount_controller;
	public Client_resetPW_controller client_resetPW_controller;
	public Client_friendsList_controller client_friendsList_controller;
	public Client_deleteFriend_controller client_deleteFriend_controller;
	public Client_addFriend_controller client_addFriend_controller;
	public Client_memberList_controller client_memberList_controller;
	public Client_chat_controller client_chat_controller;
	public Client_changePW_controller client_changePW_controller;
	public Client_chat_start_controller client_chat_start_controller;
	public Client_alarm_controller client_alarm_controller;

	// ���� ������� ������ user_pk
	private String user_pk = null;

	// ���� ������� ������ ģ��_pk, ģ��_�̸� -> ģ���� ���� ���� ����� �ʱ�ȭ!!
	private Object[] friends_pk = new Object[0];
	private Object[] friends_name = new Object[0];

	List friends_pk_array = Arrays.asList(friends_pk);
	List friends_name_array = Arrays.asList(friends_name);

	// ������
	public MainFrame() {
		this.setLayout(new FlowLayout());
		// this.setLayout(new BorderLayout());
		getIp(); // ���� �������� ip�� ������
		connect(); // ���� ����
		createController(); // Ŭ���̾�Ʈ �� ��Ʈ�ѷ� ��� ����
		createPanel(); // ���� �ڽ� �гε� ����
		addListener();

		System.out.println("�̰ž�!!!!!!!!!!!!!!!!!!!!" + friends_pk_array.size());

		// â�� ������ ���α׷� ����
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.add(loginForm);
		this.add(findId);
		this.add(resetPw);
		this.add(register);
		// this.add(main);
		// this.add(address_book);
		// this.add(search_Add_Friends);
		this.pack(); // ���� ���Ե� �г��� ũ�⸸ŭ���� ������ ũ�� ����
		this.setTitle("AVOCATALK");
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(new Color(195, 237, 96));
		// this.getContentPane().setPreferredSize(new Dimension(380, 600));
		this.setResizable(false);
		this.setVisible(true);
	}

	// ���� ip������
	public void getIp() {
		try {
			inetAddess = inetAddess.getLocalHost();
			ip = inetAddess.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("������ ip : " + ip);
	}

	// MessengerServer�� ����
	public void connect() {
		try {
			client = new Socket(ip, port);
			client_Thread = new Client_Thread(this, client);
			System.out.println("=====Client_thread����=====");
			// ������ ���� ����
			client_Thread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ���� �ڽ� �г� ���� -> ������ �ڱ� �ڽ� �Ѱ���
	public void createPanel() {
		loginForm = new LoginForm(this); // �α����� ����
		register = new Register(this); // ȸ�������� ����
		System.out.println("MainFrame : createPanel() method ���� ������ register -> " + register);
		findId = new FindId(this);
		resetPw = new ResetPw(this);
		main = new Main(this);
		deleteFriends = new DeleteFriends(this);
		search_Add_Friends = new Search_Add_Friends(this);

		// Pages �迭�� JPanel �߰����ֱ�
		login_pages.add(loginForm);
		login_pages.add(register);
		login_pages.add(findId);
		login_pages.add(resetPw);

		System.out.println("MainFrame : createPanel() ���� ��µǴ� login_pages list �� ���� : " + login_pages.size());
	}

	// Ŭ���̾�Ʈ �� ��Ʈ�ѷ����� -> ������ ���ϰ� mainFrame�� �Ѱ���
	public void createController() {
		client_login_controller = new Client_login_controller(client, this);
		client_regist_controller = new Client_regist_controller(client, this);
		client_findAccount_controller = new Client_findAccount_controller(client, this);
		client_resetPW_controller = new Client_resetPW_controller(client, this);
		client_checkId_controller = new Client_checkId_controller(client, this);
		client_friendsList_controller = new Client_friendsList_controller(client, this);
		client_deleteFriend_controller = new Client_deleteFriend_controller(client, this);
		client_addFriend_controller = new Client_addFriend_controller(client, this);
		client_memberList_controller = new Client_memberList_controller(client, this);
		client_chat_controller = new Client_chat_controller(client, this);
		client_changePW_controller = new Client_changePW_controller(client, this);
		client_chat_start_controller = new Client_chat_start_controller(client, this);
		client_alarm_controller = new Client_alarm_controller(client, this);
	}

	public void addListener() {
		loginForm.lb_open_resist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				changePage(1);
				System.out.println("MainFrame : loginForm.lb_open_resist.addMouseListener ");
			}
		});
		loginForm.find_id.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				changePage(2);
				System.out.println("MainFrame : loginForm.find_id.addMouseListener ");
			}
		});
		loginForm.reset_pw.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				changePage(3);
				System.out.println("MainFrame : loginForm.reset_pw.addMouseListener ");
			}
		});
	}

	public void changePage(int num) {
		for (int i = 0; i < login_pages.size(); i++) {
			login_pages.get(i).setVisible(false);
			if (i == num) {
				login_pages.get(num).setVisible(true);
			}
		}
	}

	public String getUser_pk() {
		return user_pk;
	}

	public void setUser_pk(String user_pk) {
		this.user_pk = user_pk;
	}

	public Object[] getFriends_pk() {
		return friends_pk;
	}

	public void setFriends_pk(Object[] freinds_pk) {
		this.friends_pk = friends_pk;
	}

	public Object[] getFriends_name() {
		return friends_name;
	}

	public void setFriends_name(Object[] friends_name) {
		this.friends_name = friends_name;
	}

	public static void main(String[] args) {
		new MainFrame();
	}

	public List getFriends_pk_array() {
		return friends_pk_array;
	}

	public void setFriends_pk_array(List friends_pk_array) {
		this.friends_pk_array = friends_pk_array;
	}

	public List getFriends_name_array() {
		return friends_name_array;
	}

	public void setFriends_name_array(List friends_name_array) {
		this.friends_name_array = friends_name_array;
	}
}
