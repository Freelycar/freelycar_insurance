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

@ServerEndpoint("/echosdfsdgerter6665zzz")
public class SocketHelper {

	private static Map<String, Session> sessions = new ConcurrentHashMap<>();

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onopen:" + session);
		//sessions.put(session, VALUE);
	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("message:" + message);
		sessions.put(message, session);//message: phone
	}
	
	
	public static void sendMessage(String phone,String message){
		try {
			for (Map.Entry<String ,Session> m : sessions.entrySet()) {
				if(m.getKey().equals(phone)){
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
				System.out.println("@OnClose:" + remove);
			}
		}
	}
	
	//https://stackoverflow.com/a/33489241
	@OnError
	public void onError(Session session, Throwable thr) {
		
	}
}
