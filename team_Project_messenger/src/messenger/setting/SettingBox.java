package messenger.setting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.border.Border;

public class SettingBox extends JLabel{
	Font font_style = new Font("���", Font.BOLD, 15);
	Color settingBox_color = new Color(240, 240, 240);
	
	public SettingBox(String name) {
		this.setOpaque(true);
		this.setFont(font_style);
		this.setPreferredSize(new Dimension(450, 80));
		this.setBackground(settingBox_color);
		this.setText(name);
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JLabel obj = (JLabel) e.getSource();
				getSetting(obj);
				//System.out.println(obj.getText());
				//System.out.println("===="+name+" Ŭ���߽��ϴ� ====");
			}
		});
	}
	
	
	public void getSetting(JLabel obj) {
		String select_box_name = obj.getText();
		
		if(select_box_name.equals("�������� ����")) {
			System.out.println("==== �������� ����â�� ���ϴ� ====");
			new Personal_info_edit(select_box_name);
		}else if(select_box_name.equals("ä�ù� ����")){
			System.out.println("==== ä�ù� ����â�� ���ϴ� ====");
			new Chatting_room_edit(select_box_name);
		}else if(select_box_name.equals("����Ʈ���� ����")) {
			System.out.println("==== ����Ʈ���� ����â�� ���ϴ� ====");
			new Software_info(select_box_name);
		}
	}
}
