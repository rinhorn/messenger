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
		
		//패널 생성
		p_sample = new JPanel();
		
		//붙일 패널 크기및 레이아웃 설정
		p_sample.setLayout(new GridLayout(1, 6));
		p_sample.setPreferredSize(new Dimension(300,50));
		
		//사용될 이미지의 경로
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
		this.setVisible(false); //추후에 false로 변경 예정
		
	}
	public void sendImagePath(int index) {
		//iconList.get(index).getImage().getSource().
	}
}
