package com.freelycar.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

@ServerEndpoint("/echo")
public class SocketHelper {

	private static Map<String, Session> sessions = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session) {
		//System.out.println("onopen:" + session);
		try {
			session.getBasicRemote().sendText("{\"msg\":\"success\"}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		//System.out.println("message:" + message);
		if(!message.startsWith("{")){
			return;
		}
		
		JSONObject json = null;
		try {
			 json = new JSONObject(message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//System.out.println("加入socket openId"+json.getString("openId"));
		sessions.put(json.getString("openId"), session);//message: phone
	}
	
	
	public static void sendMessage(String openId,String message){
		try {
			for (Map.Entry<String ,Session> m : sessions.entrySet()) {
				if(m.getKey().equals(openId)){
					//System.out.println("发送openId"+openId+"的推送");
					m.getValue().getBasicRemote().sendText(message);
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(Session session) {
		for (Map.Entry<String ,Session> m : sessions.entrySet()) {
			if(m.getValue() == session){
				Session remove = sessions.remove(m.getKey());
				//System.out.println("@OnClose:" + remove);
			}
		}
	}
	
	//https://stackoverflow.com/a/33489241
	@OnError
	public void onError(Session session, Throwable thr) {
		
	}
}
