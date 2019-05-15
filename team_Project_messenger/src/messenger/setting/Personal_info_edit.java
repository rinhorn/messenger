package messenger.setting;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Personal_info_edit extends JFrame{
	JPanel p_wrapper;
	
	public Personal_info_edit(String name) {
		p_wrapper = new JPanel();
		p_wrapper.setBackground(Color.GREEN);
		
		this.add(p_wrapper);
		this.setSize(400, 500);
		this.setTitle(name);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
