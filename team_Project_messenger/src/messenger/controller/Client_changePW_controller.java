package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.google.gson.JsonElement;

import messenger.MainFrame;

//'login'�� ��û�ϴ� Ŭ���̾�Ʈ�� ��Ʈ�ѷ�
public class Client_changePW_controller {
	//Ŭ���̾�Ʈ�� ������ request ����
	String user_id;
	String user_pw;
	
	//JSON���� ����
	String request_msg;
	BufferedWriter buffw;
	
	Socket client;
	MainFrame mainFrame;
	
	//Server���� ���� JSON����
	boolean flag;
	
	public Client_changePW_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	//�� Ŭ���̾�Ʈ���� ��û���� [requestType] JSOM�ۼ��Ͽ� ������ ����
	
	public void changePW_request(String user_id,String user_pw) {
		this.user_id = user_id;
		this.user_pw = user_pw;
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\":\"changePW\",");
		sb.append("\"user_id\":"+"\""+user_id+"\",");
		sb.append("\"user_pw\":"+"\""+user_pw+"\"");
		sb.append("}");
		System.out.println("Client_change_controller : Ŭ���̾�Ʈ�� ������ user_id : "+user_id);
		request_msg = sb.toString();
		System.out.println("Client_changePW_controller�� : request_msg : "+request_msg);
		try {
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(request_msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//�� �����κ��� �Ѱܹ��� [responseType] JSON �Ľ�
	
	public void changePW_parser(JsonElement obj) {
		flag = obj.getAsJsonObject().get("flag").getAsBoolean();
		System.out.println("Client_changePW_controller : changePW_parser : Server���� �Ѿ�� Flag : "+flag);
		
		if(flag) {
			JOptionPane.showMessageDialog(mainFrame, "��й�ȣ ���� ����");
			mainFrame.resetPw.btn_change.setEnabled(false);
			mainFrame.checkThread.thread_flag = false;
		}else {
			JOptionPane.showMessageDialog(mainFrame, "��й�ȣ ���� ����");
		}
	}
}






