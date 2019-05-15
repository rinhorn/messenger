package messenger.setting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.border.Border;

public class SettingBox extends JLabel{
	Font font_style = new Font("고딕", Font.BOLD, 15);
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
				//System.out.println("===="+name+" 클릭했습니다 ====");
			}
		});
	}
	
	
	public void getSetting(JLabel obj) {
		String select_box_name = obj.getText();
		
		if(select_box_name.equals("개인정보 설정")) {
			System.out.println("==== 개인정보 설정창을 엽니다 ====");
			new Personal_info_edit(select_box_name);
		}else if(select_box_name.equals("채팅방 설정")){
			System.out.println("==== 채팅방 설정창을 엽니다 ====");
			new Chatting_room_edit(select_box_name);
		}else if(select_box_name.equals("소프트웨어 정보")) {
			System.out.println("==== 소프트웨어 정보창을 엽니다 ====");
			new Software_info(select_box_name);
		}
	}
}
