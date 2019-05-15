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
	JButton btn_regist;	//가입완료버튼
	JButton btn_cancel;

	Font font;
	boolean idChecked = false; //아이디 중복체크를 했는지 안했는지 여부
	LoginForm move_loginForm;
	JFileChooser chooser;
	String profile; //Member DB 프로필사진 필드명
	String profileName;
	String[] emailArray = {"선택","naver.com","google.com","nate.com","daum.net","직접입력"};
	Image img;
	Canvas can_profile = new Canvas() {
		@Override
		public void paint(Graphics g) {
			System.out.println("paint메서드");
			g.setColor(new Color(255, 216, 216));
			g.fillRect(0, 0, 100, 100);
			g.drawImage(img, 0, 0, 100, 100, this);
			
		}
	};
	
	//json관련 멤버변수
	String request_msg; //json통해 보낼 메세지
	BufferedWriter json_writer; //json을 실어보낼 출력스트림
	
	//통신관련 멤버변수
	Socket client;
	Client_Thread client_Thread;
	
	//유저가 입력하는 textFiled값을 받을 변수
	String user_id;
	String user_name;
	String user_pw;
	String user_birth;
	String user_email;
	String user_profile;
	
	AvatarSelect avatarSelect;//아바타를 고를 패널 
	MainFrame mainFrame; //부모 객체
	
	String checkId_flag="true"; //아이디 중복체크 결과를 받을 변수
	int checkId_count;	// 아이디 중복 체크 시행 여부를 받는 변수 (했다면 1, 안했다면 0)
	String regist_flag; //회원 가입 결과를 받을 변수
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
		lb_title = new JLabel("회원가입");
			lb_title.setFont(new Font("굴림", Font.BOLD, 30));
		lb_id = new JLabel("아이디");
			lb_id.setPreferredSize(d);
		txt_id = new JTextField();
			txt_id.setPreferredSize(d2);
		lb_id_check = new JLabel("아이디 중복체크");
			lb_id_check.setPreferredSize(new Dimension(300, 30));
			lb_id_check.setFont(new Font("돋움", Font.BOLD, 11));
			lb_id_check.setHorizontalAlignment(SwingConstants.CENTER);
			lb_id_check.setForeground(Color.RED);
		lb_name = new JLabel("이름");
			lb_name.setPreferredSize(d);
		txt_name = new JTextField();
			txt_name.setPreferredSize(d2);
		lb_pw = new JLabel("비밀번호");
			lb_pw.setPreferredSize(d);
		txt_pw = new JPasswordField();
			txt_pw.setPreferredSize(d2);
		lb_pw_check = new JLabel("비밀번호 확인");
			lb_pw_check.setPreferredSize(d);
			lb_pw_check.setFont(new Font(null, Font.BOLD, 11));
		txt_pw_check = new JPasswordField();	
			txt_pw_check.setPreferredSize(d2);
		lb_birth = new JLabel("생년월일");
			lb_birth.setPreferredSize(d);
		ch_year = new Choice();
			ch_year.setPreferredSize(new Dimension(65, 30));
		ch_month = new Choice();
			ch_month.setPreferredSize(new Dimension(65, 30));
		ch_day = new Choice();	
			ch_day.setPreferredSize(new Dimension(65, 30));
		lb_email = new JLabel("이메일");
			lb_email.setPreferredSize(d);
		txt_email = new JTextField();
			txt_email.setPreferredSize(new Dimension(80, 30));
		lb_gol = new JLabel("@");
		ch_mail = new Choice();
			ch_mail.setPreferredSize(new Dimension(100, 40));
		lb_profile = new JLabel("아바타설정");
			lb_profile.setPreferredSize(d);
		btn_addProfile = new JButton("아바타보기");
			btn_addProfile.setBackground(new Color(150,112,7));
		btn_regist = new JButton("가입완료");
			btn_regist.setBackground(new Color(150,112,7));
		btn_cancel = new JButton("돌아가기");
			btn_cancel.setBackground(new Color(150,112,7));
		chooser = new JFileChooser();
		
		//생년월일 choice 값 넣기	
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
		
		//이메일 choice 값 넣기
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
		
		//아이디 중복체크관련하여 부정행위 방지
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
		
		//아바타 보기 버튼 선택시
		btn_addProfile.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				avatarSelect.register = Register.this; //현재 register의 주소값을 아바타에게 주입
				avatarSelect.setVisible(true);
			}
		});
		
		btn_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.changePage(0);
			}
		});
		
		//가입완료 버튼 클릭
		// 제이슨 작성될 method
		btn_regist.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				request_regist();
			}
		});
		
		this.setPreferredSize(new Dimension(380, 600));
		this.setVisible(false);
	}
	
	//이메일을 '직접입력'으로 선택한 경우
	public void setEmail() {
		if(ch_mail.getSelectedItem() == "직접입력") {
			p_center.remove(ch_mail);
			txt_input_email = new JTextField();
			txt_input_email.setPreferredSize(new Dimension(100, 30));
			p_center.add(txt_input_email);
			p_center.updateUI(); //컴포넌트 새로 추가하면 무조건 updateUI
		}
	}
	
	//[클라이언트 컨트롤러]로 보낼 checkId요청 메서드
	public void request_CheckId() {
		user_id = txt_id.getText();
		System.out.println("Register : request_CheckId() method : user_id -> "+user_id);
		System.out.println("Register : request_CheckId() method : client_Thread -> "+client_Thread);
		System.out.println("Register : request_CheckId() method : client_Thread.client_checkId_controller -> "+client_Thread.client_checkId_controller);
		mainFrame.client_checkId_controller.checkId_request(user_id);
	}
	
	//[클라이언트 컨트롤러]로부터 받은 checkId결과 메서드
	public void response_checkId(String flag) {
		this.checkId_flag = flag;
		if(flag.equals("true")) { //아이디를 사용할수 없다면
			JOptionPane.showMessageDialog(this, "이미 사용중인 아이디입니다.");
			checkId_count=0;
		}else {
			JOptionPane.showMessageDialog(this, "사용가능한 아이디입니다.");
			ok_id_length = txt_id.getText().length();
		}
	}
	
	//[클라이언트 컨트롤러]로 보낼 regist요청 메서드
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
		
		// 입력한 패스워드와 패스워드 확인 값이 같고 아이디 중복 체크 결과가 긍정이라면 회원등록 작업 수행
		if(pw.equals(user_pw_check) && checkId_flag.equals("false")) {
			System.out.println("Register : request_regist() method 에서 회원등록 요청을 보냅니다.");
			mainFrame.client_regist_controller.regist_request(member_id, member_name, pw, birth, email, profile);
			checkId_flag="true";
		// 아이디 중복체크 버튼을 누렀거나 중복확인 후 아이디 입력값을 변경하였다면 중복확인 진행할것을 알림
		}else if(checkId_count==0 || !(confirm_ok_id_length == ok_id_length)){
			JOptionPane.showMessageDialog(this, "아이디 중복확인을 진행하세요.");
		// 입력한 패스워드와 패스워드 확인 값이 같지 않다면 아래 안내사항을 출력
		}else if(!(pw.equals(user_pw_check))) {
			JOptionPane.showMessageDialog(this, "비밀번호 입력을 확인하세요");
		}
	}
//	
//	//회원 가입(1명씩 등록가능)
//	// 회원가입 controller 로 이동할 method
//	public void memberRegist() {
//		if(idChecked == false) {
//			JOptionPane.showMessageDialog(this, "아이디 중복여부를 체크하세요");
//			return;
//		}
//		//지역변수 선언 및 초기화
//		PreparedStatement pstmt = null;
//		int result = 0; 
//		
//		//필드값 얻어오기 : seq_member, member_id, name, pw, birth, profile, email
//		String id = txt_id.getText();
//		String pw = txt_pw.getText();
//		String pw_check = txt_pw_check.getText();
//		String name = txt_name.getText();
//		String birth = ch_year.getSelectedItem()+ch_month.getSelectedItem()+ch_day.getSelectedItem();
//		String email = txt_email.getText()+lb_gol.getText()+ch_mail.getSelectedItem();
//		System.out.println(birth);
//		System.out.println(email);
//		//유효성 체크
//		if(id.length() == 0){
//			JOptionPane.showMessageDialog(this, "아이디를 입력하세요.");
//		}else if(pw.length() == 0 || pw_check.length() == 0){
//			JOptionPane.showMessageDialog(this, "비밀번호를 입력하세요.");
//		}else if(name.length() == 0) {
//			JOptionPane.showMessageDialog(this, "이름을 입력하세요.");
//		}else if(birth.length() == 0) {
//			JOptionPane.showMessageDialog(this, "생일을 선택하세요.");
//		}else if(email.length() == 0 ) {
//			JOptionPane.showMessageDialog(this, "메일을 입력하세요.");
//		}else { //모든 정보를 입력했다면 sql문을  올려주기
//			if(!pw.equals(pw_check)) { //내용비교하려면 equals! 사용하기
//				JOptionPane.showMessageDialog(this, "비밀번호가 불일치 합니다.");
//				return;
//			}
//			String sql = "insert into member(seq_member,member_id, member_name, pw, birth, profile, email)";
//			sql = sql + " values (seq_member.nextval,'"+id+"','"+name+"','"+pw+"','"+birth+"','"+profileName+"','"+email+"')";
//			System.out.println(sql);
//			try {
//				pstmt = adminManager.con.prepareStatement(sql);
//				result = pstmt.executeUpdate();
//				if(result == 0) {
//					JOptionPane.showMessageDialog(this, "가입이 실패하였습니다.");
//				}else {
//					JOptionPane.showMessageDialog(this, "가입이 완료되었습니다.");
//					JOptionPane.showMessageDialog(this, "로그인 화면으로 이동합니다.");
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








