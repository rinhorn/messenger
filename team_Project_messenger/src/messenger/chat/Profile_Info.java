package messenger.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import messenger.addressbook.Address_book;
import messenger.addressbook.PersonBox;


// ����� ������ �󼼺��� �˾�â�Դϴ�.
public class Profile_Info extends JFrame implements ActionListener {
	JPanel p_north, p_south;
	JButton bt_send, bt_cancel;
	JLabel name;
	Address_book address_book;
	Chat_Window chat_window;

	public Profile_Info(Address_book address_book, Chat_Window chat_window, String profilename) {
		this.address_book = address_book;
		this.chat_window = chat_window;

		p_north = new JPanel();
		p_south = new JPanel();
		bt_send = new JButton("��ȭ������");
		bt_cancel = new JButton("���");
		name = new JLabel(profilename);

		p_north.setPreferredSize(new Dimension(270, 240));
		p_north.setBackground(Color.YELLOW);
		name.setFont(new Font("�޸ո���ü", Font.BOLD, 25));
		name.setForeground(Color.BLACK);
		p_south.setPreferredSize(new Dimension(270, 160));
		p_south.setBackground(Color.WHITE);

		// ���� �г� �߰�
		this.add(p_north, BorderLayout.NORTH);
		p_north.add(this.name);
		// �Ʒ��� �г� �߰�
		this.add(p_south);
		p_south.add(bt_send);
		p_south.add(bt_cancel);
		
		//��ư�� �̺�Ʈ ������ ����
		bt_send.addActionListener(this);
		bt_cancel.addActionListener(this);

		setLocationRelativeTo(chat_window);
		setSize(270, 400);
		setVisible(true);
	}
	//��ư�� �̺�Ʈ ����
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj == bt_send) {
			Chat_Window chat = new Chat_Window(address_book, PersonBox.selectedBox);
			System.out.println("��ȭ �����ž�?");
		}
		if(obj == bt_cancel) {
			System.out.println("���");
			super.dispose();
		}
	}
}
