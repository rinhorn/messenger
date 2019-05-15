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

// 프로그램이 끝날때까지 죽지 않는 메인 프레임(모든 패널의 부모역할)

public class MainFrame extends JFrame {
	InetAddress inetAddess;
	String ip; // 추후 서버 ip주소로 고정시킬 예정
	int port = 7777;
	Socket client;
	public Client_Thread client_Thread;

	// 공지) 자식객체와 클라이언트 측 클래스를 추가하는 경우에 접근제한은 무조건 public으로!
	// 자식객체 new 이후에 패널에 붙게 하기 위해 add꼭 해주세용!
	// 자식객체 보유
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

	// 클라이언트 측 컨트롤러 보유
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

	// 현재 사용중인 유저의 user_pk
	private String user_pk = null;

	// 현재 사용중인 유저의 친구_pk, 친구_이름 -> 친구가 없는 것을 고려해 초기화!!
	private Object[] friends_pk = new Object[0];
	private Object[] friends_name = new Object[0];

	List friends_pk_array = Arrays.asList(friends_pk);
	List friends_name_array = Arrays.asList(friends_name);

	// 생성자
	public MainFrame() {
		this.setLayout(new FlowLayout());
		// this.setLayout(new BorderLayout());
		getIp(); // 현재 접속중인 ip값 얻어오기
		connect(); // 소켓 연결
		createController(); // 클라이언트 측 컨트롤러 모두 생성
		createPanel(); // 각종 자식 패널들 생성
		addListener();

		System.out.println("이거야!!!!!!!!!!!!!!!!!!!!" + friends_pk_array.size());

		// 창이 닫히면 프로그램 종료
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
		this.pack(); // 추후 덮게될 패널의 크기만큼으로 프레임 크기 조절
		this.setTitle("AVOCATALK");
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(new Color(195, 237, 96));
		// this.getContentPane().setPreferredSize(new Dimension(380, 600));
		this.setResizable(false);
		this.setVisible(true);
	}

	// 접속 ip얻어오기
	public void getIp() {
		try {
			inetAddess = inetAddess.getLocalHost();
			ip = inetAddess.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println("접속한 ip : " + ip);
	}

	// MessengerServer와 연결
	public void connect() {
		try {
			client = new Socket(ip, port);
			client_Thread = new Client_Thread(this, client);
			System.out.println("=====Client_thread가동=====");
			// 쓰레드 가동 시작
			client_Thread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 각종 자식 패널 생성 -> 생성시 자기 자신 넘겨줌
	public void createPanel() {
		loginForm = new LoginForm(this); // 로그인폼 생성
		register = new Register(this); // 회원가입폼 생성
		System.out.println("MainFrame : createPanel() method 에서 생성한 register -> " + register);
		findId = new FindId(this);
		resetPw = new ResetPw(this);
		main = new Main(this);
		deleteFriends = new DeleteFriends(this);
		search_Add_Friends = new Search_Add_Friends(this);

		// Pages 배열에 JPanel 추가해주기
		login_pages.add(loginForm);
		login_pages.add(register);
		login_pages.add(findId);
		login_pages.add(resetPw);

		System.out.println("MainFrame : createPanel() 에서 출력되는 login_pages list 의 길이 : " + login_pages.size());
	}

	// 클라이언트 측 컨트롤러생성 -> 생성시 소켓과 mainFrame을 넘겨줌
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
