package messenger.setting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Setting extends JPanel{
	JScrollPane scroll;
	
	String[] setting_list_name = {"�������� ����", "ä�ù� ����", "����Ʈ���� ����"};
	List<Object> setting_list = new ArrayList<Object>();
	
	public Setting() {
		scroll = new JScrollPane(this);
		
		for(int i=0; i<setting_list_name.length; i++) {
			setting_list.add(new SettingBox(setting_list_name[i]));
			this.add((JLabel) setting_list.get(i));
		}
		
		this.updateUI();
		this.setPreferredSize(new Dimension(490, 570));
		this.setBackground(Color.WHITE);
		this.setVisible(true);
	}
}
