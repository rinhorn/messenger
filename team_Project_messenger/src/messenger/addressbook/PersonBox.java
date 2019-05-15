package messenger.addressbook;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import messenger.Client_Thread;
import messenger.MainFrame;
import messenger.chat.Chat_Window;

// 등록된 맴버를 address_book 에 표현할 personBox
// Class 는 라벨을 상속받아 그 자체가 라벨로 되게 한다. 즉 현재 personBox는 라벨이되는것
public class PersonBox extends JLabel{
	public Chat_Window chat_window;
	Address_book address_book;
	int personBox_width = address_book.PANEL_BOTTOM_WIDTH;
	int personBox_height = 50;

	//선택된 personBox의 레퍼런스를 담을 변수.
	public String personBox_name;
	public String friendID;  
	public int click_count;
	boolean click_flag;
	Client_Thread chat_thread;
	MainFrame mainFrame;
	String user_pk;
	String friend_pk, friend_name; // frineds_pk와 friends_name 배열의 한명 한명
	
	//chat_window 리스트 생성
	ArrayList<Chat_Window> chat_window_array = new ArrayList<Chat_Window>();
	
	//유저가 선택한 친구와 한번도 대화방을 생성한 적이 없다면 초기값을 null로
	//추후, 유저가 동일한 친구와 대화 시도시에(=이미 채팅방이 생성되어있는 상태) 이 room_num값에 최초 생성될 시에 받은 room_num값을 입력해 준다
	public String room_num = null; 
	
	public PersonBox(Address_book address_book, String user_pk, String friend_pk, String friend_name) {
		this.user_pk = user_pk;
		this.friend_pk = friend_pk;
		this.friend_name = friend_name;
		this.address_book = address_book;
		System.out.println("▣ PersonBox 에서 받은 address_book : "+this.address_book);
		System.out.println("user_pk,friend_pk,friend_name : "+user_pk+","+friend_pk+","+friend_name);
		
		// personBox의 이름 처리를 클래스 내에서 해준다.
		this.setText((String)friend_name);
		// personBox은 기본이 투명하기 때문에 불투명처리
		this.setOpaque(true);
		
		// personBox 안에 사용할 문구의 font style 과 크기를 지정
		this.setFont(new Font("고딕", Font.BOLD, 15));
		
		// personBox의 배경색을 지정
		this.setBackground(new Color(235,235,235));
		
		// personBox 의 size 를 지정한다.
		this.setPreferredSize(new Dimension(personBox_width, personBox_height));
		
		// personBox 를 클릭하였을때 동작을 설정
		// 본 project 에서는 personBox 를 클릭시 채팅창이 뜰 수 있게 한다.
		// 본래 클릭 이벤트는 ActionListener 를 사용하나 라벨에서는 지원되지 않기 떄문에 mouseListener 를 사용
		// mouseListener 는 재정의 (Overide) 해야 할 추상 method 가 많기 때문에 필요한것만 사용하기 위해 Adapter 를 사용
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				click_count++;
				Object click_obj = e.getSource();
				PersonBox currentObj = (PersonBox)click_obj;
				System.out.println("▶ 클릭한 친구 이름 : "+currentObj.getText());
				System.out.println("▶ 클릭한 친구_pk : "+friend_pk);
				//System.out.println(currentObj.personBox_name);
				//System.out.println(this);
				//static 변수 selectedBox 에 현재 선택된 PersonBox를 선언.

				
				// 아래는 더블클릭을 구현할 Logic
				if(e.getClickCount()%2 == 0) {
					// PersonBox 를 클릭하여 채팅창을 열었다면 thread 에게 채팅창이 열린 사실을 알려줌
					//chat_thread.select_opend_window = true;
					
					if(room_num == null) {
						System.out.println("Address_book : 한번이라도 이 친구와 대화한적이 없으면 그 방번호는 null이어야되는데 그럴까? : "+room_num);
						chat_start_request(); //최초 대화방 생성
					}else {
						System.out.println("Address_book : 한번이라도 이 친구와 대화한적이 있으면 그 방번호는  : "+room_num);
						dialogue_request(); //기존의 대화 내용 출력
					}
					
				}else if(click_count%2 == 1){
					setFriend_pk();
					PersonBox.this.setBackground(Color.RED);
					PersonBox.this.repaint();
				}else if(click_count%2 == 0){
					setFriend_pk();
					PersonBox.this.setBackground(new Color(235, 235, 235));
					PersonBox.this.repaint();
				}
				
				/*if(click_count == 1) {
					click_flag = true;
					if(click_flag) {
						setBackground(Color.LIGHT_GRAY);
					}
				}else {
					click_flag = false;
					setBackground(new Color(235,235,235));
				}*/
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(Color.LIGHT_GRAY);
			}
			
			
			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(new Color(235,235,235));
			}
		});
		
		// 아래는 디버깅을 위한 코드, 제대로 생성되었는지 생성되는 라벨 Componenet 의 정보를 표시하게 한다.
		//System.out.println(this);
	}	
	
	//최초 대화방 생성
	public void chat_start_request() {
		String chat_type = "1";
		System.out.println("PersonBox : chat_type : "+chat_type+", user_pk : " + user_pk +", friend_pk : "+friend_pk+", friend_name : "+ friend_name);
		System.out.println("PersonBox가 address_book.mainFrame.client_chat_start_controller를 가지고 있다면 : "+address_book.mainFrame.client_chat_start_controller);
		address_book.mainFrame.client_chat_start_controller.chat_start_request(chat_type, user_pk, friend_pk, friend_name);		
	}
	public void dialogue_request() {	
		String chat_type = "1";
		System.out.println("PersonBox : 이친구와 기존에 방이 있음!!!!!!!!!!!!!!!!!!!!!");
	}
	
	//address_book클래스로 유저가 삭제하려고 선택한 friend_pk값을 넘겨줌
	public void setFriend_pk() {
		address_book.friend_pk = friend_pk;
		address_book.friend_name = friend_name;
		System.out.println("personbox 曰 : setFriend_pk()호출중, 넘겨줄 friend_pk 값 : "+friend_pk);
	}
}
