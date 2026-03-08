package gds.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketSessionService {
    
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    
    public void registerSession(String username, WebSocketSession session) {
        sessions.put(username, session);
    }
    
    public void removeSession(String username) {
        sessions.remove(username);
    }
    
    public WebSocketSession getSession(String username) {
        return sessions.get(username);
    }
    
    public ConcurrentHashMap.KeySetView<String, WebSocketSession> getAllUsernames() {
        return sessions.keySet();
    }
}
