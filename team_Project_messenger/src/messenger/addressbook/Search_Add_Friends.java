package messenger.addressbook;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import messenger.MainFrame;
import messenger.tableModel.TableModel;

// 친구 추가 버튼을 눌렀을때 출력되는 window 에 관한 Class
// 주요 기능은
//		1. 전체 회원의 목록을 table 로 출력
//		2. 친구 검색 기능
//		3. 등록 버튼 클릭시 데이터베이스에 친구 관계를 표시하는 Table 에 update
public class Search_Add_Friends extends JFrame {
	public TableModel tableModel;

	JPanel panel_north;
	JPanel panel_south;
	JPanel panel_center;
	JPanel panel_center_inner;

	JTextField t_search_friend_name;

	JTable table;

	JScrollPane scroll;

	JButton bt_search;
	JButton bt_regist;
	JButton bt_cancel;

	Font bt_style = new Font("휴먼매직체", Font.BOLD, 20);

	MainFrame mainFrame;

	// 친구 추가 버튼 클릭시 출력되는 창 중앙의 회원 전체 목록 table 에서 선택한 인원의 primarykey(회원번호) 를 가져와 담는
	// 변수
	String selectedRow_PK;
	String selectedRow_name;

	public Search_Add_Friends(MainFrame mainFrame) {
		this.mainFrame = mainFrame;

		panel_north = new JPanel();
		panel_north.setBackground(new Color(162, 94, 53));
		panel_south = new JPanel();
		panel_south.setBackground(new Color(162, 94, 53));
		panel_center = new JPanel();
		panel_center.setBackground(new Color(162, 94, 53));
		// panel_center.setBackground(Color.BLUE);
		panel_center_inner = new JPanel();
		panel_center_inner.setPreferredSize(new Dimension(450, 425));
		// panel_center_inner.setBackground(Color.BLUE);

		table = new JTable();
		table.setRowHeight(25);
		table.setFont(bt_style);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getSelectedFriend_PK();
				// selfCheck();
			}
		});
		tableModel = new TableModel();
		scroll = new JScrollPane(table);
		// getMember();

		// 친구 검색을 위한 검색어 입력창
		t_search_friend_name = new JTextField(25);
		t_search_friend_name.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					search_friends();
				}
			}
		});

		// 버튼에 관한 설정
		bt_search = new JButton("검색");
		bt_search.setFont(bt_style);
		bt_search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.client_memberList_controller.memberList_request();
				search_friends();
			}
		});

		// 등록 버튼에 관한 설정
		bt_regist = new JButton("등록");
		bt_regist.setFont(bt_style);
		bt_regist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("선택 친구 pk : " + selectedRow_PK);
				int result = JOptionPane.showConfirmDialog(Search_Add_Friends.this, selectedRow_name+"님을 등록 하시겠습니까?");
				if(result==JOptionPane.OK_OPTION) {
					// ♣ 등록 버튼 클릭시 컨트롤러를 통한 JSON 전송 메서드 호출!		--	2019-02-13
					mainFrame.client_addFriend_controller.setFriendPk(selectedRow_PK);
					mainFrame.client_addFriend_controller.addFriend_reqeust();
					Search_Add_Friends.this.setVisible(false);
				}else {
					System.out.println("===친구 추가 취소 누름===");
				}
				selectedRow_name = null;
				selectedRow_PK = null;
			}
		});

		// 취소 버튼에 관한 설정
		bt_cancel = new JButton("취소");
		bt_cancel.setFont(bt_style);
		bt_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close_window(); // 취소 버튼 클릭시 동작하는 method
			}
		});

		// 창을 닫을 시 없애는 것이 아닌 안보이게 처리,,
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				mainFrame.search_Add_Friends.setVisible(false);
			}
		});

		panel_north.add(t_search_friend_name);
		panel_north.add(bt_search);

		panel_center_inner.add(scroll);
		panel_center.add(panel_center_inner);

		panel_south.add(bt_regist);
		panel_south.add(bt_cancel);

		this.add(panel_north, BorderLayout.NORTH);
		this.add(panel_center);
		this.add(panel_south, BorderLayout.SOUTH);
		this.setBounds(650, 300, 500, 550);
		this.setVisible(false);
	}

	public void uploadTable() {
		table.setModel(tableModel);
		table.updateUI();
	}
	////////////////////////////////////////////////////////////////////////////////////////////

	// 친구 찾기 창에서 친구를 검색하는 동작을 진행하는 method
	public void search_friends() {
		int find_friend_row = 0;
		System.out.println("==== search_friends ====");

		String searching_name = t_search_friend_name.getText();

		for (int row = 0; row < tableModel.data.length; row++) {
			for (int col = 0; col < tableModel.columnName.length; col++) {
				Object user_name = table.getValueAt(row, col);
				// System.out.println(user_name);

				if (searching_name.equals(user_name)) {
					find_friend_row = row;

					System.out.println("▶ 찾은 친구의 행번호 : " + find_friend_row);
					System.out.println(table.getValueAt(find_friend_row, 1));

					Object[][] searched_friend_info = remodel_table(find_friend_row);

					tableModel.data = searched_friend_info;
					table.updateUI();
				}
			}
		}
	}

	public Object[][] remodel_table(int find_friend_row) {
		int row = find_friend_row;

		Object[][] searched_friend_info = new Object[1][3];

		for (int col = 0; col < tableModel.columnName.length + 1; col++) {
			searched_friend_info[0][col] = tableModel.data[row][col];
			System.out.println("리모델 될 테이블 모델 값들 : " + searched_friend_info[0][col]);
		}

		return searched_friend_info;
	}

	// 취소 버튼 클릭시 동작하는 method
	public void close_window() {
		System.out.println("==== close_window ====");
		this.setVisible(false);
	}

	// 친구 전체 목록에서 선택한 친구의 Primary_key 를 얻는 method // 친구 추가 controller 에 추가될 method
	public void getSelectedFriend_PK() {
		int selectedRow = table.getSelectedRow();
		int column = 0;
		selectedRow_PK = (String) table.getValueAt(selectedRow, column - 1);
		selectedRow_name = (String) table.getValueAt(selectedRow, column + 1);
		System.out.println("선택한 친구의 pk 는 : " + selectedRow_PK);
		System.out.println("선택한 친구의 이름 은(는) : " + selectedRow_name);

	}

}
