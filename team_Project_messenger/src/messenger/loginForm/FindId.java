package messenger.loginForm;

/*
 * ����(id)�� ã�� Ŭ����
 * */
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import messenger.Client_Thread;
import messenger.MainFrame;

public class FindId extends JPanel {
	// GUI���� ��� ����
	JPanel p_center;
	JLabel lb_name;
	JLabel lb_birth;
	JTextField txt_name;
	JButton btn_findId;
	JButton btn_cancel;
	Choice ch_year;
	Choice ch_month;
	Choice ch_day;
	JLabel lb_email;
	JTextField txt_email;
	JLabel lb_gol;
	Choice ch_mail;
	JTextField txt_input_email;
	String[] emailArray = { "����", "naver.com", "google.com", "nate.com", "daum.net", "�����Է�" };

	// DB���� ���� �������
	PreparedStatement pstmt;
	ResultSet rs;
	MainFrame mainFrame;

	// json���� �������
	String request_msg; // json���� ���� �޼���
	BufferedWriter json_writer; // json�� �Ǿ�� ��½�Ʈ��

	// ����ڰ� ���̵�ã�⸦ ���� ��, ã�� ���̵� ������ ����
	String user_id;

	// �Է��� ����� ����
	String user_name;
	String user_email;
	String user_birth;

	public FindId(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.setLayout(new BorderLayout());
		Dimension d = new Dimension(70, 25);

		p_center = new JPanel();
		lb_name = new JLabel("�̸�");
		lb_name.setPreferredSize(d);
		lb_birth = new JLabel("����");
		lb_birth.setPreferredSize(d);
		txt_name = new JTextField(16);
		ch_year = new Choice();
		ch_year.setPreferredSize(new Dimension(60, 30));
		ch_month = new Choice();
		ch_month.setPreferredSize(new Dimension(55, 30));
		ch_day = new Choice();
		ch_day.setPreferredSize(new Dimension(55, 30));
		lb_email = new JLabel("�̸���");
		lb_email.setPreferredSize(d);
		txt_email = new JTextField(6);
		lb_gol = new JLabel("@");
		ch_mail = new Choice();
		btn_findId = new JButton("�����ϱ�");
		btn_findId.setBackground(new Color(150, 112, 7));
		btn_cancel = new JButton("���ư���");
		btn_cancel.setBackground(new Color(150, 112, 7));

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

		// email ���ֱ�
		for (int i = 0; i < emailArray.length; i++) {
			ch_mail.add(emailArray[i].toString());
		}

		ch_mail.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				setEmail();
			}
		});

		p_center.add(lb_name);
		p_center.add(txt_name);
		p_center.add(lb_birth);
		p_center.add(ch_year);
		p_center.add(ch_month);
		p_center.add(ch_day);
		p_center.add(lb_email);
		p_center.add(txt_email);
		p_center.add(lb_gol);
		p_center.add(ch_mail);
		p_center.add(btn_findId, BorderLayout.SOUTH);
		p_center.add(btn_cancel);

		// ���Ϲ߼� ��ư -> ������ �̸�,�������,�̸����� ���� �����ߴ� ���̵� ��� ���� msg�� json����� ���� ��û
		btn_findId.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				requestFindId();
			}
		});

		// �α��� ȭ������ ���ư���
		btn_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txt_email.setText("");
				txt_name.setText("");
				
				// Login ȭ������ ���ư���
				mainFrame.changePage(0);
			}
		});

		p_center.setBackground(new Color(195, 237, 96));
		this.add(p_center);
		this.setPreferredSize(new Dimension(300, 140));
		this.setVisible(false);
	}

	// �̸����� '�����Է�'���� ������ ���
	public void setEmail() {
		if (ch_mail.getSelectedItem() == "�����Է�") {
			p_center.remove(ch_mail);
			txt_input_email = new JTextField();
			txt_input_email.setPreferredSize(new Dimension(100, 30));
			p_center.add(txt_input_email);
			p_center.updateUI(); // ������Ʈ ���� �߰��ϸ� ������ updateUI
		}
	}

	// ����ڰ� �Է��� �̸�,����,������ ȸ��DB�� �ִ��� ���������带 ���� ���� ��û�ϴ� �޼���
	public void requestFindId() {
		System.out.println("���̵�ã�� ��ư Ŭ��");
		user_name = txt_name.getText();
		user_birth = ch_year.getSelectedItem() + ch_month.getSelectedItem() + ch_day.getSelectedItem();
		user_email = txt_email.getText() + lb_gol.getText() + ch_mail.getSelectedItem();

		System.out.println("FindId : requestFindId() : user_name : " + user_name);
		System.out.println("FindId : requestFindId() : user_birth : " + user_birth);
		System.out.println("FindId : requestFindId() : user_email : " + user_email);
		
		//��Ʈ�ѷ��� ���� JSON ������ ��������!
		mainFrame.client_findAccount_controller.findID_request(user_name, user_birth, user_email);
	}
}
