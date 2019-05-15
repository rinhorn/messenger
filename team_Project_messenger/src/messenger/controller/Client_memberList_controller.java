package messenger.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import messenger.MainFrame;
import messenger.loginForm.LoginForm;
import messenger.loginForm.Register;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

//클라이언트측 '아이디중복'확인 관련 컨트롤러

public class Client_memberList_controller {
	
	// server 로 부터 받은 전체 member 이차원 배열 data 를 담아 놓을 Object 이차원배열 변수
	Object[][] model;
	
	//서버가 보내준 response 정보
	String flag;
	
	//JSON관련 변수
	String request_msg;
	BufferedWriter buffw;
	
	Socket client;
	Register register;
	MainFrame mainFrame;
	
	
	public Client_memberList_controller(Socket client, MainFrame mainFrame) {
		this.client = client;
		this.mainFrame = mainFrame;
	}
	
	//★ 클라이언트부터 요청받은 [requestType] JSOM작성하여 서버로 전송
	
	public void memberList_request() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\" : \"getMembers\"");
		sb.append("}");
		request_msg = sb.toString();
		System.out.println("Client_memberList_controller曰 : request_msg : "+request_msg);	
		try {
			buffw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
			buffw.write(request_msg+"\n");
			buffw.flush();
			System.out.println("전송 완료");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//★ 서버로부터 넘겨받은 [responseType] JSON 파싱
	
	public void memberList_parser(JsonElement obj) {
		// 친구추가 버튼 클릭시 모든 멤버를 출력하기 위한 메서드
			System.out.println("===== Get ALL Member answer recived=====");
			JsonElement dataElement = obj.getAsJsonObject().get("tableModel");
			JsonArray data = dataElement.getAsJsonArray();
			Gson gson = new Gson();
			model = gson.fromJson(data, Object[][].class);
			
			System.out.println("▶ ClientThread 가 받은 JSON : " + data);
			
			//모든 멤버에 대한 Json 파싱 후 바로 모델에 대입해주기!
			mainFrame.search_Add_Friends.tableModel.data = model;
	}
	
	// TableModel 에 대입시킬 데이터 얻어올 메서드
	public Object[][] getModel(){
		return model;
	}
}
