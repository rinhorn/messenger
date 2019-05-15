package messenger.icon;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;


// Main 화면 상단 메뉴 아이콘을 설정하는 Class 입니다.
public class MenuIcon implements Icon{
	
	Image img;
	ImageIcon img_icon;
	String path = "res/";
	String img_name;
	int x_loaction;
	int y_loaction;
	int width;
	int height;
	
	// 생성자에서 아이콘 설정에 필요한 값들을 설정합니다.
	public MenuIcon(String img_name,  int x_loaction, int y_loaction, int width, int height) {
		this.img_name = img_name;
		this.x_loaction = x_loaction;
		this.y_loaction = y_loaction;
		this.width = width;
		this.height = height;
	}
	
	// 아래 코드들은 아이콘의 gui 적 요소들을 setting 하는 method 들입니다.
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		img_icon = new ImageIcon(path+img_name);						// 이미지 아이콘으로 경로의 이미지를 불러옴
		img = img_icon.getImage();													// 이미지 아이콘에서 이미지를 뽑아냄, 그리고 이미지 형 변수에 담아놓음	
		img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);		// 뽑아낸 이미지의 크기를 설정한다.
		g.drawImage(img, x_loaction, y_loaction, width, height, null);	// 뽑아낸 이미지를 다시 그림
	}

	
	// 아래 코드들은 아이콘이 차지할 영역을 설정하는 method 입니다.
	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return 100;
	}

	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return 100;
	}

}
