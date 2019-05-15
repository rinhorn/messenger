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
	public Connection conn; // LoginCheck Class ���� ���� Connection ����
	
	Search_Add_Friends search_friends;
	public Client_Thread chat_thread; // ä�� ������ 
	Thread thread_search;
	static Address_book address_window;
	Font bt_style = new Font("���", Font.BOLD, 15);
	
	JPanel p_title;
	JPanel p_button;
	JPanel panel_top;
	JPanel panel_top_inner;
	public JPanel panel_bottom;
	
	JLabel label_title;

	//�� 
	IconLabel lb_addFriend;
	IconLabel lb_deleteFriend;
	IconLabel lb_randomChat;
	IconLabel lb_search;
	
	//�� ������
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

	// thread �ߺ� ������ ���� ���� ����
	int threadCount;

	MainFrame mainFrame;
	Socket client;				// LoginForm ���� ���� ���ӽ� ������ client �� Socket
	
	Client_Thread client_Thread;
	
	//ģ�� ���response �����
	String flag;
	Object[] friends_pk;
	Object[] friends_name; 
	
	//personBox �迭 ����(ģ������ ������ �������)
	public ArrayList<PersonBox> personBox_array = new ArrayList<PersonBox>();
	
	//chat_window ����Ʈ ����
	public ArrayList<Chat_Window> chat_window_array = new ArrayList<Chat_Window>();
	
	// ģ�� ������ ���ؼ� deleteFriends ���� �����ϱ�
	public DeleteFriends deleteFriends;
	public String friend_pk = "";
	public String friend_name = "";
	
	
	Chat_Window  chat_window;

	
	//public Address_book(Socket client, Client_Thread client_Thread, Object[] names, Object[] allFriendID)
	public Address_book(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());
		
		// ���� �޴��� �̸��� ģ���߰� ��ư�� �����ϴ� panel ����
		p_title = new JPanel();
		
		// ���� â�� title �� ǥ��
		label_title = new JLabel("ģ�� ���");
		label_title.setFont(new Font("���", Font.BOLD, 15));
		//label_title.setForeground(Color.WHITE);

		// ģ���߰� ��ư�� Ŭ���Ͽ������� ���� ����
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
					JOptionPane.showMessageDialog(address_window, "������ ģ���� �����ϼ���.");
				} else if (!(friend_pk.equals(""))) {
					int result = JOptionPane.showConfirmDialog(Address_book.this, "ģ����Ͽ��� "+friend_name+"���� �����Ͻðڽ��ϱ�?");
					if(result == JOptionPane.OK_OPTION) {
						System.out.println("mainFrame.client_deleteFriend_controller.delete_request�� ���Ѱ��ֱ� user_pk : "+mainFrame.getUser_pk()+", friend_pk : "+friend_pk);
						mainFrame.client_deleteFriend_controller.delete_request(mainFrame.getUser_pk(), friend_pk);
					}		
				}
				 friend_pk = "";
				 friend_name = "";
			};
		});
			
		//����ä��
		lb_randomChat = new IconLabel(mainFrame.getUser_pk(), "randomChat.png", LB_WIDTH, LB_HEIGHT);
		
		// ������ �Ŵ� �̸��� ģ���߰�, ģ������ ��ư�� �����ϴ� �гο� ���� ��ġ �� component ������ ���� ���� code
		p_title.setLayout(new BorderLayout());
		p_button = new JPanel();
		p_button.setBackground(new Color(195,237,96));
		
		p_button.add(lb_addFriend, BorderLayout.EAST);
		p_button.add(lb_deleteFriend, BorderLayout.EAST);
		p_button.add(lb_randomChat, BorderLayout.EAST);
		
		p_title.setBackground(new Color(195,237,96));
		p_title.add(label_title, BorderLayout.WEST);
		p_title.add(p_button, BorderLayout.EAST);

		// ģ�� �˻��� ���� Component ����� Ű�Է½� ���� ����
		search_bar = new JTextField();
		search_bar.setPreferredSize(new Dimension(380, 30));
		search_bar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				// ����Ű�� �Է������� ģ���� ã�� Method �� �����Ѵ�.
				if (key == KeyEvent.VK_ENTER) {
					search_button_action();
				}
				
				if (key == 27) {
					search_bar.setText("");
				}
			}
		});

		// ģ�� �˻� ��ư�� ���� �̺�Ʈ �ο�
		lb_search = new IconLabel(mainFrame.getUser_pk(), "search.png", LB_WIDTH, LB_HEIGHT);
			
		// ģ����ư Ŭ���� ģ���� ã�� Method �� ����
//		lb_search.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				search_button_action();
//			}
//		});

		// �˻��� �ϱ� ���� search_bar �� �ԷµǴ� ���� ���������� Ȯ���Ѵ�.
		thread_search = new Thread() {
			@Override
			public void run() {
				// while �� ���� ������ ��� �ݺ���
				while (true) {
					// �˻� ������ ���� �ߴٸ�
					if (bt_search_flag) {
						// �˻������� �����ϴ� method �� ȣ��
						//search_person();
						//panel_bottom.updateUI();
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// �˻��� �������� �ʾҴٸ�
					} else {
						// �׸��� �˻�â�� ��ĭ�̶��
						if (search_bar.getText().isEmpty()) {
							// ģ�� ��� ��ü�� ����ϴ� method �� ȣ��
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
			// panel �� �ٴ� component(JLabel) �� ���� ó�� �ϰ� �ʹٸ� panel �� �����ϴ� scroll �� ũ�Ⱑ �ƴ� panel ��ü�� ũ�⸦ �������־���Ѵ�.
			// scroll �� �� �״�� panel �� ������ �� panel �� �ٴ� component(JLabel) �� �������� ������ ���� ���Ѵ�.
			// �� component(JLabel) �� �ٴ� ����� ���� container component �� ������ ���� component(JLabel) �� GUI �� ǥ���� �ٸ��� �ȴ�. 
		scroll = new JScrollPane(panel_bottom);
		scroll.setPreferredSize(new Dimension(490, 475));
		// ��ũ�� ���� �Ӽ� ����
		scroll.getVerticalScrollBar().setUnitIncrement(20);		// ��ũ�� �������� �ӵ� ����
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);	// ���� ��ũ���� ������� ����
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);	// ���� ��ũ�� ��븸 ���
		panel_bottom.setBackground(Color.WHITE);
		
		System.out.println("�α���Ŭ���̾�Ʈ��Ʈ�η����� ���� user_pk : "+mainFrame.getUser_pk());
		//showAllFriends();
		
		
//		lb_deleteFriend.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				int result= JOptionPane.showConfirmDialog(Address_book.this, "���� �Ͻðڽ��ϱ�?");
//				if(result==JOptionPane.OK_OPTION) {
//					System.out.println("���� ��ư ���� ���� ID �Ѿ����?  "+friend_pk);
//					deleteFriends.send_signal(friend_pk);
//				}
//			}
//		});

		this.add(panel_top, BorderLayout.NORTH);
		this.add(scroll);

		this.setBounds(0, 0, 490, 500);
		this.setVisible(false);
	}
	
	// �˻� ��ư�� ������ ����� ���� method
	public void search_button_action() {
		// �˻� ����� Thread �ߺ� ȣ���� �����ϱ� ���� threadCount ���� ������Ŵ
		// �̶� threadCount �� �� method ȣ��ø��� �ʱ�ȭ �Ǹ� thread �� �ߺ��߻��ϱ� ������ �ɹ������� ����
		threadCount++;
		// �˻� ������ �����ߴٴ� flag ���� true �� ����
		bt_search_flag = true;
		// ��ư�� ���ȴٴ°��� Ȯ���ϱ� ���� ��¹�
		System.out.println("==== bt_search ====");

		// threadCount �� 1 �� ��� thread �� ������
		if (threadCount == 1) {
			thread_search.start();
		}
	}

		
	
//	// ģ����Ͽ��� ���ϴ� ģ���� ã�� ���� �˻������ method
//	public void search_person() {
//		// �˻�â�� �Է��� ���� ������ ������ ����
//		String search_who = search_bar.getText();
//
//		// �˻�â�� �Է��� ���� ģ�� ����� ��� �ִ� names List �� ���ԵǾ� �ִٸ�
//		if (Arrays.asList(names).contains(search_who)) {
//			// ���� ����� �ִ� ģ������� ��� �����ϰ�
//			panel_bottom.removeAll();
//			//panel_bottom.add(mainFrame.personBox);
//
//			// JLabel ��Ҹ� panel_bottom �� ������ ������ ��� ������Ʈ�� �����ϴ� method �� updateUI() �� ȣ��
//			panel_bottom.updateUI();
//
//			// �˻�â�� �Է��� ���� ģ������� ��� �ִ� names List �� ���Ե��� �ʴ´ٸ�
//		} else {
//			// �˻��� �Է�â�� ������ �����
//			search_bar.setText("");
//
//			// �˻��� �����ߴٴ� flag �� �ʱ�ȭ �Ѵ�.
//			bt_search_flag = false;
//		}
//	}


	// ��ü ģ������� �����ִ� method
	public void showAllFriends() {
		// ������ �����ϴ� ��� ģ�� ����� �����Ѵ�.
		panel_bottom.removeAll();
		// ģ������ ����� ����ִ� names List �� ����� ��ȸ�� ���ο� ģ������� �����Ѵ�.
		
		System.out.println("ģ����� : " + mainFrame.getFriends_pk_array().size());
		for (int i = 0; i < mainFrame.getFriends_pk_array().size(); i++) {
			// ģ���̸��� �ߺ��� ���� �ֱ� ������, �̸����ٴ� ID�� ǥ��
			// ģ�� ����� ǥ���ϴ� ����� JLabel �� names List �� �����ϴ� ģ���̸� ���� ��ŭ ����
			// �̶� JLabel ���� ģ�� 1���� �̸��� ���� ǥ�õǰԵ�

			PersonBox personBox = new PersonBox(this, mainFrame.getUser_pk(),mainFrame.getFriends_pk_array().get(i).toString(),mainFrame.getFriends_name_array().get(i).toString());
			// personBox ���� ���� ������ PersonBox ��ü ������ ��.
			panel_bottom.add(personBox);
			personBox_array.add(personBox);
		}
		// ģ�� �˻� ���� �� ģ����� �ʱ�ȭ ȿ���� ���� ���
		panel_bottom.updateUI();
		panel_bottom.invalidate();
		mainFrame.main.change_page(0);

		System.out.println("�� ģ����� ���ſϷ� \n ==== showAllFriends() ====");
		
	}

	
	// ģ�� �߰��� ģ����Ͽ� �߰��Ǵ� Label �� �����Ͽ� Panel ���̸� �������ϴ� Method
	public void remake_BottomPanel() {
		// ģ����� �߰� �� panel_bottom �� ���̸� �������ϱ� ���� ���� panel_bottom �� scroll �� ��� Frame ����
		// �����Ͽ� �ش�.
		this.remove(panel_bottom);
		this.remove(scroll);

		// ģ������� ��� panel_bottom �� ���̴� ģ���� ����� ���� �ڵ����� �þ��.
		// panel �� ���� ���� �÷����� ���� �����Ǵ� Panel ���� ������� �ʴ´�, ���� �ø� ���̰��� panel �� �����Ŵ
		panel_bottom.setPreferredSize(new Dimension(PANEL_BOTTOM_WIDTH, PANEL_BOTTOM_HEIGHT * friends_pk.length));
		// ��ǻ� ģ������� ǥ���ϴ� panel_bottom �� scroll ó���� ���� JScrollPane ������ scroll �� ���ԵǾ�
		// Frame �� �ٴ´�.
		this.add(scroll);

		// ���� ���� ���� panel_bottom �� ģ������� �����Ͽ� ���� Method �� ȣ���Ѵ�.
		//showAllFriends(names, allFriendID);
	}
	
	
	//ä�� ��ŸƮ ����� �޴� �޼���
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
					System.out.println("PersonBox : ���� ���� "+room_num+"�� ���� ���� chatWindow�� ������!!!!!!!");
				}else {
					System.out.println("PersonBox : ���� ���� chatWindow�� ��������");
				}
			}
		}
	}
	
	// ä��â�� ������ count �� ���� listen �� ä��â �������θ� ��������
	int chat_count;
	//ä�� ����� �޴� �޼���
	public void responseChat(String room_num, String talker_pk, String talker_name, String content_type, String content,String send_time) {
		//if(chat_flag) {
		String the_room_num = null;
			for(int i=0; i<chat_window_array.size(); i++) {
				the_room_num = chat_window_array.get(i).room_num;
			}
				if(the_room_num.equals(room_num)) {
					// ������ ���� ���ٸ� ä��â�� �����ϰ� chat_array �� ��´�.
					if(chat_count == 0) {
						chat_count++;
					/*Chat_Window*/  chat_window = new Chat_Window(mainFrame, mainFrame.getUser_pk(), talker_pk, talker_name, room_num);
					chat_window_array.add(chat_window);
					}
					// �����Ǿ��ٸ� �����Ǿ��ִ� ä��â�� textarea �� �޼��� ������ ������.
					chat_window.ta_chat_box.append(talker_name+" >> "+content+"\n");
					System.out.println("Address_book - responseChat : ���� ������ room_num�� "+room_num);
				}
		//	}
	}
}






