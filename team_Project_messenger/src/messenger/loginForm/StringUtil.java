package messenger.loginForm;
//Ȯ���� ���� �̸��� �˱����� Ŭ����
public class StringUtil {
	public static String getExt(String path) {
		int last = path.lastIndexOf(".");
		String ext = path.substring(last+1, path.length());
		return ext;
	}
}
