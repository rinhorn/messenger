package messenger.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import messenger.addressbook.Address_book;
import messenger.addressbook.PersonBox;


// ������ ������� ä��â�� ���������� �������� ����� ������ Label �Դϴ�.
public class Profile_Send extends JLabel {
	Chat_Window chat_window;
	Address_book address_book;
	String name;

	public Profile_Send(Chat_Window chat_window) {
		
		this.chat_window = chat_window;
		name = PersonBox.selectedBox.personBox_name;
		// personBox�� �̸� ó���� Ŭ���� ������ ���ش�.
		this.setText(name);
		// personBox�� �⺻�� �����ϱ� ������ ������ó��
		this.setOpaque(true);

		// personBox �ȿ� ����� ������ font style �� ũ�⸦ ����
		this.setFont(new Font("�޸ո���ü", Font.BOLD, 25));

		// personBox�� ������ ����
		this.setBackground(new Color(235, 235, 235));

		// personBox �� size �� �����Ѵ�.
		this.setPreferredSize(new Dimension(3000, 100));
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Object click_obj = e.getSource();
				Profile_Send currentObj = (Profile_Send)click_obj;
				Profile_Info profile_info = new Profile_Info(address_book, chat_window, currentObj.name);
				
				/*
				if (e.getClickCount() % 2 == 0) {
					Chat_Window addchat = new Chat_Window();
					// chat_window.setTitle(address_book_window.personBox.getText());
					// System.out.println(e);
					// System.out.println(e.getClickCount());
				} else if (e.getClickCount() % 2 == 1) {
					JOptionPane.showMessageDialog(chat_window, currentObj.getText()+"���� �������Դϴ�. ����Ŭ���� ä��â�� �����ϴ�.");
				}*/
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(Color.CYAN);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(new Color(235, 235, 235));
			}
		});

	}
}
