package messenger.loginForm;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
//업그레이드 버튼
import javax.swing.JButton;

public class IconButton extends JButton {
	int id;
	String path;
	URL url;
	ImageIcon icon;
	Image newImg;
	int width;
	int height;
	
	public IconButton(int id, String path, int width, int height) {
		this.id = id;
		this.path = path;
		this.width = width;
		this.height = height;
		url = getClass().getClassLoader().getResource(path);
		icon = new ImageIcon(url);
		newImg = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
		this.setIcon(new ImageIcon(newImg));
	}
}
