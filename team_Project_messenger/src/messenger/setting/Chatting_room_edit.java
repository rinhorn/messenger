package messenger.setting;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Chatting_room_edit extends JFrame{
	JPanel p_wrapper;
	
	public Chatting_room_edit(String name) {
		p_wrapper = new JPanel();
		p_wrapper.setBackground(Color.BLUE);
		
		this.add(p_wrapper);
		this.setSize(400, 500);
		this.setTitle(name);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}

