package messenger.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import messenger.MainFrame;
import messenger.addressbook.Address_book;
import messenger.chat.ChattingRoom;
import messenger.icon.MenuIcon;
import messenger.setting.Setting;

public class Main extends JPanel{
	Socket client;
	JPanel content;
	JMenuBar bar;
	JMenu[] menu = new JMenu[3];
	JPanel[] page = new JPanel[3];
	public Address_book address_book;
	public ChattingRoom chattingRoom;
	
	Image img_friend_icon;
	Image img_chat_icon;
	Image img_setting_icon;
	Object[] names;					// server 로 부터 받아온 친구 목록을 저장하는 배열변수
	
	Object[] allFriendID;  // 친구의 PK_ID를 담을 배열	
	
	Icon icon_friend;
	Icon icon_chat;
	Icon icon_setting;
	
	MainFrame mainFrame;
	//public Main(Socket client, Client_Thread client_Thread, Object[] friends, Object[] allFriendID)
	public Main(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());
		
		bar = new JMenuBar();
		
		// menu 에 대한 Design 설정
		Dimension menu_size = new Dimension(100, 50);
		Font menu_font = new Font("고딕", Font.BOLD, 15);
		menu[0] = new JMenu("친구");
		menu[1] = new JMenu("채팅");
		menu[2] = new JMenu("설정");
		
	
		menu[0].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				change_page(0);
				System.out.println("▶ 클릭한 메뉴 : 친구 목록");
			}
		});
		
		menu[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				change_page(1);
				System.out.println("▶ 클릭한 메뉴 : 채팅 목록");
			}
		});
		
		menu[2].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				change_page(2);
				System.out.println("▶ 클릭한 메뉴 : 채팅 설정");
			}
		});
		
		// 메뉴에 아이콘을 적용함
		menu[0].setIcon(new MenuIcon("friends.png", 23, -5, 60, 60));
		menu[1].setIcon(new MenuIcon("Chat.png", 28, 3, 45, 45));
		menu[2].setIcon(new MenuIcon("setting.png", 28, 5, 40, 40));
		
		// 메뉴에 마우스 올라갈때 커서 모양 설정
		menu[0].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menu[1].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menu[2].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		// 메뉴바에대한 색상 설정
		bar.setBackground(new Color(195,237,96));
		
		
		// menu size, font, mouseListener 를 각각 설정
		for(int i=0; i<menu.length; i++) {
			menu[i].setPreferredSize(menu_size);
			menu[i].setFont(menu_font);
	
			bar.add(menu[i]);
		}
		
		// 메뉴 아래에 붙을 패널에 관한 설정;
		content = new JPanel();
		
		// 패널 디자인 설정
		content.setPreferredSize(new Dimension(500, 400));
		// content panel 의 내부 padding 을 설정
		content.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));
		address_book = new Address_book(this.mainFrame);
		page[0] = address_book;
		chattingRoom = new ChattingRoom(this.mainFrame);
		page[1] = chattingRoom;
		page[2] = new Setting();
		
		
		content.add(page[0]);
		content.add(page[1]);
		content.add(page[2]);
		

		
		this.add(bar, BorderLayout.NORTH);
		this.add(content);
		this.setVisible(true); 
		this.setPreferredSize(new Dimension(497, 640));
		//this.setResizable(false);
	}
	
	
	// 메뉴 클릭시 페이지 전환 동작 method
	public void change_page(int num) {
		for(int i=0; i<page.length; i++) {
			if(i==num) {
				page[i].setVisible(true);
			}else {
				page[i].setVisible(false);
			}
		}
	}
}
