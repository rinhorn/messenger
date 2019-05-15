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
	Object[] names;					// server �� ���� �޾ƿ� ģ�� ����� �����ϴ� �迭����
	
	Object[] allFriendID;  // ģ���� PK_ID�� ���� �迭	
	
	Icon icon_friend;
	Icon icon_chat;
	Icon icon_setting;
	
	MainFrame mainFrame;
	//public Main(Socket client, Client_Thread client_Thread, Object[] friends, Object[] allFriendID)
	public Main(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());
		
		bar = new JMenuBar();
		
		// menu �� ���� Design ����
		Dimension menu_size = new Dimension(100, 50);
		Font menu_font = new Font("���", Font.BOLD, 15);
		menu[0] = new JMenu("ģ��");
		menu[1] = new JMenu("ä��");
		menu[2] = new JMenu("����");
		
	
		menu[0].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				change_page(0);
				System.out.println("�� Ŭ���� �޴� : ģ�� ���");
			}
		});
		
		menu[1].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				change_page(1);
				System.out.println("�� Ŭ���� �޴� : ä�� ���");
			}
		});
		
		menu[2].addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				change_page(2);
				System.out.println("�� Ŭ���� �޴� : ä�� ����");
			}
		});
		
		// �޴��� �������� ������
		menu[0].setIcon(new MenuIcon("friends.png", 23, -5, 60, 60));
		menu[1].setIcon(new MenuIcon("Chat.png", 28, 3, 45, 45));
		menu[2].setIcon(new MenuIcon("setting.png", 28, 5, 40, 40));
		
		// �޴��� ���콺 �ö󰥶� Ŀ�� ��� ����
		menu[0].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menu[1].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		menu[2].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		// �޴��ٿ����� ���� ����
		bar.setBackground(new Color(195,237,96));
		
		
		// menu size, font, mouseListener �� ���� ����
		for(int i=0; i<menu.length; i++) {
			menu[i].setPreferredSize(menu_size);
			menu[i].setFont(menu_font);
	
			bar.add(menu[i]);
		}
		
		// �޴� �Ʒ��� ���� �гο� ���� ����;
		content = new JPanel();
		
		// �г� ������ ����
		content.setPreferredSize(new Dimension(500, 400));
		// content panel �� ���� padding �� ����
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
	
	
	// �޴� Ŭ���� ������ ��ȯ ���� method
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
