package messenger.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

public class MessageBox extends JLabel{
	public MessageBox() {
		// MessageBox(Label) 은 기본이 투명하기 때문에 불투명처리
		this.setOpaque(true);
		
		// MessageBox 안에 사용할 문구의 font style 과 크기를 지정
		this.setFont(new Font("굴림", Font.BOLD, 15));
		
		// MessageBox의 배경색을 지정
		this.setBackground(Color.LIGHT_GRAY);
		
		// MessageBox 의 size 를 지정한다.
		this.setPreferredSize(new Dimension(150000, 30));
		
		
		//System.out.println("메세지박스 태어났습니다!");
	}
}
