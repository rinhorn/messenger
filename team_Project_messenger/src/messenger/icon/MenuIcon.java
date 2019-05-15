package messenger.icon;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;


// Main ȭ�� ��� �޴� �������� �����ϴ� Class �Դϴ�.
public class MenuIcon implements Icon{
	
	Image img;
	ImageIcon img_icon;
	String path = "res/";
	String img_name;
	int x_loaction;
	int y_loaction;
	int width;
	int height;
	
	// �����ڿ��� ������ ������ �ʿ��� ������ �����մϴ�.
	public MenuIcon(String img_name,  int x_loaction, int y_loaction, int width, int height) {
		this.img_name = img_name;
		this.x_loaction = x_loaction;
		this.y_loaction = y_loaction;
		this.width = width;
		this.height = height;
	}
	
	// �Ʒ� �ڵ���� �������� gui �� ��ҵ��� setting �ϴ� method ���Դϴ�.
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		img_icon = new ImageIcon(path+img_name);						// �̹��� ���������� ����� �̹����� �ҷ���
		img = img_icon.getImage();													// �̹��� �����ܿ��� �̹����� �̾Ƴ�, �׸��� �̹��� �� ������ ��Ƴ���	
		img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);		// �̾Ƴ� �̹����� ũ�⸦ �����Ѵ�.
		g.drawImage(img, x_loaction, y_loaction, width, height, null);	// �̾Ƴ� �̹����� �ٽ� �׸�
	}

	
	// �Ʒ� �ڵ���� �������� ������ ������ �����ϴ� method �Դϴ�.
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
