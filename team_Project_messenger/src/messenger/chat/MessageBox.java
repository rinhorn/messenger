package messenger.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

public class MessageBox extends JLabel{
	public MessageBox() {
		// MessageBox(Label) �� �⺻�� �����ϱ� ������ ������ó��
		this.setOpaque(true);
		
		// MessageBox �ȿ� ����� ������ font style �� ũ�⸦ ����
		this.setFont(new Font("����", Font.BOLD, 15));
		
		// MessageBox�� ������ ����
		this.setBackground(Color.LIGHT_GRAY);
		
		// MessageBox �� size �� �����Ѵ�.
		this.setPreferredSize(new Dimension(150000, 30));
		
		
		//System.out.println("�޼����ڽ� �¾���ϴ�!");
	}
}
