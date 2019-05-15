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
		
		
		//왼쪽 위 텍스트필드에는 채탕창 이름
		user.setText("누구 누구 누구 누구 누구");
		//왼쪽 아래 텍스트필드에는 마지막 채팅내용
		dialogue.setText("야이야이야");
		//오른쪽 텍스트필드에는 시간
		la_time.setText("00:00:00");
		
		//안에 사용할 문구의 font style 과 크기를 지정
		user.setFont(new Font("돋움", Font.BOLD, 18));
		dialogue.setFont(new Font("돋움",Font.PLAIN, 13));
		
		//배경색 지정
		setBackground(new Color(255,255,255));

		// 패널의 size 를 지정한다.
		la_text.setPreferredSize(new Dimension(380,50));
		la_time.setPreferredSize(new Dimension(50,50));
		//텍스트필드의 사이즈를 지정
		user.setPreferredSize(new Dimension(100,25));
		dialogue.setPreferredSize(new Dimension(100,25));
		
		//각 패널의 테두리 선을 투명으로
		user.setBorder(BorderFactory.createEmptyBorder());
		dialogue.setBorder(BorderFactory.createEmptyBorder());
		
		//대화방 하나를 나타내는 패널하나의 테두리선을 추가
		this.setBorder(new TitledBorder(new LineBorder(Color.black,1)));
		this.setPreferredSize(new Dimension(450,60));
		
		//시간을 패널 아래쪽으로 정렬
		la_time.setVerticalAlignment(SwingConstants.BOTTOM);
		
		//라벨에 텍스트필드 추가
		la_text.add(user,BorderLayout.NORTH);
		la_text.add(dialogue,BorderLayout.SOUTH);
		//la_number.add(time,BorderLayout.SOUTH);
		
		//패널에 라벨 추가
		add(la_text);
		add(la_time,BorderLayout.EAST);
		
		
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				click_count++;
				Object click_obj = e.getSource();
				ChattingBox clickObj = (ChattingBox)click_obj;
				if(e.getClickCount() == 2) {
					//패널 더블클릭시 나올 채팅창
					
				}
				//한번 클릭시 진하게 표시함
				if(click_count == 1) {
					click_flag = true;
					if(click_flag) {
						setBackground(Color.LIGHT_GRAY);
					}
				}else {//아니라면 다시 원상복귀
					click_flag = false;
					click_count=0;
					setBackground(new Color(255,255,255));
				}
			}
			
		});
	}
}
