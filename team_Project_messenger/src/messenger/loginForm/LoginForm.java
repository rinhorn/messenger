package messenger.loginForm;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import messenger.Client_Thread;
import messenger.MainFrame;
import messenger.controller.Client_login_controller;
import messenger.main.Main;

public class LoginForm extends JPanel{
	JPanel p_center;
	JPanel p_north;
	JPanel p_south;
	JPanel p_input;
	JTextField txt_id;
	Choice ch_id;
	JPasswordField txt_pw;
	JButton btn_login;
	Toolkit kit = Toolkit.getDefaultToolkit();
	Image img = kit.getImage(getClass().getClassLoader().getResource("main.png"));
	public JLabel lb_open_resist; //ȸ������â
	public JLabel find_id, lb, reset_pw; //����(id)ã��, ��й�ȣ �缳��
	FindId findId;
	ResetPw resetPw;
	public static final int WIDTH = 380;
	public static final int HEIGHT = 600;
	boolean loginCheck = true;

	MainFrame mainFrame;
	Main main;
	Client_login_controller client_login_controller;
	
	public String entered_seq_member, entered_id, entered_pw, entered_name; // �α��� ���� �� ������ �α��� ����

	
	String user_id, user_pw, user_name, user_pk;
	String flag;
	
	public LoginForm(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		
		this.setLayout(new BorderLayout());//������ ��ġ�� �г��� ���Բ� ��ü �гο� borderLayout���� ����
		
		p_center = new JPanel();
		p_south = new JPanel();
			p_south.setLayout(new BorderLayout());
		p_input = new JPanel();
			p_input.setBackground(new Color(195,237,96));	
		//ch_id = new Choice();
		txt_id = new JTextField();
		txt_pw = new JPasswordField();
		btn_login = new JButton("�α���");
			btn_login.setBackground(new Color(150,112,7));
		lb_open_resist = new JLabel("ȸ������");
		find_id = new JLabel("�ƺ�ī�� ���� ã��");
		lb = new JLabel("|");
		reset_pw = new JLabel("��й�ȣ �缳��");
				
		Dimension d = new Dimension(220,35);
		//ch_id.setPreferredSize(new Dimension(220, 40));
		txt_id.setPreferredSize(new Dimension(220, 35));
			p_center.add(txt_id);
			//p_center.add(ch_id);
		txt_pw.setPreferredSize(d);
			p_center.add(txt_pw);
		btn_login.setPreferredSize(d);
			p_center.add(btn_login);
		lb_open_resist.setPreferredSize(d);
			//lb_open_resist.setHorizontalAlignment(NORMAL);
			p_center.add(lb_open_resist);
			
		p_north = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.setColor(new Color(195,237,96));
				g.fillRect(0, 0, 380,250);
				g.drawImage(img, 80, 25, 200, 200, p_north);
			}
		};
		
		p_input.add(find_id);
		p_input.add(lb);
		p_input.add(reset_pw);
		
		p_south.add(p_input, BorderLayout.CENTER);
			p_input.setBorder(new EmptyBorder(100, 0, 0, 0));
																//top,left,bottom,right
		p_north.setPreferredSize(new Dimension(WIDTH,250));
			this.add(p_north, BorderLayout.NORTH);
		
		p_center.setPreferredSize(new Dimension(WIDTH,200));
			p_center.setBackground(new Color(195,237,96));
			this.add(p_center, BorderLayout.CENTER);
			
		p_south.setPreferredSize(new Dimension(WIDTH,150));
			p_south.setBackground(new Color(195,237,96));
			this.add(p_south, BorderLayout.SOUTH);
		
			
		
		//�α��� ��ư
		btn_login.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				request_Login();
			}
		});
		
		// ��й�ȣ �Է��� Enter Ű �Է½� �߻��ϴ� �̺�Ʈ�� ���� ������ ����
		txt_pw.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int keycode = e.getKeyCode();
				if(keycode == KeyEvent.VK_ENTER) {	
					request_Login();
				}
			}
		});
		
		
		// ȸ������ �� Ŭ���� �߻��ϴ� �̺�Ʈ�� ���� ������ ����
	/*	lb_open_resist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mainFrame.add(mainFrame.register);
			}
		});*/
		
		//���� ã�� 
		find_id.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println("�ƺ�ī�� ����ã��"); //�� ���� ã��
			}
		});
		
		//��й�ȣ �缳��
		reset_pw.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("��й�ȣ �缳���ϱ�"); 
			}	
		});
		
		this.setPreferredSize(new Dimension(WIDTH,HEIGHT));
		this.setVisible(true);
	}
	
	//[Ŭ���̾�Ʈ ��Ʈ�ѷ�]�� ���� Login��û �޼���
	public void request_Login() {
		user_id = txt_id.getText();
		user_pw = txt_pw.getText();
		mainFrame.client_login_controller.login_request(user_id, user_pw);
		
	}
	
	//[Ŭ���̾�Ʈ ��Ʈ�ѷ�]�κ��� ���� Login��� �޼���
	public void response_Login(String flag, String user_name, String user_pk) {
		System.out.println("�α������� �������� �α��� �޼��� ȣ����");
		this.flag = flag;
		this.user_name = user_name;
		this.user_pk = user_pk;
		if(flag.equals("true")) { //����� "����"�̶��
			JOptionPane.showMessageDialog(this, user_name+"�� �α��εǾ����ϴ�.");
			this.setVisible(false);
			mainFrame.add(mainFrame.main);
			mainFrame.setUser_pk(user_pk);
			mainFrame.client_friendsList_controller.friendsList_request(mainFrame.getUser_pk());
			System.out.println("�α����� : mainFrame.main.address_book : "+mainFrame.main.address_book);
			mainFrame.pack();
		}else { //"����"�̶��
			JOptionPane.showMessageDialog(this, "��ġ�ϴ� ������ �����ϴ�.");
		}
	}
}
