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
	
	// ä��â�� �Է��� �޽����� ���� �������
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
						//�ǽð����� ������ �Է��� content����
						message = tf_insert_message_window.getText();
						
						if(!tf_insert_message_window.getText().isEmpty()) {

							//DB�� ������ �Է��� content���� �߰���û
							content_type = "1";
							content = message;
							request_dialogue();
			
							ta_chat_box.append("My Message >> "+content+"\n");
							// ����������!!!!!!!!!!!!!!!!!!!!!!!!!�� �޼��� ����
							//.send(message);
							
							tf_insert_message_window.setText("");
							// �Ʒ��� Frame this �� ã�� ���� ����
							// System.out.println(this);						--> Chat_Window �� �ν��Ͻ� �ּҰ�
							// System.out.println(Chat_Window.this);	--> Chat_Window Frame source
							content_type = null;
							content = null;
						}
					}
				}
			});
			
		bt_send_message = new JButton("����");
			bt_send_message.addActionListener(this);
			
		bt_emoticon = new JButton("�̸�Ƽ��");
			bt_emoticon.addActionListener(this);
			
		bt_send_Image = new JButton("�̹��� ����");
			bt_send_Image.addActionListener(this);
			
		bt_send_File = new JButton("���� ����");
			bt_send_File.addActionListener(this);
			
		bt_send_Profile = new JButton("������ ������");
			bt_send_Profile.addActionListener(this);
			
			
		chat_menu_panel = new JPanel();
			chat_menu_panel.add(bt_emoticon);
			chat_menu_panel.add(bt_send_Image);
			chat_menu_panel.add(bt_send_File);
			chat_menu_panel.add(bt_send_Profile);
		
			chat_top_panel = new JPanel();
//			tp_chat_box = new JTextPane();			// JTextArea �� �ٸ��� �̹����� ǥ�� ����
//				tp_chat_box.setEditable(false);			// JTextPane �� Editer ��带 ���
//				tp_chat_box.setAutoscrolls(true);
		
			ta_chat_box = new JTextArea();
				ta_chat_box.setEditable(false);
				ta_chat_box.setAutoscrolls(true);
				ta_chat_box.setPreferredSize(new Dimension(100, 400));
				//		�� �ּ� �ڵ�� messageBox(Label) �� JTextPane �� �߰� �� ��ũ�� ���ۿ� �̻������� ����Ŵ
				//		�̻� �����̶� ���� ��ũ���� �ִ�ġ�� �̵� ���ϴ� ������ �ǹ���
				
			scroll = new JScrollPane(ta_chat_box);
				// ��ũ�� �� ũ�� ����
				// ��ũ�� ���� ũ��� ä��â ũ�⿡ ������ �ش�.
			scroll.setPreferredSize(new Dimension(ta_chat_box_WIDTH, ta_chat_box_HEIGHT));
				//scroll.setPreferredSize(new Dimension(ta_chat_box_WIDTH, ta_chat_box_HEIGHT));
				// ���� ��ũ�Ѹ� ���
				scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				// ���� ��ũ�� ��� ����
				scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				// ���콺 �� ���۽� ��ũ�ѹ� �ӵ� ����
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
		
		this.setTitle(chat_friend_name+"�԰��� ä��");
		this.setSize(WIDTH, HEIGHT);
		this.setVisible(true);
	}
		
		

	@Override
	public void actionPerformed(ActionEvent e) {
		Object bt_obj = e.getSource();
		button_action(bt_obj);
	}
	
	public void button_action(Object bt_obj) {
		// �޼��� ������ ��ư Ŭ�������� ��������
		if(bt_obj == bt_send_message) {
			System.out.println("====bt_send_message====");
			//�ǽð����� ������ �Է��� content����
			message = tf_insert_message_window.getText();

			//����������� ����!!!!!!!!!!!!!!!!!!!!!

			//DB�� ������ �Է��� content���� �߰���û
			content_type = "1";
			content = message;
			request_dialogue();
			
			//���� ���� ���� ���̱�
			ta_chat_box.append("My Message >> "+content+"\n");
			
			// ����������!!!!!!!!!!!!!!!!!!!!!!!!!�� �޼��� ����
			//.send(message);
			//address_book.chat_thread.send(message);
			tf_insert_message_window.setText("");
			content_type = null;
			content = null;
			
		// �̸�Ƽ�� ��ư�� �������� ��������
		}else if(bt_obj == bt_emoticon) {
			System.out.println("====bt_emoticon====");
			bt_emoticon_click_count++;
			//System.out.println(bt_emoticon_click_count);
			
			// �ش� ��ư�� Ŭ���� Ƚ���� Ȧ���� ��쿡�� emoticon â�� ���
			if(bt_emoticon_click_count%2==1) {
				emoticon = new Emoticon3(this);
				
			// �ش� ��ư�� Ŭ���� Ƚ���� ¦���� ��쿡�� emoticon â�� ����
			}else {
				chat_bottom_panel.remove(emoticon);
				this.setSize(new Dimension(WIDTH, HEIGHT));
			}
		
		// �̹��� ������ ��ư�� Ŭ���Ͽ����� ���� ����
		}else if(bt_obj == bt_send_Image) {
			System.out.println("====bt_send_Image====");
			SendImage sendImage = new SendImage();
			sendImage.sendImage(this);
			
		// ���� ������ ��ư�� Ŭ���Ͽ����� ���� ����
		}else if(bt_obj == bt_send_File) {
			System.out.println("====bt_send_File====");
			//create_sendFile_Window();
			
		// ������ ������ ��ư�� Ŭ�������� ��������
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
		System.out.println("chat_window : request_dialogue ȣ����..");
		System.out.println("chat_window ���� �Ѱ��� �� : user_pk, chat_friend_pk, room_num, content_type, content : "+user_pk+","+chat_friend_pk+","+ room_num+","+content_type+","+content);
		mainFrame.client_chat_controller.chat_request(user_pk, chat_friend_pk, room_num, content_type, content);
	}
	
	// ä��â�� ������ �����ϴ� method 
	public void close_chat() {
		// â�� ���� ��� �ش� personBox �� ���� count �� �ʱ�ȭ��
		/*if(address_book.chat_thread.select_opend_window) {
			personbox.click_count=0;
		}*/
		//System.out.println(personbox.click_count);
		super.dispose();
	}


}
