package messenger.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import messenger.MainFrame;
import messenger.addressbook.Address_book;
import messenger.addressbook.PersonBox;

public class Chat_Window extends JFrame implements ActionListener{
	public static final int WIDTH = 450;
	public static final int HEIGHT = 630;
	
	int messageBox_quantity;
	
	JPanel chat_top_panel;
	JPanel chat_bottom_panel;
	JPanel chat_menu_panel;
	public JTextPane tp_chat_box;
		int tp_chat_box_WIDTH = 400;
		int tp_chat_box_HEIGHT = 500;
		
	public JTextArea ta_chat_box;
		int ta_chat_box_WIDTH = 400;
		int ta_chat_box_HEIGHT = 500;
	
	JTextField tf_insert_message_window;
	public JScrollPane scroll;

	JButton bt_send_message;
	JButton bt_emoticon;
		int bt_emoticon_click_count;
	JButton bt_send_Image;
	JButton bt_send_File;
	JButton bt_send_Profile;
	
	Emoticon3 emoticon;
	
	// 채팅창에 입력한 메시지를 담을 멤버변수
	String message;
	
//	OutputStream output_stream = null;
//	OutputStreamWriter output_stream_writer = null;
//	BufferedWriter bufferd_writer = null;
	
	MainFrame mainFrame;
	public String user_pk, chat_friend_pk, chat_friend_name, room_num;
	String content_type = null;
	String content = null;
	Address_book address_book;
	
	public Chat_Window(MainFrame mainFrame, String user_pk, String chat_friend_pk, String chat_friend_name, String room_num) {
		this.mainFrame = mainFrame;
		this.user_pk = user_pk;
		this.chat_friend_pk = chat_friend_pk;
		this.chat_friend_name = chat_friend_name;
		this.room_num = room_num;
	
		tf_insert_message_window = new JTextField(25);
			tf_insert_message_window.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if(key == KeyEvent.VK_ENTER) {
						//실시간으로 유저가 입력한 content내용
						message = tf_insert_message_window.getText();
						
						if(!tf_insert_message_window.getText().isEmpty()) {

							//DB로 유저가 입력한 content내용 추가요청
							content_type = "1";
							content = message;
							request_dialogue();
			
							ta_chat_box.append("My Message >> "+content+"\n");
							// 서버스레드!!!!!!!!!!!!!!!!!!!!!!!!!로 메세지 전송
							//.send(message);
							
							tf_insert_message_window.setText("");
							// 아래는 Frame this 를 찾기 위한 과정
							// System.out.println(this);						--> Chat_Window 의 인스턴스 주소값
							// System.out.println(Chat_Window.this);	--> Chat_Window Frame source
							content_type = null;
							content = null;
						}
					}
				}
			});
			
		bt_send_message = new JButton("전송");
			bt_send_message.addActionListener(this);
			
		bt_emoticon = new JButton("이모티콘");
			bt_emoticon.addActionListener(this);
			
		bt_send_Image = new JButton("이미지 전송");
			bt_send_Image.addActionListener(this);
			
		bt_send_File = new JButton("파일 전송");
			bt_send_File.addActionListener(this);
			
		bt_send_Profile = new JButton("프로필 보내기");
			bt_send_Profile.addActionListener(this);
			
			
		chat_menu_panel = new JPanel();
			chat_menu_panel.add(bt_emoticon);
			chat_menu_panel.add(bt_send_Image);
			chat_menu_panel.add(bt_send_File);
			chat_menu_panel.add(bt_send_Profile);
		
			chat_top_panel = new JPanel();
//			tp_chat_box = new JTextPane();			// JTextArea 와 다르게 이미지도 표현 가능
//				tp_chat_box.setEditable(false);			// JTextPane 의 Editer 모드를 잠금
//				tp_chat_box.setAutoscrolls(true);
		
			ta_chat_box = new JTextArea();
				ta_chat_box.setEditable(false);
				ta_chat_box.setAutoscrolls(true);
				ta_chat_box.setPreferredSize(new Dimension(100, 400));
				//		위 주석 코드는 messageBox(Label) 을 JTextPane 에 추가 후 스크롤 동작에 이상현상을 일으킴
				//		이상 현상이라 함은 스크롤이 최대치로 이동 못하는 현상을 의미함
				
			scroll = new JScrollPane(ta_chat_box);
				// 스크롤 바 크기 조정
				// 스크롤 바의 크기는 채팅창 크기에 영향을 준다.
			scroll.setPreferredSize(new Dimension(ta_chat_box_WIDTH, ta_chat_box_HEIGHT));
				//scroll.setPreferredSize(new Dimension(ta_chat_box_WIDTH, ta_chat_box_HEIGHT));
				// 세로 스크롤만 사용
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				// 가로 스크롤 사용 제한
				scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				// 마우스 휠 동작시 스크롤바 속도 지정
				scroll.getVerticalScrollBar().setUnitIncrement(20);		
			//chat_top_panel.setBackground(Color.YELLOW);
			chat_top_panel.setPreferredSize(new Dimension(190, 400));
			chat_top_panel.add(scroll);
			
		chat_bottom_panel = new JPanel();
			chat_bottom_panel.setLayout(new FlowLayout());
			chat_bottom_panel.add(chat_menu_panel);
			chat_bottom_panel.add(tf_insert_message_window);
			chat_bottom_panel.add(bt_send_message);
			
		
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close_chat();
			}
		});
		
		this.add(scroll, BorderLayout.NORTH);
		this.add(chat_bottom_panel);
		
		this.setTitle(chat_friend_name+"님과의 채팅");
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);
	}
		
		

	@Override
	public void actionPerformed(ActionEvent e) {
		Object bt_obj = e.getSource();
		button_action(bt_obj);
	}
	
	public void button_action(Object bt_obj) {
		// 메세지 보내기 버튼 클릭했을시 동작제어
		if(bt_obj == bt_send_message) {
			System.out.println("====bt_send_message====");
			//실시간으로 유저가 입력한 content내용
			message = tf_insert_message_window.getText();

			//서버스레드로 전송!!!!!!!!!!!!!!!!!!!!!

			//DB로 유저가 입력한 content내용 추가요청
			content_type = "1";
			content = message;
			request_dialogue();
			
			//내가 말한 내용 붙이기
			ta_chat_box.append("My Message >> "+content+"\n");
			
			// 서버스레드!!!!!!!!!!!!!!!!!!!!!!!!!로 메세지 전송
			//.send(message);
			//address_book.chat_thread.send(message);
			tf_insert_message_window.setText("");
			content_type = null;
			content = null;
			
		// 이모티콘 버튼을 눌렀을시 동작제어
		}else if(bt_obj == bt_emoticon) {
			System.out.println("====bt_emoticon====");
			bt_emoticon_click_count++;
			//System.out.println(bt_emoticon_click_count);
			
			// 해당 버튼을 클릭한 횟수가 홀수인 경우에는 emoticon 창을 띄움
			if(bt_emoticon_click_count%2==1) {
				emoticon = new Emoticon3(this);
				
			// 해당 버튼을 클릭한 횟수가 짝수일 경우에는 emoticon 창을 닫음
			}else {
				chat_bottom_panel.remove(emoticon);
				this.setSize(new Dimension(WIDTH, HEIGHT));
			}
		
		// 이미지 보내기 버튼을 클릭하였을시 동작 제어
		}else if(bt_obj == bt_send_Image) {
			System.out.println("====bt_send_Image====");
			SendImage sendImage = new SendImage();
			sendImage.sendImage(this);
			
		// 파일 보내기 버튼을 클릭하였을시 동작 제어
		}else if(bt_obj == bt_send_File) {
			System.out.println("====bt_send_File====");
			//create_sendFile_Window();
			
		// 프로필 보내기 버튼을 클릭했을시 동작제어
		}else if(bt_obj == bt_send_Profile) {
			System.out.println("====bt_send_Profile====");
			Profile_List profile_list = new Profile_List(this, address_book);
						
		}
	}
	


//		try {
//			output_stream=client.getOutputStream();
//			output_stream_writer= new OutputStreamWriter(output_stream);
//			bufferd_writer = new BufferedWriter(output_stream_writer);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	
	public void request_dialogue(){
		System.out.println("chat_window : request_dialogue 호출중..");
		System.out.println("chat_window 에서 넘겨줄 값 : user_pk, chat_friend_pk, room_num, content_type, content : "+user_pk+","+chat_friend_pk+","+ room_num+","+content_type+","+content);
		mainFrame.client_chat_controller.chat_request(user_pk, chat_friend_pk, room_num, content_type, content);
	}
	
	// 채팅창은 닫힐때 동작하는 method 
	public void close_chat() {
		// 창이 닫힐 경우 해당 personBox 에 대한 count 가 초기화됨
		/*if(address_book.chat_thread.select_opend_window) {
			personbox.click_count=0;
		}*/
		//System.out.println(personbox.click_count);
		super.dispose();
	}


}
