package messenger.loginForm;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import messenger.Client_Thread;
import messenger.MainFrame;
import messenger.controller.Client_checkId_controller;
import messenger.controller.Client_regist_controller;

public class Register extends JPanel{
	JPanel p_north;
	JPanel p_center;
	JPanel p_south;
	JLabel lb_title;
	JLabel lb_id;
	JLabel lb_pw;
	JLabel lb_pw_check;
	JLabel lb_id_check;
	JLabel lb_birth;
	JLabel lb_name;
	JTextField txt_id;
	JPasswordField txt_pw;
	JPasswordField txt_pw_check;
	Choice ch_year;
	Choice ch_month;
	Choice ch_day;
	JLabel lb_email;
	JTextField txt_email;
	JLabel lb_gol;
	Choice ch_mail;
	JTextField txt_name;
	JTextField txt_input_email;
	JLabel lb_profile;
	JButton btn_addProfile;
	JButton btn_regist;	//���ԿϷ��ư
	JButton btn_cancel;

	Font font;
	boolean idChecked = false; //���̵� �ߺ�üũ�� �ߴ��� ���ߴ��� ����
	LoginForm move_loginForm;
	JFileChooser chooser;
	String profile; //Member DB �����ʻ��� �ʵ��
	String profileName;
	String[] emailArray = {"����","naver.com","google.com","nate.com","daum.net","�����Է�"};
	Image img;
	Canvas can_profile = new Canvas() {
		@Override
		public void paint(Graphics g) {
			System.out.println("paint�޼���");
			g.setColor(new Color(255, 216, 216));
			g.fillRect(0, 0, 100, 100);
			g.drawImage(img, 0, 0, 100, 100, this);
			
		}
	};
	
	//json���� �������
	String request_msg; //json���� ���� �޼���
	BufferedWriter json_writer; //json�� �Ǿ�� ��½�Ʈ��
	
	//��Ű��� �������
	Socket client;
	Client_Thread client_Thread;
	
	//������ �Է��ϴ� textFiled���� ���� ����
	String user_id;
	String user_name;
	String user_pw;
	String user_birth;
	String user_email;
	String user_profile;
	
	AvatarSelect avatarSelect;//�ƹ�Ÿ�� �� �г� 
	MainFrame mainFrame; //�θ� ��ü
	
	String checkId_flag="true"; //���̵� �ߺ�üũ ����� ���� ����
	int checkId_count;	// ���̵� �ߺ� üũ ���� ���θ� �޴� ���� (�ߴٸ� 1, ���ߴٸ� 0)
	String regist_flag; //ȸ�� ���� ����� ���� ����
	int ok_id_length;
	
	public Register(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		this.client_Thread = mainFrame.client_Thread;
		Dimension d = new Dimension(80,30);
		Dimension d2 = new Dimension(200, 30);
		p_north = new JPanel();
			p_north.setPreferredSize(new Dimension(380, 50));
		p_center = new JPanel();
			p_center.setPreferredSize(new Dimension(330, 500));
			//p_center.setLayout(new GridLayout());
		p_south = new JPanel();	
			p_south.setPreferredSize(new Dimension(380, 50));
		lb_title = new JLabel("ȸ������");
			lb_title.setFont(new Font("����", Font.BOLD, 30));
		lb_id = new JLabel("���̵�");
			lb_id.setPreferredSize(d);
		txt_id = new JTextField();
			txt_id.setPreferredSize(d2);
		lb_id_check = new JLabel("���̵� �ߺ�üũ");
			lb_id_check.setPreferredSize(new Dimension(300, 30));
			lb_id_check.setFont(new Font("����", Font.BOLD, 11));
			lb_id_check.setHorizontalAlignment(SwingConstants.CENTER);
			lb_id_check.setForeground(Color.RED);
		lb_name = new JLabel("�̸�");
			lb_name.setPreferredSize(d);
		txt_name = new JTextField();
			txt_name.setPreferredSize(d2);
		lb_pw = new JLabel("��й�ȣ");
			lb_pw.setPreferredSize(d);
		txt_pw = new JPasswordField();
			txt_pw.setPreferredSize(d2);
		lb_pw_check = new JLabel("��й�ȣ Ȯ��");
			lb_pw_check.setPreferredSize(d);
			lb_pw_check.setFont(new Font(null, Font.BOLD, 11));
		txt_pw_check = new JPasswordField();	
			txt_pw_check.setPreferredSize(d2);
		lb_birth = new JLabel("�������");
			lb_birth.setPreferredSize(d);
		ch_year = new Choice();
			ch_year.setPreferredSize(new Dimension(65, 30));
		ch_month = new Choice();
			ch_month.setPreferredSize(new Dimension(65, 30));
		ch_day = new Choice();	
			ch_day.setPreferredSize(new Dimension(65, 30));
		lb_email = new JLabel("�̸���");
			lb_email.setPreferredSize(d);
		txt_email = new JTextField();
			txt_email.setPreferredSize(new Dimension(80, 30));
		lb_gol = new JLabel("@");
		ch_mail = new Choice();
			ch_mail.setPreferredSize(new Dimension(100, 40));
		lb_profile = new JLabel("�ƹ�Ÿ����");
			lb_profile.setPreferredSize(d);
		btn_addProfile = new JButton("�ƹ�Ÿ����");
			btn_addProfile.setBackground(new Color(150,112,7));
		btn_regist = new JButton("���ԿϷ�");
			btn_regist.setBackground(new Color(150,112,7));
		btn_cancel = new JButton("���ư���");
			btn_cancel.setBackground(new Color(150,112,7));
		chooser = new JFileChooser();
		
		//������� choice �� �ֱ�	
		for(int i=2019; i>=1930; i--) {
			ch_year.add(""+i+"");
		}
		ch_year.select((2019-1930)/2);
		for(int i=1; i<=12; i++) {
			ch_month.add(""+i+"");
		}
		ch_month.select(6);
		for(int i=1; i<=31; i++) {
			ch_day.add(""+i+"");
		}
		ch_day.select(15);
		
		//�̸��� choice �� �ֱ�
		for(int i=0; i<emailArray.length; i++) {
			ch_mail.add(emailArray[i].toString());	
		}
		ch_mail.addItemListener(new ItemListener() {		
			@Override
			public void itemStateChanged(ItemEvent e) {
				setEmail();
			}
		});
		
		avatarSelect = new AvatarSelect();
		
		lb_title.setBounds(190,100,380,100); 
		p_north.add(lb_title);
		
		p_center.add(lb_id);
		p_center.add(txt_id);
		p_center.add(lb_id_check);
		p_center.add(lb_name);
		p_center.add(txt_name);
		p_center.add(lb_pw);
		p_center.add(txt_pw);
		p_center.add(lb_pw_check);
		p_center.add(txt_pw_check);
		p_center.add(lb_birth);
		p_center.add(ch_year);
		p_center.add(ch_month);
		p_center.add(ch_day);
		p_center.add(lb_profile);
		can_profile.setPreferredSize(new Dimension(100, 100));
		p_center.add(can_profile);
		p_center.add(btn_addProfile);
		p_center.add(avatarSelect);
		
		p_south.add(btn_regist);
		p_south.add(btn_cancel);
		
		p_center.add(lb_email);
		p_center.add(txt_email);
		p_center.add(lb_gol);
		p_center.add(ch_mail);
		p_north.setBackground(new Color(195,237,96));
		p_center.setBackground(new Color(195,237,96));
		p_south.setBackground(new Color(195,237,96));
		this.setBackground(new Color(195,237,96));
		
		this.add(p_north, BorderLayout.NORTH);
		this.add(p_center);
		this.add(p_south,BorderLayout.SOUTH);
		
		lb_id_check.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkId_count++;
				request_CheckId(); 
			}
		});
		
		//���̵� �ߺ�üũ�����Ͽ� �������� ����
		Document doc = txt_id.getDocument();
		doc.addDocumentListener(new DocumentListener() {		
			@Override
			public void removeUpdate(DocumentEvent e) {
				idChecked = false;		
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				idChecked = false;	
			}
			@Override
			public void changedUpdate(DocumentEvent e) {	
			}
		});
		
		//�ƹ�Ÿ ���� ��ư ���ý�
		btn_addProfile.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				avatarSelect.register = Register.this; //���� register�� �ּҰ��� �ƹ�Ÿ���� ����
				avatarSelect.setVisible(true);
			}
		});
		
		btn_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changePage(0);
			}
		});
		
		//���ԿϷ� ��ư Ŭ��
		// ���̽� �ۼ��� method
		btn_regist.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				request_regist();
			}
		});
		
		this.setPreferredSize(new Dimension(380, 600));
		this.setVisible(false);
	}
	
	//�̸����� '�����Է�'���� ������ ���
	public void setEmail() {
		if(ch_mail.getSelectedItem() == "�����Է�") {
			p_center.remove(ch_mail);
			txt_input_email = new JTextField();
			txt_input_email.setPreferredSize(new Dimension(100, 30));
			p_center.add(txt_input_email);
			p_center.updateUI(); //������Ʈ ���� �߰��ϸ� ������ updateUI
		}
	}
	
	//[Ŭ���̾�Ʈ ��Ʈ�ѷ�]�� ���� checkId��û �޼���
	public void request_CheckId() {
		user_id = txt_id.getText();
		System.out.println("Register : request_CheckId() method : user_id -> "+user_id);
		System.out.println("Register : request_CheckId() method : client_Thread -> "+client_Thread);
		System.out.println("Register : request_CheckId() method : client_Thread.client_checkId_controller -> "+client_Thread.client_checkId_controller);
		mainFrame.client_checkId_controller.checkId_request(user_id);
	}
	
	//[Ŭ���̾�Ʈ ��Ʈ�ѷ�]�κ��� ���� checkId��� �޼���
	public void response_checkId(String flag) {
		this.checkId_flag = flag;
		if(flag.equals("true")) { //���̵� ����Ҽ� ���ٸ�
			JOptionPane.showMessageDialog(this, "�̹� ������� ���̵��Դϴ�.");
			checkId_count=0;
		}else {
			JOptionPane.showMessageDialog(this, "��밡���� ���̵��Դϴ�.");
			ok_id_length = txt_id.getText().length();
		}
	}
	
	//[Ŭ���̾�Ʈ ��Ʈ�ѷ�]�� ���� regist��û �޼���
	public void request_regist() {
		int confirm_ok_id_length = txt_id.getText().length();
		String member_id = txt_id.getText();
		String pw = txt_pw.getText();
		String user_pw_check = txt_pw_check.getText();
		String member_name = txt_name.getText();
		String birth = ch_year.getSelectedItem()+ch_month.getSelectedItem()+ch_day.getSelectedItem();
		String email = txt_email.getText()+lb_gol.getText()+ch_mail.getSelectedItem();
		String profile = avatarSelect.selected_path;
		System.out.println(profile);
		
		// �Է��� �н������ �н����� Ȯ�� ���� ���� ���̵� �ߺ� üũ ����� �����̶�� ȸ����� �۾� ����
		if(pw.equals(user_pw_check) && checkId_flag.equals("false")) {
			System.out.println("Register : request_regist() method ���� ȸ����� ��û�� �����ϴ�.");
			mainFrame.client_regist_controller.regist_request(member_id, member_name, pw, birth, email, profile);
			checkId_flag="true";
		// ���̵� �ߺ�üũ ��ư�� �����ų� �ߺ�Ȯ�� �� ���̵� �Է°��� �����Ͽ��ٸ� �ߺ�Ȯ�� �����Ұ��� �˸�
		}else if(checkId_count==0 || !(confirm_ok_id_length == ok_id_length)){
			JOptionPane.showMessageDialog(this, "���̵� �ߺ�Ȯ���� �����ϼ���.");
		// �Է��� �н������ �н����� Ȯ�� ���� ���� �ʴٸ� �Ʒ� �ȳ������� ���
		}else if(!(pw.equals(user_pw_check))) {
			JOptionPane.showMessageDialog(this, "��й�ȣ �Է��� Ȯ���ϼ���");
		}
	}
//	
//	//ȸ�� ����(1�� ��ϰ���)
//	// ȸ������ controller �� �̵��� method
//	public void memberRegist() {
//		if(idChecked == false) {
//			JOptionPane.showMessageDialog(this, "���̵� �ߺ����θ� üũ�ϼ���");
//			return;
//		}
//		//�������� ���� �� �ʱ�ȭ
//		PreparedStatement pstmt = null;
//		int result = 0; 
//		
//		//�ʵ尪 ������ : seq_member, member_id, name, pw, birth, profile, email
//		String id = txt_id.getText();
//		String pw = txt_pw.getText();
//		String pw_check = txt_pw_check.getText();
//		String name = txt_name.getText();
//		String birth = ch_year.getSelectedItem()+ch_month.getSelectedItem()+ch_day.getSelectedItem();
//		String email = txt_email.getText()+lb_gol.getText()+ch_mail.getSelectedItem();
//		System.out.println(birth);
//		System.out.println(email);
//		//��ȿ�� üũ
//		if(id.length() == 0){
//			JOptionPane.showMessageDialog(this, "���̵� �Է��ϼ���.");
//		}else if(pw.length() == 0 || pw_check.length() == 0){
//			JOptionPane.showMessageDialog(this, "��й�ȣ�� �Է��ϼ���.");
//		}else if(name.length() == 0) {
//			JOptionPane.showMessageDialog(this, "�̸��� �Է��ϼ���.");
//		}else if(birth.length() == 0) {
//			JOptionPane.showMessageDialog(this, "������ �����ϼ���.");
//		}else if(email.length() == 0 ) {
//			JOptionPane.showMessageDialog(this, "������ �Է��ϼ���.");
//		}else { //��� ������ �Է��ߴٸ� sql����  �÷��ֱ�
//			if(!pw.equals(pw_check)) { //������Ϸ��� equals! ����ϱ�
//				JOptionPane.showMessageDialog(this, "��й�ȣ�� ����ġ �մϴ�.");
//				return;
//			}
//			String sql = "insert into member(seq_member,member_id, member_name, pw, birth, profile, email)";
//			sql = sql + " values (seq_member.nextval,'"+id+"','"+name+"','"+pw+"','"+birth+"','"+profileName+"','"+email+"')";
//			System.out.println(sql);
//			try {
//				pstmt = adminManager.con.prepareStatement(sql);
//				result = pstmt.executeUpdate();
//				if(result == 0) {
//					JOptionPane.showMessageDialog(this, "������ �����Ͽ����ϴ�.");
//				}else {
//					JOptionPane.showMessageDialog(this, "������ �Ϸ�Ǿ����ϴ�.");
//					JOptionPane.showMessageDialog(this, "�α��� ȭ������ �̵��մϴ�.");
//					move_loginForm = new LoginForm();
//					this.setVisible(false);
//				}
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}finally {
//				if(pstmt != null) {
//					try {
//						pstmt.close();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//	}
}








