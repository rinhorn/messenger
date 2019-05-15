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

// ��ϵ� �ɹ��� address_book �� ǥ���� personBox
// Class �� ���� ��ӹ޾� �� ��ü�� �󺧷� �ǰ� �Ѵ�. �� ���� personBox�� ���̵Ǵ°�
public class PersonBox extends JLabel{
	public Chat_Window chat_window;
	Address_book address_book;
	int personBox_width = address_book.PANEL_BOTTOM_WIDTH;
	int personBox_height = 50;

	//���õ� personBox�� ���۷����� ���� ����.
	public String personBox_name;
	public String friendID;  
	public int click_count;
	boolean click_flag;
	Client_Thread chat_thread;
	MainFrame mainFrame;
	String user_pk;
	String friend_pk, friend_name; // frineds_pk�� friends_name �迭�� �Ѹ� �Ѹ�
	
	//chat_window ����Ʈ ����
	ArrayList<Chat_Window> chat_window_array = new ArrayList<Chat_Window>();
	
	//������ ������ ģ���� �ѹ��� ��ȭ���� ������ ���� ���ٸ� �ʱⰪ�� null��
	//����, ������ ������ ģ���� ��ȭ �õ��ÿ�(=�̹� ä�ù��� �����Ǿ��ִ� ����) �� room_num���� ���� ������ �ÿ� ���� room_num���� �Է��� �ش�
	public String room_num = null; 
	
	public PersonBox(Address_book address_book, String user_pk, String friend_pk, String friend_name) {
		this.user_pk = user_pk;
		this.friend_pk = friend_pk;
		this.friend_name = friend_name;
		this.address_book = address_book;
		System.out.println("�� PersonBox ���� ���� address_book : "+this.address_book);
		System.out.println("user_pk,friend_pk,friend_name : "+user_pk+","+friend_pk+","+friend_name);
		
		// personBox�� �̸� ó���� Ŭ���� ������ ���ش�.
		this.setText((String)friend_name);
		// personBox�� �⺻�� �����ϱ� ������ ������ó��
		this.setOpaque(true);
		
		// personBox �ȿ� ����� ������ font style �� ũ�⸦ ����
		this.setFont(new Font("���", Font.BOLD, 15));
		
		// personBox�� ������ ����
		this.setBackground(new Color(235,235,235));
		
		// personBox �� size �� �����Ѵ�.
		this.setPreferredSize(new Dimension(personBox_width, personBox_height));
		
		// personBox �� Ŭ���Ͽ����� ������ ����
		// �� project ������ personBox �� Ŭ���� ä��â�� �� �� �ְ� �Ѵ�.
		// ���� Ŭ�� �̺�Ʈ�� ActionListener �� ����ϳ� �󺧿����� �������� �ʱ� ������ mouseListener �� ���
		// mouseListener �� ������ (Overide) �ؾ� �� �߻� method �� ���� ������ �ʿ��Ѱ͸� ����ϱ� ���� Adapter �� ���
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				click_count++;
				Object click_obj = e.getSource();
				PersonBox currentObj = (PersonBox)click_obj;
				System.out.println("�� Ŭ���� ģ�� �̸� : "+currentObj.getText());
				System.out.println("�� Ŭ���� ģ��_pk : "+friend_pk);
				//System.out.println(currentObj.personBox_name);
				//System.out.println(this);
				//static ���� selectedBox �� ���� ���õ� PersonBox�� ����.

				
				// �Ʒ��� ����Ŭ���� ������ Logic
				if(e.getClickCount()%2 == 0) {
					// PersonBox �� Ŭ���Ͽ� ä��â�� �����ٸ� thread ���� ä��â�� ���� ����� �˷���
					//chat_thread.select_opend_window = true;
					
					if(room_num == null) {
						System.out.println("Address_book : �ѹ��̶� �� ģ���� ��ȭ������ ������ �� ���ȣ�� null�̾�ߵǴµ� �׷���? : "+room_num);
						chat_start_request(); //���� ��ȭ�� ����
					}else {
						System.out.println("Address_book : �ѹ��̶� �� ģ���� ��ȭ������ ������ �� ���ȣ��  : "+room_num);
						dialogue_request(); //������ ��ȭ ���� ���
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
		
		// �Ʒ��� ������� ���� �ڵ�, ����� �����Ǿ����� �����Ǵ� �� Componenet �� ������ ǥ���ϰ� �Ѵ�.
		//System.out.println(this);
	}	
	
	//���� ��ȭ�� ����
	public void chat_start_request() {
		String chat_type = "1";
		System.out.println("PersonBox : chat_type : "+chat_type+", user_pk : " + user_pk +", friend_pk : "+friend_pk+", friend_name : "+ friend_name);
		System.out.println("PersonBox�� address_book.mainFrame.client_chat_start_controller�� ������ �ִٸ� : "+address_book.mainFrame.client_chat_start_controller);
		address_book.mainFrame.client_chat_start_controller.chat_start_request(chat_type, user_pk, friend_pk, friend_name);		
	}
	public void dialogue_request() {	
		String chat_type = "1";
		System.out.println("PersonBox : ��ģ���� ������ ���� ����!!!!!!!!!!!!!!!!!!!!!");
	}
	
	//address_bookŬ������ ������ �����Ϸ��� ������ friend_pk���� �Ѱ���
	public void setFriend_pk() {
		address_book.friend_pk = friend_pk;
		address_book.friend_name = friend_name;
		System.out.println("personbox �� : setFriend_pk()ȣ����, �Ѱ��� friend_pk �� : "+friend_pk);
	}
}
