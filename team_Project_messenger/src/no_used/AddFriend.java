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

// ģ���߰� ��ư Ŭ���� ��Ÿ�� â�� ���� Class �Դϴ� (�̻��)
public class AddFriend extends JFrame{
	JPanel p_center, p_south;
	JLabel la_title;
	JButton bt_add, bt_cancel;
	JTextField tf_friend_name;
	boolean flag = true;
	
	public AddFriend() {
		la_title = new JLabel("ģ���߰�");
			la_title.setFont(new Font("�޸ո���", Font.BOLD, 25));
		p_center = new JPanel();
		p_south = new JPanel();
		bt_add = new JButton("���");
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
		this.setTitle("ģ�� �߰�");
		this.setVisible(flag);
	}
	
	
	// ģ���� �߰� ������ �ϴ� method �Դϴ�.
	public void addFriends() {
		// ģ�� �̸� �Է�â���� �Է��� ���� ������ ������ �����Ѵ�.
		String friends_name = tf_friend_name.getText();
		
		// Address_book Instance �� ������ names List �� �߰��ϰ��� �ϴ� ģ���̸��� �߰��Ѵ�.
		Address_book.address_window.names.add(friends_name);
		
		System.out.println("==== addFriends() ====");
		
		// ģ����� ������ ����  Address_book Instance �� ���� panel_bottom �� �������ϴ� method �� ȣ���Ѵ�.
		Address_book.address_window.remake_BottomPanel();
		
		// method ����� ���� ����� ģ���߰� â�� �ݽ��ϴ�.
		super.dispose();
	}
}
*/