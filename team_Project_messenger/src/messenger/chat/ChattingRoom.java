package messenger.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import messenger.MainFrame;
import messenger.addressbook.IconLabel;

public class ChattingRoom extends JPanel{
	
	public static final int PANEL_BOTTOM_WIDTH = 450;
	int PANEL_BOTTOM_HEIGHT = 58;
	
	ChattingBox roomBox;
	
	JPanel p_top;
	JPanel p_top_inner_top;
	JPanel p_title;
	JPanel p_bottom;

	JPanel p_bt;
	JButton bt_chat_delete;
	
	JTextField tf_search;
	JButton bt_search;
	
	JLabel la_title;
	
	IconLabel lb_search;
	
	JScrollPane scroll;
	
	MainFrame mainFrame;
	
	//라벨 사이즈
		public static final int LB_WIDTH=40;
		public static final int LB_HEIGHT=40;
	
	public ChattingRoom(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());
		Font bt_style = new Font("고딕", Font.BOLD, 15);
		
		p_top = new JPanel();
			p_top.setBackground(new Color(195,237,96));
			p_top.setPreferredSize(new Dimension(490, 90));
			
			// 현재 창의 title 을 표시할 label 을 붙히는 패널
			p_title = new JPanel();
				// p_title 안에 label 요소에 대한 설정
				la_title = new JLabel("채팅 목록");
				la_title.setFont(new Font("고딕", Font.BOLD, 15));
				//la_title.setForeground(Color.WHITE);
				
				// p_title 에 관한 설정
				p_title.setLayout(new BorderLayout());
				p_title.setBackground(new Color(195,237,96));
				p_title.add(la_title, BorderLayout.WEST);
				
				p_title.add(la_title);
				
				//채팅 목록 삭제 버튼을 넣기 위한 패널
				p_bt=new JPanel();
				p_bt.setBackground(new Color(195,237,96));
				
				p_title.add(p_bt, BorderLayout.EAST);
				
//				//채팅 목록 삭제 버튼
//				bt_chat_delete=new JButton("채팅 삭제");
//				bt_chat_delete.setBackground(new Color(150,112,7));
//				bt_chat_delete.setFont(bt_style);
//			
//				p_bt.add(bt_chat_delete);
				
//				bt_chat_delete.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						
//					}
//				});
			
			// 검색입력창과 검색 버튼을 포함하는 panel 설정
			p_top_inner_top = new JPanel();
				p_top_inner_top.setBackground(new Color(195,237,96));
				
				tf_search = new JTextField();
					tf_search.setPreferredSize(new Dimension(300, 30));
				lb_search = new IconLabel(mainFrame.getUser_pk(), "search.png", LB_WIDTH, LB_HEIGHT);
				lb_search.setFont(bt_style);
					
				p_top_inner_top.add(tf_search);
				p_top_inner_top.add(lb_search);
		
		// 채팅방 목록을 표현할 패널
		p_bottom = new JPanel();
			p_bottom.setBackground(Color.WHITE);
			p_bottom.setPreferredSize(new Dimension(PANEL_BOTTOM_WIDTH, PANEL_BOTTOM_HEIGHT * 50));
			
			
			scroll = new JScrollPane(p_bottom);
				scroll.setPreferredSize(new Dimension(490, 475));
				scroll.getVerticalScrollBar().setUnitIncrement(20);
				
				
				
				
		// 버튼 색상에 관한 설정
		//lb_search.setBackground(new Color(150,112,7));
		
		
		
		p_top.setLayout(new BorderLayout());
		p_top.add(p_top_inner_top, BorderLayout.NORTH);
		p_top.add(p_title);
		
		
		this.add(p_top, BorderLayout.NORTH);
		this.add(p_bottom);
		
		//ChatBox를 부착
		roomBox=new ChattingBox();
		p_bottom.add(roomBox);
		
		this.setPreferredSize(new Dimension(490, 570));
		//this.setBackground(Color.YELLOW);
		this.setVisible(true);
	}
}
