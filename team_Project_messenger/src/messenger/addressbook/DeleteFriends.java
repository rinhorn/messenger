/*
 * 해야할 것
 *  1. AddressBook에서 deleteFriends를 new 해주면서 생성자로 AddressBook 넘겨받기
 *  2. delete result 메서드도 addressBook에 직접 적기
 *  3. 로그인 폼에서 client를 new 해주니까 여기서 끌어오기
 *  4. 매개변수 잘 넘겨받는지 찍어보기
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

// 친구 삭제 버튼 클릭시 작업과 관련된 클래스
public class DeleteFriends {
	Client_Thread client_thread;  // <-- 메인? 에서 클라이언트 new 해주니까 getter 끌어오기 
	Address_book address_book; // 삭제 버튼과 리스너 연결하기 위해서 친구목록창이 필요하다  
	PersonBox personBox; // 선택한 라벨의 정보를 알기위해서
	String request_msg; // json에 보낼 메시지
	BufferedWriter json_writer; // json을 실어 보낼 출력스트림
	LoginForm loginForm;
	Socket client;  // <-- 로그인 폼에서 client를 new 해주니까 getter 끌어오기 
	String friendId;
	Object[] newFriendID; // 새로 갱신할 친구 목록을 담을 배열
	Object[] newNames; // 새로 갱신된 친구목록의 이름을 담을 배열
	MainFrame mainFrame;
	public DeleteFriends(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
//		this.friendId= friendId;
		
		// 친구를 선택한 상태에서 '친구삭제' 버튼을 누르면 정보 넘겨주고, 서버쓰레드에서 deleteFriend_controller 실행
		
		/*
		// 삭제 버튼과 리스너 연결
		address_book.bt_deleteFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int result= JOptionPane.showConfirmDialog(address_book, "삭제 하시겠습니까?");
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
		System.out.println("삭제 요청 메서드를 호출했습니다");
		
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
			System.out.println("삭제 요청 보내기를 성공했습니다");
		} catch (IOException e) {
			System.out.println("삭제 요청 보내기 실패");
			e.printStackTrace();
		}
	}
	
	// 친구 삭제 결과 여부를 받아서 표현할 메서드
	public void delete_result(boolean result, Object[] newNames, Object[] newFriendID) {
		this.newNames = newNames;
		this.newFriendID= newFriendID;
		if(result) {
			JOptionPane.showMessageDialog(address_book, "친구 삭제 성공");
			//address_book.showAllFriends(newNames, newFriendID); 	// 업데이트 된 친구 목록을 재출력
		}else {
			JOptionPane.showMessageDialog(address_book, "친구 삭제 실패");
		}
	}

	
}
