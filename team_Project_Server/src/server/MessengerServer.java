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


// �޽��� ���ڵ����͸� �ް� �ٽ� �ٸ� ���濡�� ������ ������ �ϰԵ� Server�� ���õ� Class �Դϴ�.
public class MessengerServer  extends JFrame{
	
	// ������ �����ΰ� ������ ����
	JPanel p_north;
	JTextField t_port;
	JButton bt;
	JTextArea area;
	JScrollPane scroll;
	JScrollBar bar;
	
	// ���� ���ϰ� ������ �������
	ServerSocket server;
	int port = 7777;
	public Socket client;
	
	// ���� ������� ������ ����
	Thread serverThread;
	public Vector<ServerThread> list=new Vector();
	
	public MessengerServer() {
		
		p_north=new JPanel();
		t_port= new JTextField(Integer.toString(port),8); 
		bt= new JButton("��������");
		area= new JTextArea();
		scroll= new JScrollPane(area);
		bar = scroll.getVerticalScrollBar();
		
		// ���� ������ ����
		serverThread= new Thread() {
			public void run() {
				System.out.println("�� serverThread running~~~");
				runServer();
			}
		};
		
		// �����ӿ� �����ϱ�
		p_north.add(t_port);
		p_north.add(bt);
		add(p_north, BorderLayout.NORTH);
		add(scroll);
		
		// ä��â ��Ʈ ������ ����
		area.setFont(new Font("����", Font.BOLD, 23));
		
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
			server=new ServerSocket(port); // ���� ����
			area.append("���� ���� �Ϸ�\n");
			
			// ������ ���� ������ ��� ������
			while(true) {
				Socket client=server.accept(); // ���� ����
				String ip=client.getInetAddress().getHostAddress();
				area.append(ip+"���� �����߽��ϴ�\n");
				// ������ ������ ������ ������ ����
				ServerThread st = new ServerThread(this, client);
				st.start(); 
				list.add(st);
				System.out.println("vector����Ʈ�� ��� ���������� : "+st);
				area.append(list.size()+"�� ������\n");
			}	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new MessengerServer();

	}

}
