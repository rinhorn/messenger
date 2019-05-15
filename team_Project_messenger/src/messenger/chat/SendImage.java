package messenger.chat;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

// 채팅창에서 이미지 전송 버튼을 클릭시 나타나는
public class SendImage extends JFrame{
	Chat_Window chat_window;
	String jfileChooser_default_path = "C:/Users/ParkHyeonho/Desktop/IT/KGITBANK_carrier/JAVA/JAVA/project_1812/res";
	protected static final Component ImageButton = null;
    String path;
    ImageIcon icon;
    Image img;
    File file;
    JFileChooser chooser;
    
    // chat_window instance 를 매개변수로 받아와 chat_window 요소에 이미지 요소의 추가에 대비한다.
    public void sendImage(Chat_Window chat_window) {
    	this.chat_window = chat_window;
    	
    	// JFileChooser 를 선언하여 전송할 ImageFile 을 선택할 수 있는 Diallog 창을 띄운다.
		JFileChooser chooser = new JFileChooser(jfileChooser_default_path);
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png");
		chooser.addChoosableFileFilter(filter);
		int result = chooser.showOpenDialog(ImageButton);
		
		if(result == JFileChooser.APPROVE_OPTION){
			file = chooser.getSelectedFile();
			path = file.getAbsolutePath();
			JLabel label = new JLabel();
			label.setPreferredSize(new Dimension(8000, 200));
			label.setIcon(imageSize(path));
			chat_window.tp_chat_box.insertComponent(label);
		}
		else if(result == JFileChooser.CANCEL_OPTION){
			super.dispose();		
		}
	}		
    
	public ImageIcon imageSize(String ImagePath){
        icon = new ImageIcon(ImagePath);
        img = icon.getImage();
        Image newImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImg);
        return image;
    }
}
