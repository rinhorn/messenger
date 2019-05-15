package messenger.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import messenger.addressbook.Address_book;
import messenger.addressbook.PersonBox;


// 채팅창에서 프로필 보내기 버튼을 클릭시 나타나는 프로필 선택창과 관련된 Class 입니다.
public class Profile_List extends JFrame{
	Address_book address_book;		// address_book 에 있는 요소에 대한 정보를 사용하기 위한 수단
	Chat_Window chat_window;		// chat_window 에 있는 요소를 사용하기 위한 수단
	
	JScrollPane scroll;
	
	JPanel panel_center;
	JPanel panel_south;
	
	JButton bt_send;
	JButton bt_cancel;
	
	
	
	public Profile_List(Chat_Window chat_window, Address_book address_book) {
		this.chat_window = chat_window;
		this.address_book = address_book;
		
		
		panel_center = new JPanel();
			panel_center.setPreferredSize(new Dimension(490, address_book.names.size()*58));
			panel_center.setBackground(new Color(255, 240, 225));
		panel_south = new JPanel();
			panel_south.setPreferredSize(new Dimension(500, 65));
			panel_south.setBackground(new Color(162,94,53));
			
		scroll = new JScrollPane(panel_center);
			scroll.setPreferredSize(new Dimension(500, 400));
			scroll.getVerticalScrollBar().setUnitIncrement(20);
		
			
		// 보내기 버튼에 대한 클릭 이벤트 동작 설정
		bt_send = new JButton("보내기");
			bt_send.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("==== bt_send ====");
					send_Profile();
				}
			});
		bt_cancel = new JButton("취소");
			bt_cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("==== bt_cancel ====");
					window_close();
				}
			});
		
		// 버튼의 크기 디자인을 설정하는 code	
		Dimension bt_size = new Dimension(100, 50);
		bt_send.setPreferredSize(bt_size);
		bt_cancel.setPreferredSize(bt_size);
		
		
		showAllFriends();
		
		panel_south.add(bt_send);
		panel_south.add(bt_cancel);
		
		this.add(scroll);
		this.add(panel_south, BorderLayout.SOUTH);
		this.setTitle("친구 프로필 보내기");
		this.setBounds(1200, 300, 550, 490);
		this.setVisible(true);
	}
	
	
	public void window_close() {
		super.dispose();
	}
	
	
	
	public void send_Profile() {
		// 프로필 보내기 버튼 클릭시 해당 profile 을 채팅창에 띄움
		// Profile_Send Class 에는 보낸 profile 을 클릭시 사용자 정보창이 뜨게 설정되어 있음
		Profile_Send send_profile = new Profile_Send(chat_window);
		
		// 메세지 창에 사용자 profile 정보를 담고 있는 JLabel component 인 send_profile 을 붙힘
		chat_window.tp_chat_box.insertComponent(send_profile);
		//연락처 보낸후 UI 갱신.
		//tp_chat_box.updateUI();
		
		// 스크롤을 항상 최하단에 위치시키기 위해 작성한 code
		//scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}
	
	
	
	
	public void showAllFriends() {
		int click_count = 0;
		
		// 기존에 존재하던 모든 친구 목록을 삭제한다.
		panel_center.removeAll();
		// 친구들의 목록을 담고있는 names List 의 목록을 조회해 새로운 친구목록을 구성한다.
		for(int i=0; i<address_book.names.size(); i++) {
			// 친구 목록을 표시하는 방법인 JLabel 에 names List 에 존재하는 친구이름 갯수 만큼 만듬
			// 이때 JLabel 에는 친구 1명의 이름이 각각 표시되게됨
			PersonBox personBox = new PersonBox(address_book, address_book.names.get(i), address_book.chat_thread);
			personBox.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//System.out.println(e.getClickCount());
					if(e.getClickCount()%2==1) {
						personBox.setBackground(Color.lightGray);
					}else {
						personBox.setBackground(new Color(235, 235, 235));
					}
				}
			});
			//personBox 내의 내용 설정을 PersonBox 객체 내에서 함.
			panel_center.add(personBox);
		}
	}
}
