package messenger.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import messenger.addressbook.Address_book;

public class ChattingBox extends JPanel{
	
	Address_book address_book;
	public String chatBox_name;
	
	JLabel la_text;
	JLabel la_time;
	JLabel user;
	JLabel dialogue;

	int click_count;
	boolean click_flag;

	public ChattingBox() {
		
		la_text=new JLabel();
		la_time=new JLabel();
		user=new JLabel();
		dialogue=new JLabel();
		
		
		la_text.setLayout(new BorderLayout());
		la_time.setLayout(new BorderLayout());
		
		
		//���� �� �ؽ�Ʈ�ʵ忡�� ä��â �̸�
		user.setText("���� ���� ���� ���� ����");
		//���� �Ʒ� �ؽ�Ʈ�ʵ忡�� ������ ä�ó���
		dialogue.setText("���̾��̾�");
		//������ �ؽ�Ʈ�ʵ忡�� �ð�
		la_time.setText("00:00:00");
		
		//�ȿ� ����� ������ font style �� ũ�⸦ ����
		user.setFont(new Font("����", Font.BOLD, 18));
		dialogue.setFont(new Font("����",Font.PLAIN, 13));
		
		//���� ����
		setBackground(new Color(255,255,255));

		// �г��� size �� �����Ѵ�.
		la_text.setPreferredSize(new Dimension(380,50));
		la_time.setPreferredSize(new Dimension(50,50));
		//�ؽ�Ʈ�ʵ��� ����� ����
		user.setPreferredSize(new Dimension(100,25));
		dialogue.setPreferredSize(new Dimension(100,25));
		
		//�� �г��� �׵θ� ���� ��������
		user.setBorder(BorderFactory.createEmptyBorder());
		dialogue.setBorder(BorderFactory.createEmptyBorder());
		
		//��ȭ�� �ϳ��� ��Ÿ���� �г��ϳ��� �׵θ����� �߰�
		this.setBorder(new TitledBorder(new LineBorder(Color.black,1)));
		this.setPreferredSize(new Dimension(450,60));
		
		//�ð��� �г� �Ʒ������� ����
		la_time.setVerticalAlignment(SwingConstants.BOTTOM);
		
		//�󺧿� �ؽ�Ʈ�ʵ� �߰�
		la_text.add(user,BorderLayout.NORTH);
		la_text.add(dialogue,BorderLayout.SOUTH);
		//la_number.add(time,BorderLayout.SOUTH);
		
		//�гο� �� �߰�
		add(la_text);
		add(la_time,BorderLayout.EAST);
		
		
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				click_count++;
				Object click_obj = e.getSource();
				ChattingBox clickObj = (ChattingBox)click_obj;
				if(e.getClickCount() == 2) {
					//�г� ����Ŭ���� ���� ä��â
					
				}
				//�ѹ� Ŭ���� ���ϰ� ǥ����
				if(click_count == 1) {
					click_flag = true;
					if(click_flag) {
						setBackground(Color.LIGHT_GRAY);
					}
				}else {//�ƴ϶�� �ٽ� ���󺹱�
					click_flag = false;
					click_count=0;
					setBackground(new Color(255,255,255));
				}
			}
			
		});
	}
}
