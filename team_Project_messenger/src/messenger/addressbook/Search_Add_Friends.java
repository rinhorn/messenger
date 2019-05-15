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

// ģ�� �߰� ��ư�� �������� ��µǴ� window �� ���� Class
// �ֿ� �����
//		1. ��ü ȸ���� ����� table �� ���
//		2. ģ�� �˻� ���
//		3. ��� ��ư Ŭ���� �����ͺ��̽��� ģ�� ���踦 ǥ���ϴ� Table �� update
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

	Font bt_style = new Font("�޸ո���ü", Font.BOLD, 20);

	MainFrame mainFrame;

	// ģ�� �߰� ��ư Ŭ���� ��µǴ� â �߾��� ȸ�� ��ü ��� table ���� ������ �ο��� primarykey(ȸ����ȣ) �� ������ ���
	// ����
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

		// ģ�� �˻��� ���� �˻��� �Է�â
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

		// ��ư�� ���� ����
		bt_search = new JButton("�˻�");
		bt_search.setFont(bt_style);
		bt_search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mainFrame.client_memberList_controller.memberList_request();
				search_friends();
			}
		});

		// ��� ��ư�� ���� ����
		bt_regist = new JButton("���");
		bt_regist.setFont(bt_style);
		bt_regist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("���� ģ�� pk : " + selectedRow_PK);
				int result = JOptionPane.showConfirmDialog(Search_Add_Friends.this, selectedRow_name+"���� ��� �Ͻðڽ��ϱ�?");
				if(result==JOptionPane.OK_OPTION) {
					// �� ��� ��ư Ŭ���� ��Ʈ�ѷ��� ���� JSON ���� �޼��� ȣ��!		--	2019-02-13
					mainFrame.client_addFriend_controller.setFriendPk(selectedRow_PK);
					mainFrame.client_addFriend_controller.addFriend_reqeust();
					Search_Add_Friends.this.setVisible(false);
				}else {
					System.out.println("===ģ�� �߰� ��� ����===");
				}
				selectedRow_name = null;
				selectedRow_PK = null;
			}
		});

		// ��� ��ư�� ���� ����
		bt_cancel = new JButton("���");
		bt_cancel.setFont(bt_style);
		bt_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close_window(); // ��� ��ư Ŭ���� �����ϴ� method
			}
		});

		// â�� ���� �� ���ִ� ���� �ƴ� �Ⱥ��̰� ó��,,
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

	// ģ�� ã�� â���� ģ���� �˻��ϴ� ������ �����ϴ� method
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

					System.out.println("�� ã�� ģ���� ���ȣ : " + find_friend_row);
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
			System.out.println("���� �� ���̺� �� ���� : " + searched_friend_info[0][col]);
		}

		return searched_friend_info;
	}

	// ��� ��ư Ŭ���� �����ϴ� method
	public void close_window() {
		System.out.println("==== close_window ====");
		this.setVisible(false);
	}

	// ģ�� ��ü ��Ͽ��� ������ ģ���� Primary_key �� ��� method // ģ�� �߰� controller �� �߰��� method
	public void getSelectedFriend_PK() {
		int selectedRow = table.getSelectedRow();
		int column = 0;
		selectedRow_PK = (String) table.getValueAt(selectedRow, column - 1);
		selectedRow_name = (String) table.getValueAt(selectedRow, column + 1);
		System.out.println("������ ģ���� pk �� : " + selectedRow_PK);
		System.out.println("������ ģ���� �̸� ��(��) : " + selectedRow_name);

	}

}
