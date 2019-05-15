/*
 * �ؾ��� ��
 *  1. AddressBook���� deleteFriends�� new ���ָ鼭 �����ڷ� AddressBook �Ѱܹޱ�
 *  2. delete result �޼��嵵 addressBook�� ���� ����
 *  3. �α��� ������ client�� new ���ִϱ� ���⼭ �������
 *  4. �Ű����� �� �Ѱܹ޴��� ����
 *  
 * */

package messenger.addressbook;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

import messenger.Client_Thread;
import messenger.MainFrame;
import messenger.loginForm.LoginForm;

// ģ�� ���� ��ư Ŭ���� �۾��� ���õ� Ŭ����
public class DeleteFriends {
	Client_Thread client_thread;  // <-- ����? ���� Ŭ���̾�Ʈ new ���ִϱ� getter ������� 
	Address_book address_book; // ���� ��ư�� ������ �����ϱ� ���ؼ� ģ�����â�� �ʿ��ϴ�  
	PersonBox personBox; // ������ ���� ������ �˱����ؼ�
	String request_msg; // json�� ���� �޽���
	BufferedWriter json_writer; // json�� �Ǿ� ���� ��½�Ʈ��
	LoginForm loginForm;
	Socket client;  // <-- �α��� ������ client�� new ���ִϱ� getter ������� 
	String friendId;
	Object[] newFriendID; // ���� ������ ģ�� ����� ���� �迭
	Object[] newNames; // ���� ���ŵ� ģ������� �̸��� ���� �迭
	MainFrame mainFrame;
	public DeleteFriends(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
//		this.friendId= friendId;
		
		// ģ���� ������ ���¿��� 'ģ������' ��ư�� ������ ���� �Ѱ��ְ�, ���������忡�� deleteFriend_controller ����
		
		/*
		// ���� ��ư�� ������ ����
		address_book.bt_deleteFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result= JOptionPane.showConfirmDialog(address_book, "���� �Ͻðڽ��ϱ�?");
				if(result==JOptionPane.OK_OPTION) {
					send_signal();
				}
			}
		});
		*/
		/*
		StringBuffer sb=null;
		sb.append("{");
		sb.append("\"requestType\":\"delete\",");
		sb.append("\"friendId\":"+"\""+friendId+"\"");
		sb.append("}");
		
		request_msg = sb.toString();
		*/
	}
	public void send_signal(String friendId){
		this.friendId= friendId;
		System.out.println("���� ��û �޼��带 ȣ���߽��ϴ�");
		
		StringBuffer sb=new StringBuffer();
		
		sb.append("{");
			sb.append("\"requestType\":\"delete\",");
			sb.append("\"friendId\":"+"\""+friendId+"\"");
		sb.append("}");
		
		request_msg = sb.toString();
		
		try {
			json_writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			json_writer.write(request_msg+"\n");
			json_writer.flush();
			System.out.println("���� ��û �����⸦ �����߽��ϴ�");
		} catch (IOException e) {
			System.out.println("���� ��û ������ ����");
			e.printStackTrace();
		}
	}
	
	// ģ�� ���� ��� ���θ� �޾Ƽ� ǥ���� �޼���
	public void delete_result(boolean result, Object[] newNames, Object[] newFriendID) {
		this.newNames = newNames;
		this.newFriendID= newFriendID;
		if(result) {
			JOptionPane.showMessageDialog(address_book, "ģ�� ���� ����");
			//address_book.showAllFriends(newNames, newFriendID); 	// ������Ʈ �� ģ�� ����� �����
		}else {
			JOptionPane.showMessageDialog(address_book, "ģ�� ���� ����");
		}
	}

	
}
