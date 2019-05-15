package server;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


// 메신저 문자데이터를 받고 다시 다른 상대방에게 보내는 역할을 하게될 Server와 관련된 Class 입니다.
public class MessengerServer  extends JFrame{
	
	// 프레임 디자인과 관련한 변수
	JPanel p_north;
	JTextField t_port;
	JButton bt;
	JTextArea area;
	JScrollPane scroll;
	JScrollBar bar;
	
	// 서버 소켓과 관련한 멤버변수
	ServerSocket server;
	int port = 7777;
	public Socket client;
	
	// 서버 쓰레드와 관련한 변수
	Thread serverThread;
	public Vector<ServerThread> list=new Vector();
	
	public MessengerServer() {
		
		p_north=new JPanel();
		t_port= new JTextField(Integer.toString(port),8); 
		bt= new JButton("서버가동");
		area= new JTextArea();
		scroll= new JScrollPane(area);
		bar = scroll.getVerticalScrollBar();
		
		// 서버 쓰레드 생성
		serverThread= new Thread() {
			public void run() {
				System.out.println("▶ serverThread running~~~");
				runServer();
			}
		};
		
		// 프레임에 부착하기
		p_north.add(t_port);
		p_north.add(bt);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		// 채팅창 폰트 사이즈 조절
		area.setFont(new Font("돋움", Font.BOLD, 23));
		
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				serverThread.start();
			}
		});
		
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		setSize(600,600);
		setVisible(true);
		
	}
	public void runServer() {
		port=Integer.parseInt(t_port.getText());
		try {
			server=new ServerSocket(port); // 서버 생성
			area.append("서버 생성 완료\n");
			
			// 접속자 감지 행위를 계속 진행함
			while(true) {
				Socket client=server.accept(); // 서버 가동
				String ip=client.getInetAddress().getHostAddress();
				area.append(ip+"님이 접속했습니다\n");
				// 접속자 각각을 감당할 쓰레드 생성
				ServerThread st = new ServerThread(this, client);
				st.start(); 
				list.add(st);
				System.out.println("vector리스트에 담길 서버스레드 : "+st);
				area.append(list.size()+"명 접속중\n");
			}	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new MessengerServer();

	}

}
