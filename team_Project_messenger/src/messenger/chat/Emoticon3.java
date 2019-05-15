package messenger.chat;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Emoticon3 extends JPanel implements ActionListener{
	JPanel p_emoticon;
	URL img_path1, img_path2, img_path3;
	JButton bt1, bt2, bt3;
	ImageIcon icon1, icon2, icon3;
	ImageIcon newIcon1, newIcon2, newIcon3;
	JLabel lab1, lab2, lab3;
	Image img1, img2, img3;
	ArrayList iconList = new ArrayList();
	
	// 이모티콘 버튼 크기를 나타내는 변수
	int iconWidth=100;
	int iconHeight=100;
	
	Chat_Window chat_window;

	public Emoticon3(Chat_Window chat_window) {
		this.chat_window = chat_window;
		
		//p_emoticon=new JPanel();
		
		
		// 사용될 이미지의 경로
		img_path1=getClass().getClassLoader().getResource("p0.jpg");
		img_path2=getClass().getClassLoader().getResource("p1.jpg");
		img_path3=getClass().getClassLoader().getResource("p2.jpg");
		
		bt1=new JButton();
		bt2=new JButton();
		bt3=new JButton();
		icon1=new ImageIcon(img_path1);
		icon2=new ImageIcon(img_path2);
		icon3=new ImageIcon(img_path3);
		
		this.setPreferredSize(new Dimension(400,100));
		this.setBackground(Color.PINK);
		this.setLayout(new GridLayout(1, 3));
		

		chat_window.chat_bottom_panel.add(this);
		
		// 버튼 아이콘 이미지 사이즈 조절하기
		img1=icon1.getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_DEFAULT);
		newIcon1=new ImageIcon(img1);
		img2=icon2.getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_DEFAULT);
		newIcon2=new ImageIcon(img2);
		img3=icon3.getImage().getScaledInstance(iconWidth,iconHeight,Image.SCALE_DEFAULT);
		newIcon3=new ImageIcon(img3);
		
		// 사이즈 조절한 아이콘 이미지 배열에 넣어주기
		iconList.add(newIcon1);
		iconList.add(newIcon2);
		iconList.add(newIcon3);
		/*
		ImageIcon[] images = {newIcon1,newIcon2,newIcon3};
		iconList.setListData(images); 
		*/
		
		// 	버튼 배경색 흰색
		bt1.setBackground(Color.WHITE);
		bt2.setBackground(Color.WHITE);
		bt3.setBackground(Color.WHITE);
		// 버튼 테두리 없애기
		bt1.setBorderPainted(false);
		bt2.setBorderPainted(false);
		bt3.setBorderPainted(false);
		// 사이즈 조절한 이미지 버튼 아이콘에 세팅하기
		bt1.setIcon(newIcon1);
		bt2.setIcon(newIcon2);
		bt3.setIcon(newIcon3);
		
		//이모티콘 목록 창에 이모티콘 버튼 붙힘
		this.add(bt1);
		this.add(bt2);
		this.add(bt3);
		
		
		// 버튼이랑 리스너 연결하기
		bt1.addActionListener(this);
		bt2.addActionListener(this);
		bt3.addActionListener(this);
		
		System.out.println("이모티콘 패널 생성");
		chat_window.setSize(chat_window.WIDTH, chat_window.HEIGHT+100);
		chat_window.chat_bottom_panel.setLayout(new FlowLayout());
		chat_window.chat_bottom_panel.add(this);
	}

	public void clickedEffect(int index) {
		ImageIcon currentImage = (ImageIcon)iconList.get(index);
		JLabel lab = new JLabel(currentImage);
		lab.setPreferredSize(new Dimension(8000,100));
		//이모티콘을 선택했을 경우 이모티콘 카운트 증가
		chat_window.content_type = "2";
		chat_window.content = "이모티콘(추후변경예정)";
		//System.out.println("chat_window :  currentImage.getImage().getClass().getName() : "+  currentImage.getImage().getClass().getName());
		
		chat_window.request_dialogue();
		
		//send메서드 호출해야될 곳!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		
		//선택한 이모티콘을 GUI로 표현
		chat_window.tp_chat_box.insertComponent(lab);
		chat_window.scroll.getVerticalScrollBar().setValue(chat_window.scroll.getVerticalScrollBar().getMaximum());
		
		
	}
	
	 public void actionPerformed(ActionEvent e) { 
		 Object obj = e.getSource();
		 if(obj==bt1) { 
			 clickedEffect(0); 
		 }else if(obj==bt2) { 
			 clickedEffect(1); 
		 }else if(obj==bt3) { 
			 clickedEffect(2); 
		 }

	 }
}
