package messenger.loginForm;
//확장자 파일 이름을 알기위한 클래스
public class StringUtil {
	public static String getExt(String path) {
		int last = path.lastIndexOf(".");
		String ext = path.substring(last+1, path.length());
		return ext;
	}
}
