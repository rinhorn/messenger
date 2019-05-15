package messenger.addressbook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.AncestorListener;

import messenger.Client_Thread;
import messenger.MainFrame;
import messenger.chat.Chat_Window;
import messenger.chat.MessageBox;
import messenger.loginForm.IconButton;
import messenger.loginForm.LoginForm;

public class Address_book extends JPanel{
	public Connection conn; // LoginCheck Class 에서 얻어온 Connection 정보
	
	Search_Add_Friends search_friends;
	public Client_Thread chat_thread; // 채팅 쓰레드 
	Thread thread_search;
	static Address_book address_window;
	Font bt_style = new Font("고딕", Font.BOLD, 15);
	
	JPanel p_title;
	JPanel p_button;
	JPanel panel_top;
	JPanel panel_top_inner;
	public JPanel panel_bottom;
	
	JLabel label_title;

	//라벨 
	IconLabel lb_addFriend;
	IconLabel lb_deleteFriend;
	IconLabel lb_randomChat;
	IconLabel lb_search;
	
	//라벨 사이즈
	public static final int LB_WIDTH=40;
	public static final int LB_HEIGHT=40;
	
	JTextField search_bar;
	
	boolean bt_search_flag = false;
	
	public static final int PANEL_BOTTOM_WIDTH = 450;
	int PANEL_BOTTOM_HEIGHT = 58;
	JScrollPane scroll;
	JTextPane tp_bottom;
	
	String room_num;

	// PersonBox personBox;
	//public List<String> names = new ArrayList<String>();
	// List<String> friends = new ArrayList<String>();

	// thread 중복 실행을 막기 위한 변수
	int threadCount;

	MainFrame mainFrame;
	Socket client;				// LoginForm 에서 서버 접속시 생성된 client 의 Socket
	
	Client_Thread client_Thread;
	
	//친구 목록response 결과값
	String flag;
	Object[] friends_pk;
	Object[] friends_name; 
	
	//personBox 배열 생성(친구들의 정보를 담기위해)
	public ArrayList<PersonBox> personBox_array = new ArrayList<PersonBox>();
	
	//chat_window 리스트 생성
	public ArrayList<Chat_Window> chat_window_array = new ArrayList<Chat_Window>();
	
	// 친구 삭제를 위해서 deleteFriends 새로 생성하기
	public DeleteFriends deleteFriends;
	public String friend_pk = "";
	public String friend_name = "";
	
	
	Chat_Window  chat_window;

	
	//public Address_book(Socket client, Client_Thread client_Thread, Object[] names, Object[] allFriendID)
	public Address_book(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());
		
		// 현재 메뉴의 이름과 친구추가 버튼을 포함하는 panel 설정
		p_title = new JPanel();
		
		// 현재 창의 title 을 표시
		label_title = new JLabel("친구 목록");
		label_title.setFont(new Font("고딕", Font.BOLD, 15));
		//label_title.setForeground(Color.WHITE);

		// 친구추가 버튼을 클릭하였을때의 동작 설정
		lb_addFriend = new IconLabel(mainFrame.getUser_pk(), "addFriend.png", LB_WIDTH, LB_HEIGHT);
		lb_addFriend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("==== bt_addFriend ====");
				mainFrame.search_Add_Friends.setVisible(true);
				mainFrame.client_memberList_controller.memberList_request();
			}
		});

		lb_deleteFriend = new IconLabel(mainFrame.getUser_pk(), "deleteFriends.png", LB_WIDTH, LB_HEIGHT);
		lb_deleteFriend.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				System.out.println("==== bt_deleteFriend ====");
				 if(friend_pk.equals("")){
					JOptionPane.showMessageDialog(address_window, "삭제할 친구를 선택하세요.");
				} else if (!(friend_pk.equals(""))) {
					int result = JOptionPane.showConfirmDialog(Address_book.this, "친구목록에서 "+friend_name+"님을 삭제하시겠습니까?");
					if(result == JOptionPane.OK_OPTION) {
						System.out.println("mainFrame.client_deleteFriend_controller.delete_request로 값넘겨주기 user_pk : "+mainFrame.getUser_pk()+", friend_pk : "+friend_pk);
						mainFrame.client_deleteFriend_controller.delete_request(mainFrame.getUser_pk(), friend_pk);
					}		
				}
				 friend_pk = "";
				 friend_name = "";
			};
		});
			
		//랜덤채팅
		lb_randomChat = new IconLabel(mainFrame.getUser_pk(), "randomChat.png", LB_WIDTH, LB_HEIGHT);
		
		// 현재의 매뉴 이름과 친구추가, 친구삭제 버튼을 포함하는 패널에 대한 위치 및 component 부착에 관한 설정 code
		p_title.setLayout(new BorderLayout());
		p_button = new JPanel();
		p_button.setBackground(new Color(195,237,96));
		
		p_button.add(lb_addFriend, BorderLayout.EAST);
		p_button.add(lb_deleteFriend, BorderLayout.EAST);
		p_button.add(lb_randomChat, BorderLayout.EAST);
		
		p_title.setBackground(new Color(195,237,96));
		p_title.add(label_title, BorderLayout.WEST);
		p_title.add(p_button, BorderLayout.EAST);

		// 친구 검색에 사용될 Component 선언과 키입력시 동작 설정
		search_bar = new JTextField();
		search_bar.setPreferredSize(new Dimension(380, 30));
		search_bar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				// 엔터키를 입력했을때 친구를 찾는 Method 를 실행한다.
				if (key == KeyEvent.VK_ENTER) {
					search_button_action();
				}
				
				if (key == 27) {
					search_bar.setText("");
				}
			}
		});

		// 친구 검색 버튼에 대한 이벤트 부여
		lb_search = new IconLabel(mainFrame.getUser_pk(), "search.png", LB_WIDTH, LB_HEIGHT);
			
		// 친구버튼 클릭시 친구를 찾는 Method 를 실행
//		lb_search.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				search_button_action();
//			}
//		});

		// 검색을 하기 위해 search_bar 에 입력되는 값을 연속적으로 확인한다.
		thread_search = new Thread() {
			@Override
			public void run() {
				// while 문 안의 동작을 계속 반복함
				while (true) {
					// 검색 동작을 수행 했다면
					if (bt_search_flag) {
						// 검색동작을 수행하는 method 를 호출
						//search_person();
						//panel_bottom.updateUI();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// 검색을 수행하지 않았다면
					} else {
						// 그리고 검색창이 빈칸이라면
						if (search_bar.getText().isEmpty()) {
							// 친구 목록 전체를 출력하는 method 를 호출
							//showAllFriends(names, allFriendID);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

				}
			}
		};

		panel_top = new JPanel();
		panel_top_inner = new JPanel();
		panel_top_inner.add(search_bar);
		panel_top_inner.add(lb_search);
		panel_top_inner.setBackground(new Color(195,237,96));
		panel_top.setBackground(new Color(195,237,96));
		panel_top.setLayout(new BorderLayout());
		panel_top.add(p_title, BorderLayout.SOUTH);
		panel_top.add(panel_top_inner);

		panel_bottom = new JPanel();
		panel_bottom.setPreferredSize(new Dimension(470, PANEL_BOTTOM_HEIGHT * mainFrame.getFriends_pk().length));			
			// panel 에 붙는 component(JLabel) 를 개행 처리 하고 싶다면 panel 을 포함하는 scroll 의 크기가 아닌 panel 자체의 크기를 지정해주어야한다.
			// scroll 은 말 그대로 panel 을 포함할 뿐 panel 에 붙는 component(JLabel) 에 직접적인 관여를 하지 못한다.
			// 즉 component(JLabel) 가 붙는 대상인 직계 container component 의 설정에 따라 component(JLabel) 의 GUI 적 표현이 다르게 된다. 
		scroll = new JScrollPane(panel_bottom);
		scroll.setPreferredSize(new Dimension(490, 475));
		// 스크롤 관련 속성 조정
		scroll.getVerticalScrollBar().setUnitIncrement(20);		// 스크롤 움직임의 속도 조절
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	// 가로 스크롤은 사용하지 않음
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	// 세로 스크롤 사용만 허용
		panel_bottom.setBackground(Color.WHITE);
		
		System.out.println("로그인클라이언트컨트로러부터 받은 user_pk : "+mainFrame.getUser_pk());
		//showAllFriends();
		
		
//		lb_deleteFriend.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int result= JOptionPane.showConfirmDialog(Address_book.this, "삭제 하시겠습니까?");
//				if(result==JOptionPane.OK_OPTION) {
//					System.out.println("삭제 버튼 누른 다음 ID 넘어오나?  "+friend_pk);
//					deleteFriends.send_signal(friend_pk);
//				}
//			}
//		});

		this.add(panel_top, BorderLayout.NORTH);
		this.add(scroll);

		this.setBounds(0, 0, 490, 500);
		this.setVisible(false);
	}
	
	// 검색 버튼을 눌렀을 경우의 동작 method
	public void search_button_action() {
		// 검색 수행시 Thread 중복 호출을 방지하기 위한 threadCount 값을 증가시킴
		// 이때 threadCount 는 본 method 호출시마다 초기화 되면 thread 가 중복발생하기 때문에 맴버변수로 선언
		threadCount++;
		// 검색 동작을 수행했다는 flag 값을 true 로 설정
		bt_search_flag = true;
		// 버튼이 눌렸다는것을 확인하기 위한 출력문
		System.out.println("==== bt_search ====");

		// threadCount 가 1 일 경우 thread 를 시작함
		if (threadCount == 1) {
			thread_search.start();
		}
	}

		
	
//	// 친구목록에서 원하는 친구를 찾기 위한 검색기능의 method
//	public void search_person() {
//		// 검색창에 입력한 값을 가져와 변수에 저장
//		String search_who = search_bar.getText();
//
//		// 검색창에 입력한 값이 친구 목록을 담고 있는 names List 에 포함되어 있다면
//		if (Arrays.asList(names).contains(search_who)) {
//			// 현재 띄워져 있는 친구목록을 모두 삭제하고
//			panel_bottom.removeAll();
//			//panel_bottom.add(mainFrame.personBox);
//
//			// JLabel 요소를 panel_bottom 에 붙혔기 때문에 요소 업데이트를 적용하는 method 인 updateUI() 를 호출
//			panel_bottom.updateUI();
//
//			// 검색창에 입력한 값이 친구목록을 담고 있는 names List 에 포함되지 않는다면
//		} else {
//			// 검색어 입력창의 내용을 지우고
//			search_bar.setText("");
//
//			// 검색을 수행했다는 flag 를 초기화 한다.
//			bt_search_flag = false;
//		}
//	}


	// 전체 친구목록을 보여주는 method
	public void showAllFriends() {
		// 기존에 존재하던 모든 친구 목록을 삭제한다.
		panel_bottom.removeAll();
		// 친구들의 목록을 담고있는 names List 의 목록을 조회해 새로운 친구목록을 구성한다.
		
		System.out.println("친구명수 : " + mainFrame.getFriends_pk_array().size());
		for (int i = 0; i < mainFrame.getFriends_pk_array().size(); i++) {
			// 친구이름이 중복될 수도 있기 때문에, 이름보다는 ID로 표기
			// 친구 목록을 표시하는 방법인 JLabel 에 names List 에 존재하는 친구이름 갯수 만큼 만듬
			// 이때 JLabel 에는 친구 1명의 이름이 각각 표시되게됨

			PersonBox personBox = new PersonBox(this, mainFrame.getUser_pk(),mainFrame.getFriends_pk_array().get(i).toString(),mainFrame.getFriends_name_array().get(i).toString());
			// personBox 내의 내용 설정을 PersonBox 객체 내에서 함.
			panel_bottom.add(personBox);
			personBox_array.add(personBox);
		}
		// 친구 검색 동작 후 친구목록 초기화 효과를 위해 사용
		panel_bottom.updateUI();
		panel_bottom.invalidate();
		mainFrame.main.change_page(0);

		System.out.println("▶ 친구목록 갱신완료 \n ==== showAllFriends() ====");
		
	}

	
	// 친구 추가시 친구목록에 추가되는 Label 에 대응하여 Panel 길이를 재조정하는 Method
	public void remake_BottomPanel() {
		// 친구목록 추가 및 panel_bottom 의 길이를 재조정하기 위해 기존 panel_bottom 과 scroll 을 모두 Frame 에서
		// 제거하여 준다.
		this.remove(panel_bottom);
		this.remove(scroll);

		// 친구목록을 담는 panel_bottom 의 길이는 친구의 명수에 따라 자동으로 늘어난다.
		// panel 의 길이 값을 늘렸을뿐 새로 생성되는 Panel 에는 적용되지 않는다, 따라서 늘린 길이값을 panel 에 적용시킴
		panel_bottom.setPreferredSize(new Dimension(PANEL_BOTTOM_WIDTH, PANEL_BOTTOM_HEIGHT * friends_pk.length));
		// 사실상 친구목록을 표현하는 panel_bottom 은 scroll 처리를 위해 JScrollPane 형태의 scroll 에 포함되어
		// Frame 에 붙는다.
		this.add(scroll);

		// 이후 새로 만든 panel_bottom 에 친구목록을 갱신하여 띄우는 Method 를 호출한다.
		//showAllFriends(names, allFriendID);
	}
	
	
	//채팅 스타트 결과를 받는 메서드
	Chat_Window  chat_window1;
	public void responseChatStart(boolean flag, String user_pk, String chat_friend_pk, String chat_friend_name, String room_num) {
		this.room_num=room_num;
		if(flag) {
			/*Chat_Window*/  chat_window1 = new Chat_Window(mainFrame, user_pk, chat_friend_pk, chat_friend_name, room_num);
			chat_window_array.add(chat_window);
			for(int i=0; i<personBox_array.size(); i++) {
				String the_friend_pk = personBox_array.get(i).friend_pk;
				if(the_friend_pk.equals(chat_friend_pk)) {
					personBox_array.get(i).room_num = room_num;
					System.out.println("PersonBox : 나는 지금 "+room_num+"번 방을 가진 chatWindow를 생성중!!!!!!!");
				}else {
					System.out.println("PersonBox : 나는 지금 chatWindow를 생성실패");
				}
			}
		}
	}
	
	// 채팅창을 생성한 count 를 통해 listen 시 채팅창 생성여부를 결정지음
	int chat_count;
	//채팅 결과를 받는 메서드
	public void responseChat(String room_num, String talker_pk, String talker_name, String content_type, String content,String send_time) {
		//if(chat_flag) {
		String the_room_num = null;
			for(int i=0; i<chat_window_array.size(); i++) {
				the_room_num = chat_window_array.get(i).room_num;
			}
				if(the_room_num.equals(room_num)) {
					// 생성된 적이 없다면 채팅창을 생성하고 chat_array 에 담는다.
					if(chat_count == 0) {
						chat_count++;
					/*Chat_Window*/  chat_window = new Chat_Window(mainFrame, mainFrame.getUser_pk(), talker_pk, talker_name, room_num);
					chat_window_array.add(chat_window);
					}
					// 생성되었다면 생성되어있는 채팅창의 textarea 에 메세지 내용을 붙힌다.
					chat_window.ta_chat_box.append(talker_name+" >> "+content+"\n");
					System.out.println("Address_book - responseChat : 내가 선택한 room_num은 "+room_num);
				}
		//	}
	}
}






