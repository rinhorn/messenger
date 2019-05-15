package messenger.loginForm;

import java.awt.Color;
import java.net.Socket;

import messenger.MainFrame;

public class CheckThread extends Thread{
	MainFrame mainFrame;
	public boolean thread_flag = true;
	
	public CheckThread(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}
	
	@Override
	public void run() {
		while(thread_flag) {

			// 변경을 위한 비밀번호가 일치,불일치 할때 그래픽 변화
			if(mainFrame.resetPw.txt_new_pw.getText().equals(mainFrame.resetPw.txt_pwCheck.getText())) {
				mainFrame.resetPw.lb_pwCheck.setText("비밀번호 일치");
				mainFrame.resetPw.lb_pwCheck.setForeground(Color.BLACK);
				mainFrame.resetPw.btn_change.setEnabled(true);
			}else {
				mainFrame.resetPw.lb_pwCheck.setText("비밀번호 불일치");
				mainFrame.resetPw.lb_pwCheck.setForeground(Color.RED);
				mainFrame.resetPw.btn_change.setEnabled(false);
			}
			
		}
		
	}
}
