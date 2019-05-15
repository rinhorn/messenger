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


// ä��â���� ������ ������ ��ư�� Ŭ���� ��Ÿ���� ������ ����â�� ���õ� Class �Դϴ�.
public class Profile_List extends JFrame{
	Address_book address_book;		// address_book �� �ִ� ��ҿ� ���� ������ ����ϱ� ���� ����
	Chat_Window chat_window;		// chat_window �� �ִ� ��Ҹ� ����ϱ� ���� ����
	
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
		
			
		// ������ ��ư�� ���� Ŭ�� �̺�Ʈ ���� ����
		bt_send = new JButton("������");
			bt_send.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("==== bt_send ====");
					send_Profile();
				}
			});
		bt_cancel = new JButton("���");
			bt_cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("==== bt_cancel ====");
					window_close();
				}
			});
		
		// ��ư�� ũ�� �������� �����ϴ� code	
		Dimension bt_size = new Dimension(100, 50);
		bt_send.setPreferredSize(bt_size);
		bt_cancel.setPreferredSize(bt_size);
		
		
		showAllFriends();
		
		panel_south.add(bt_send);
		panel_south.add(bt_cancel);
		
		this.add(scroll);
		this.add(panel_south, BorderLayout.SOUTH);
		this.setTitle("ģ�� ������ ������");
		this.setBounds(1200, 300, 550, 490);
		this.setVisible(true);
	}
	
	
	public void window_close() {
		super.dispose();
	}
	
	
	
	public void send_Profile() {
		// ������ ������ ��ư Ŭ���� �ش� profile �� ä��â�� ���
		// Profile_Send Class ���� ���� profile �� Ŭ���� ����� ����â�� �߰� �����Ǿ� ����
		Profile_Send send_profile = new Profile_Send(chat_window);
		
		// �޼��� â�� ����� profile ������ ��� �ִ� JLabel component �� send_profile �� ����
		chat_window.tp_chat_box.insertComponent(send_profile);
		//����ó ������ UI ����.
		//tp_chat_box.updateUI();
		
		// ��ũ���� �׻� ���ϴܿ� ��ġ��Ű�� ���� �ۼ��� code
		//scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}
	
	
	
	
	public void showAllFriends() {
		int click_count = 0;
		
		// ������ �����ϴ� ��� ģ�� ����� �����Ѵ�.
		panel_center.removeAll();
		// ģ������ ����� ����ִ� names List �� ����� ��ȸ�� ���ο� ģ������� �����Ѵ�.
		for(int i=0; i<address_book.names.size(); i++) {
			// ģ�� ����� ǥ���ϴ� ����� JLabel �� names List �� �����ϴ� ģ���̸� ���� ��ŭ ����
			// �̶� JLabel ���� ģ�� 1���� �̸��� ���� ǥ�õǰԵ�
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
			//personBox ���� ���� ������ PersonBox ��ü ������ ��.
			panel_center.add(personBox);
		}
	}
}
