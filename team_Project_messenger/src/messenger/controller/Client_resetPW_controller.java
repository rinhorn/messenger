package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.google.gson.JsonElement;

import messenger.MainFrame;
import messenger.loginForm.CheckThread;
import messenger.loginForm.FindId;
import messenger.loginForm.Register;
import messenger.loginForm.ResetPw;

public class Client_resetPW_controller {
	Socket client;
	MainFrame mainFrame;
	Thread thread;

	public Client_resetPW_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}

	// ★ 클라이언트부터 요청받은 [requestType] JSOM작성하여 서버로 전송

	public void resetPW_request(String user_id, String user_birth) {
		StringBuffer sb = new StringBuffer();

		sb.append("{");
		sb.append("\"requestType\":\"resetPW\",");
		sb.append("\"user_id\":\"" + user_id + "\",");
		sb.append("\"user_birth\":\"" + user_birth + "\"");
		sb.append("}");

		String request_msg = sb.toString();

		System.out.println("Client_resetPW_controller曰 : request_msg : " + request_msg);
		try {
			BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(request_msg + "\n");
			buffw.flush();
			System.out.println("Client_resetPW_controller : request_msg 전송 완료");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ★ 메일 내 인증번호와 입력한 인증번호 검증 요청!!
	public void certify_request(int keycode) {
		StringBuffer sb = new StringBuffer();

		int key = keycode;

		sb.append("{");
		sb.append("\"requestType\":\"certify\",");
		sb.append("\"key\":\"" + key + "\"");
		sb.append("}");
		
		String msg = sb.toString();
		
		System.out.println("Client_resetPW_controller曰 : certify_request : request_msg : " + msg);
		
		try {
			BufferedWriter buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(msg + "\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	// ★ 서버로부터 넘겨받은 [responseType] JSON 파싱

	public void resetPW_parser(JsonElement obj) {
		System.out.println("Client_resetPW_controller : resetPW_parser() method 호출");
		Boolean flag = obj.getAsJsonObject().get("flag").getAsBoolean();
		System.out.println("Client_resetPW_controller曰 : resetPW 결과 전송=> flag : " + flag);
		if (flag==true) {
			JOptionPane.showMessageDialog(mainFrame.resetPw, "인증번호를 메일로 보냈습니다.");
			
			//resetPW 내 타이머 작동!!
			thread = new Thread(mainFrame.resetPw);
			thread.start();
			
			mainFrame.resetPw.btn_sendMail.setEnabled(false);
			mainFrame.resetPw.btn_certify.setEnabled(true);
			mainFrame.resetPw.txt_new_pw.setText("");
			
			CheckThread checkThread = new CheckThread(mainFrame);
			checkThread.start();
		} else {
			JOptionPane.showMessageDialog(mainFrame.resetPw, "일치하는 회원 정보가 없습니다.");
		}
	}
	
	public void certify_parser(JsonElement obj) {
		System.out.println("Client_resetPW_controller : certify_parser() method 호출");
		Boolean flag = obj.getAsJsonObject().get("flag").getAsBoolean();
		System.out.println("Client_resetPW_controller曰 : certify 결과 전송=> flag : " + flag);
		if (flag==true) {
			JOptionPane.showMessageDialog(mainFrame.resetPw, "인증번호가 일치합니다.");
			
			mainFrame.resetPw.setThread_flag(false);
			mainFrame.resetPw.setTimer(180);
			mainFrame.resetPw.lb_timer.setText("03:00");
			
			mainFrame.resetPw.btn_certify.setEnabled(false);
			mainFrame.resetPw.txt_new_pw.setEditable(true);
			mainFrame.resetPw.txt_pwCheck.setEditable(true);
			
		} else {
			JOptionPane.showMessageDialog(mainFrame.resetPw, "인증번호가 일치하지 않습니다.");
		}
	}
	
}
