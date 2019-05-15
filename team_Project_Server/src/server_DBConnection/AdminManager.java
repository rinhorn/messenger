package server_DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 *  DB 접속을 제어하는 admin관리자 클래스
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

	// DB연결
	public void connect() {
		try {
			Class.forName(driver);
			System.out.println("=adminManager driver 로드=");
			con = DriverManager.getConnection(url, user, password);
			if (con == null) { // DB접속 실패시
				JOptionPane.showMessageDialog(this, "관리자에게 문의하세요");
			} else { // DB접속 성공시
				this.setTitle(user + "관리자 접속중");
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// DB연결끊기
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
