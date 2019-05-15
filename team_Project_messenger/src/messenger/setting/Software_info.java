package messenger.setting;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Software_info extends JFrame{
	JPanel p_wrapper;
	
	public Software_info(String name) {
		p_wrapper = new JPanel();
		p_wrapper.setBackground(Color.MAGENTA);
		
		this.add(p_wrapper);
		this.setSize(400, 500);
		this.setTitle(name);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
