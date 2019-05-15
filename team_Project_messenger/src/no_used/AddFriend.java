/*package no_used;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import messenger.Address_book;

// 친구추가 버튼 클릭시 나타는 창에 관한 Class 입니다 (미사용)
public class AddFriend extends JFrame{
	JPanel p_center, p_south;
	JLabel la_title;
	JButton bt_add, bt_cancel;
	JTextField tf_friend_name;
	boolean flag = true;
	
	public AddFriend() {
		la_title = new JLabel("친구추가");
			la_title.setFont(new Font("휴먼매직", Font.BOLD, 25));
		p_center = new JPanel();
		p_south = new JPanel();
		bt_add = new JButton("등록");
			bt_add.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addFriends();
				}
			});
			
		tf_friend_name = new JTextField(20);
			tf_friend_name.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					int key = e.getKeyCode();
					if(key == KeyEvent.VK_ENTER) {
						addFriends();
					}
				}
			});
		
		p_center.add(la_title, BorderLayout.NORTH);
		p_center.add(tf_friend_name, BorderLayout.CENTER);
		p_south.add(bt_add);
	
		
		this.add(p_center, BorderLayout.CENTER);
		this.add(p_south, BorderLayout.SOUTH);
		this.setBounds(450, 400, 320, 150);
		this.setResizable(false);
		this.setTitle("친구 추가");
		this.setVisible(flag);
	}
	
	
	// 친구를 추가 동작을 하는 method 입니다.
	public void addFriends() {
		// 친구 이름 입력창에서 입력한 값을 가져와 변수에 저장한다.
		String friends_name = tf_friend_name.getText();
		
		// Address_book Instance 에 접근해 names List 에 추가하고자 하는 친구이름을 추가한다.
		Address_book.address_window.names.add(friends_name);
		
		System.out.println("==== addFriends() ====");
		
		// 친구목록 갱신을 위해  Address_book Instance 를 통해 panel_bottom 을 재정의하는 method 를 호출한다.
		Address_book.address_window.remake_BottomPanel();
		
		// method 수행시 현재 띄워진 친구추가 창을 닫습니다.
		super.dispose();
	}
}
*/