package server_DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 *  DB ������ �����ϴ� admin������ Ŭ����
 * */
public class AdminManager extends JFrame {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@localhost:1521:XE";
	// String url = "jdbc:oracle:thin:@localhost:1521:XE";
	String user = "admin";
	String password = "1234";
	public Connection con;

	public AdminManager() {
		connect();
	}

	// DB����
	public void connect() {
		try {
			Class.forName(driver);
			System.out.println("=adminManager driver �ε�=");
			con = DriverManager.getConnection(url, user, password);
			if (con == null) { // DB���� ���н�
				JOptionPane.showMessageDialog(this, "�����ڿ��� �����ϼ���");
			} else { // DB���� ������
				this.setTitle(user + "������ ������");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// DB�������
	public void disconnect() {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
