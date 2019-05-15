package messenger.addressbook;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class IconLabel extends JLabel{
	String user_pk;
	String path;
	URL url;
	ImageIcon icon;
	Image newImg;
	int width;
	int height;

	public IconLabel(String user_pk, String path, int width, int height) {
		this.user_pk = user_pk;
		this.path = path;
		this.width = width;
		this.height = height;
		url = getClass().getClassLoader().getResource(path);
		icon = new ImageIcon(url);
		newImg = icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT);
		this.setIcon(new ImageIcon(newImg));
		this.setPreferredSize(new Dimension(60,height));
	}
}
