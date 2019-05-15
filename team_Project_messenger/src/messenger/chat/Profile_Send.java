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


// 프로필 보내기시 채팅창에 직접적으로 보내지는 사용자 프로필 Label 입니다.
public class Profile_Send extends JLabel {
	Chat_Window chat_window;
	Address_book address_book;
	String name;

	public Profile_Send(Chat_Window chat_window) {
		
		this.chat_window = chat_window;
		name = PersonBox.selectedBox.personBox_name;
		// personBox의 이름 처리를 클래스 내에서 해준다.
		this.setText(name);
		// personBox은 기본이 투명하기 때문에 불투명처리
		this.setOpaque(true);

		// personBox 안에 사용할 문구의 font style 과 크기를 지정
		this.setFont(new Font("휴먼매직체", Font.BOLD, 25));

		// personBox의 배경색을 지정
		this.setBackground(new Color(235, 235, 235));

		// personBox 의 size 를 지정한다.
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
					JOptionPane.showMessageDialog(chat_window, currentObj.getText()+"님의 프로필입니다. 더블클릭시 채팅창이 열립니다.");
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
