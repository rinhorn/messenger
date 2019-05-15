package messenger.chattingRoom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class ChattingRoom extends JPanel{
	
	public static final int PANEL_BOTTOM_WIDTH = 450;
	int PANEL_BOTTOM_HEIGHT = 58;
	
	JPanel p_top;
	JPanel p_top_inner_top;
	JPanel p_title;
	JPanel p_bottom;

	
	JTextField tf_search;
	JButton bt_search;
	
	JLabel la_title;
	
	JScrollPane scroll;
	
	
	public ChattingRoom() {
		this.setLayout(new BorderLayout());
		Font bt_style = new Font("���", Font.BOLD, 15);
		
		p_top = new JPanel();
			p_top.setBackground(new Color(195,237,96));
			p_top.setPreferredSize(new Dimension(490, 90));
			
			// ���� â�� title �� ǥ���� label �� ������ �г�
			p_title = new JPanel();
				// p_title �ȿ� label ��ҿ� ���� ����
				la_title = new JLabel("ä�� ���");
				la_title.setFont(new Font("���", Font.BOLD, 15));
				//la_title.setForeground(Color.WHITE);
				
				// p_title �� ���� ����
				p_title.setLayout(new BorderLayout());
				p_title.setBackground(new Color(195,237,96));
				p_title.add(la_title, BorderLayout.WEST);
				
				p_title.add(la_title);
			
			// �˻��Է�â�� �˻� ��ư�� �����ϴ� panel ����
			p_top_inner_top = new JPanel();
				p_top_inner_top.setBackground(new Color(195,237,96));
				
				tf_search = new JTextField();
					tf_search.setPreferredSize(new Dimension(300, 30));
				bt_search = new JButton("Search");
					bt_search.setFont(bt_style);
				
				p_top_inner_top.add(tf_search);
				p_top_inner_top.add(bt_search);
		
		// ä�ù� ����� ǥ���� �г�
		p_bottom = new JPanel();
			p_bottom.setBackground(Color.WHITE);
			p_bottom.setPreferredSize(new Dimension(PANEL_BOTTOM_WIDTH, PANEL_BOTTOM_HEIGHT * 50));
			
			scroll = new JScrollPane(p_bottom);
				scroll.setPreferredSize(new Dimension(490, 475));
				scroll.getVerticalScrollBar().setUnitIncrement(20);
				
				
				
		// ��ư ���� ���� ����
		bt_search.setBackground(new Color(150,112,7));
		
	
		
		p_top.setLayout(new BorderLayout());
		p_top.add(p_top_inner_top, BorderLayout.NORTH);
		p_top.add(p_title);
		
		this.add(p_top, BorderLayout.NORTH);
		this.add(p_bottom);
		this.setPreferredSize(new Dimension(490, 570));
		//this.setBackground(Color.YELLOW);
		this.setVisible(true);
	}
}
