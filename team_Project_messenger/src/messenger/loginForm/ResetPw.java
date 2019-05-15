//��й�ȣ �缳��

package messenger.loginForm;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ColorConvertOp;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import messenger.MainFrame;

public class ResetPw extends JPanel implements Runnable {
	JPanel p_center;
	JPanel p_center_inner1;
	JPanel p_center_inner2;
	JPanel p_center_inner3;
	JPanel p_center_inner4;
	JPanel p_center_inner5;
	JPanel p_center_inner6;
	JPanel p_center_inner7;
	JLabel lb_id;
	JLabel lb_birth;
	JLabel lb_new_pw;
	JLabel lb_pw2;
	JLabel lb_pwCheck;
	public JLabel lb_timer;
	JTextField txt_certify;
	JTextField txt_id;
	public JTextField txt_pwCheck;
	public JTextField txt_new_pw;
	Choice ch_year;
	Choice ch_month;
	Choice ch_day;
	public JButton btn_change;
	public JButton btn_sendMail;
	public JButton btn_certify;
	JButton btn_cancel;

	MainFrame mainFrame;
	
	//������ȣ ����
	int keycode;
	
	int timer = 180;
	int min = 0;
	int sec = 0;
	
	//Timer Thread ����
	boolean thread_flag = true;
	
	public ResetPw(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		Dimension d = new Dimension(85, 25);

		p_center = new JPanel();
		p_center.setLayout(new GridLayout(7, 1));
		p_center_inner1 = new JPanel();
		p_center_inner2 = new JPanel();
		p_center_inner3 = new JPanel();
		p_center_inner4 = new JPanel();
		p_center_inner5 = new JPanel();
		p_center_inner6 = new JPanel();
		p_center_inner7 = new JPanel();
		lb_id = new JLabel("���̵�");
		lb_id.setPreferredSize(d);
		lb_birth = new JLabel("�������");
		lb_birth.setPreferredSize(new Dimension(60, 25));
		ch_year = new Choice();
		ch_year.setPreferredSize(new Dimension(60, 30));
		ch_month = new Choice();
		ch_month.setPreferredSize(new Dimension(55, 30));
		ch_day = new Choice();
		ch_day.setPreferredSize(new Dimension(55, 30));
		txt_certify = new JTextField(15);
		lb_timer = new JLabel("03:00");
		lb_pwCheck = new JLabel("��й�ȣ Ȯ��");
		lb_pwCheck.setPreferredSize(d);
		lb_new_pw = new JLabel("�� ��й�ȣ");
		lb_new_pw.setPreferredSize(d);
		txt_id = new JTextField(25);
		txt_new_pw = new JTextField("-",25);
		lb_pw2 = new JLabel("��й�ȣ Ȯ��");
		lb_pw2.setPreferredSize(d);
		txt_pwCheck = new JTextField(25);
		lb_pwCheck = new JLabel("");
		lb_pwCheck.setForeground(Color.RED);
		// txt_new_pw.isDisplayable(true);
		btn_change = new JButton("���� �ϱ�");
		btn_change.setBackground(new Color(150, 112, 7));
		btn_sendMail = new JButton("������ȣ �߼�");
		btn_sendMail.setBackground(new Color(150, 112, 7));
		btn_certify = new JButton("���� �ϱ�");
		btn_certify.setBackground(new Color(150, 112, 7));
		btn_cancel = new JButton("���� ����");
		btn_cancel.setBackground(new Color(150, 112, 7));
		Dimension p_center_inner_size = new Dimension(400, 20);
		p_center_inner1.add(lb_id);
		p_center_inner1.add(txt_id);
		p_center_inner2.add(lb_birth);
		p_center_inner2.add(ch_year);
		p_center_inner2.add(ch_month);
		p_center_inner2.add(ch_day);
		p_center_inner2.add(btn_sendMail);
		p_center_inner3.add(txt_certify);
		p_center_inner3.add(lb_timer);
		p_center_inner3.add(btn_certify);
		p_center_inner4.add(lb_new_pw);
		p_center_inner4.add(txt_new_pw);
		p_center_inner5.add(lb_pw2);
		p_center_inner5.add(txt_pwCheck);
		p_center_inner6.add(lb_pwCheck);
		p_center_inner7.add(btn_change);
		p_center_inner7.add(btn_cancel);

		p_center_inner1.setPreferredSize(p_center_inner_size);
		p_center_inner2.setPreferredSize(p_center_inner_size);
		p_center_inner3.setPreferredSize(p_center_inner_size);
		p_center_inner4.setPreferredSize(p_center_inner_size);
		p_center_inner5.setPreferredSize(p_center_inner_size);
		p_center_inner6.setPreferredSize(p_center_inner_size);
		p_center_inner7.setPreferredSize(p_center_inner_size);
		
		//��й�ȣ �缳�� ĭ ���
		txt_new_pw.setEditable(false);
		txt_pwCheck.setEditable(false);
		btn_change.setEnabled(false);
		
		//������ȣĭ �ʱ⿣ ��ŵ�
		btn_certify.setEnabled(false);

		// ������� choice �� �ֱ�
		for (int i = 2019; i >= 1930; i--) {
			ch_year.add("" + i + "");
		}
		ch_year.select((2019 - 1930) / 2);
		for (int i = 1; i <= 12; i++) {
			ch_month.add("" + i + "");
		}
		ch_month.select(6);
		for (int i = 1; i <= 31; i++) {
			ch_day.add("" + i + "");
		}
		ch_day.select(15);

		p_center.add(p_center_inner1);
		p_center.add(p_center_inner2);
		p_center.add(p_center_inner3);
		p_center.add(p_center_inner4);
		p_center.add(p_center_inner5);
		p_center.add(p_center_inner6);
		p_center.add(p_center_inner7);
		
		// ������ȣ �߼� ��ư ������
		btn_sendMail.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String user_id = txt_id.getText();
				String user_pw = txt_pwCheck.getText();
				String user_birth = ch_year.getSelectedItem() + ch_month.getSelectedItem() + ch_day.getSelectedItem();

				mainFrame.client_resetPW_controller.resetPW_request(user_id, user_birth);
			}
		});
		
		// ������ȣ �����ϱ�!
		btn_certify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(txt_certify.getText().equals("")) {
					JOptionPane.showMessageDialog(mainFrame, "������ȣ ĭ�� ����ֽ��ϴ�.");
				}else {
					keycode = Integer.parseInt(txt_certify.getText());
					mainFrame.client_resetPW_controller.certify_request(keycode);
				}
			}
		});
		
		// ��й�ȣ �����ϱ�
		btn_change.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = txt_id.getText();
				String pw_changed = txt_new_pw.getText();
				
				if(txt_new_pw.getText().equals(txt_pwCheck.getText())) {
					mainFrame.client_changePW_controller.changePW_request(id,pw_changed);
				}
			}
		});
		// ȭ�� ���ư���
		btn_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//��й�ȣ �缳�� â���� ���ư� �� ��� �� �������� ����!
				txt_id.setText("");
				txt_new_pw.setText("");
				txt_pwCheck.setText("");
				txt_certify.setText("");
				btn_sendMail.setEnabled(true);
				btn_certify.setEnabled(false);
				btn_change.setEnabled(false);
				txt_new_pw.setEditable(false);
				txt_pwCheck.setEditable(false);
				if(btn_certify.isEnabled()) {
					mainFrame.checkThread.thread_flag = false;
				}
				
				// Login ȭ������ ���ư���
				mainFrame.changePage(0);
			}
		});

		p_center_inner1.setBackground(new Color(195, 237, 96));
		p_center_inner2.setBackground(new Color(195, 237, 96));
		p_center_inner3.setBackground(new Color(195, 237, 96));
		p_center_inner4.setBackground(new Color(195, 237, 96));
		p_center_inner5.setBackground(new Color(195, 237, 96));
		p_center_inner6.setBackground(new Color(195, 237, 96));
		p_center_inner7.setBackground(new Color(195, 237, 96));

		p_center.setPreferredSize(new Dimension(400, 300));
		p_center.setBackground(new Color(195, 237, 96));
		this.add(p_center);
		this.setBackground(new Color(195, 237, 96));
		this.setPreferredSize(new Dimension(400, 300));
		this.setVisible(false);
	}
	
	// ������ȣ�� �߼��ϸ� 3��¥�� Ÿ�̸� ����
	public void run() {
		timer = 180;
		min = 0;
		sec = 0;
		while (thread_flag) {
			try {
				timer--;
				min = timer / 60;
				sec = timer - min * 60;
				if (sec >= 10) {
					lb_timer.setText("0" + min + ":" + sec);
				} else if (sec < 10) {
					lb_timer.setText("0" + min + ":0" + sec);
				}
				Thread.sleep(1000);
				if (min == 0 && sec == 0) {
					JOptionPane.showMessageDialog(this, "�ð��ʰ��� ���� ��������!");
					thread_flag = false;
					break;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//Thread_flag ����
	public void setThread_flag(boolean thread_flag) {
		this.thread_flag = thread_flag;
	}
	
	//Timer Setter
	public void setTimer(int timer) {
		this.timer = timer;
	}
	
	//Timer Getter
	public int getTimer() {
		return timer;
	}

	
}
