package messenger.json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONParse_login {

	public static void main(String[] args) {
		// JSON �� ���α׷��� �� �ƴ� �ܼ��� �ڹٽ�ũ��Ʈ ������ ������ ǥ����̴�.
		// ���� �ڹ� ���� JSON �� �ٷ���� ���ڿ� ����Ѵ�.
		// �ڹٴ� �⺻������ json �� ���� ���� ���Ѵ�.
		// ���� json ���� library �� �ٿ� �޾� ����Ѵ�.
		
		StringBuffer sb = new StringBuffer();

		// �α���, �α׾ƿ�, �޼���, �����۱���, ģ���߰�
		// requestType : ������ ���ϴ����� �����ϴ� ����
		// requestType : ""
		
		sb.append("{");
		sb.append("\"requestType\": \"login\",");
		sb.append("\"id\": \"batman\",");
		sb.append("\"pw\": \"1234\"");
		sb.append("}");
		
		System.out.println(sb.toString());
		// �� ���ڿ��� ��������� ���ڿ�������
		// �ؼ�(parsing) �� �Ŀ��� json ��ü�� ��޵ȴ�.
		
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
