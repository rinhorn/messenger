package messenger.json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONParse_login {

	public static void main(String[] args) {
		// JSON 은 프로그래밍 언어가 아닌 단순한 자바스크립트 형식을 따르는 표기법이다.
		// 따라서 자바 언어에서 JSON 을 다루려면 문자열 취급한다.
		// 자바는 기본적으로 json 을 이해 하지 못한다.
		// 따라서 json 관련 library 를 다운 받아 등록한다.
		
		StringBuffer sb = new StringBuffer();

		// 로그인, 로그아웃, 메세지, 아이템구매, 친구추가
		// requestType : 무엇을 원하는지를 결정하는 변수
		// requestType : ""
		
		sb.append("{");
		sb.append("\"requestType\": \"login\",");
		sb.append("\"id\": \"batman\",");
		sb.append("\"pw\": \"1234\"");
		sb.append("}");
		
		System.out.println(sb.toString());
		// 위 문자열은 현재까지는 문자열이지만
		// 해석(parsing) 한 후에는 json 객체로 취급된다.
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject obj = (JSONObject)parser.parse(sb.toString());
			System.out.println(obj.get("requestType"));
			System.out.println(obj.get("id"));
			System.out.println(obj.get("pw"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	
	}
}
