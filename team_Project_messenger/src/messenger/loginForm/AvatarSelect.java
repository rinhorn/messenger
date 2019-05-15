package messenger.loginForm;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class AvatarSelect extends JPanel{
	JPanel p_sample;
	Register register;
	String selected_path;
	
	IconButton[] btn = new IconButton[12];
	
	public AvatarSelect() {
		
		//�г� ����
		p_sample = new JPanel();
		
		//���� �г� ũ��� ���̾ƿ� ����
		p_sample.setLayout(new GridLayout(1, 6));
		p_sample.setPreferredSize(new Dimension(300,50));
		
		//���� �̹����� ���
		for(int i=0; i<btn.length; i++) {
			btn[i] = new IconButton(i, "pf"+i+".png", 50, 50);
			p_sample.add(btn[i]);
			btn[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IconButton obj = (IconButton)e.getSource();
					System.out.println(obj.id+1);
					register.img = obj.newImg;
					register.can_profile.repaint();
					selected_path = obj.path;
				}
			});
		}

		
		this.add(p_sample);
		this.setSize(600,50);
		this.setVisible(false); //���Ŀ� false�� ���� ����
		
	}
	public void sendImagePath(int index) {
		//iconList.get(index).getImage().getSource().
	}
}
