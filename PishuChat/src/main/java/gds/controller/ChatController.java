package gds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {
    
    private final List<String> onlineUsers = new CopyOnWriteArrayList<>();
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @GetMapping("/")
    public String chatPage(Model model) {
        String username = "User_" + (int)(Math.random() * 1000);
        model.addAttribute("username", username);
        return "chat";
    }
    
    // Присоединение пользователя
    @MessageMapping("/chat.join")
    public void joinChat(@Payload ChatMessage message) {
        onlineUsers.add(message.getSender());
        broadcastOnlineUsers();
    }
    
    // Отправка сообщения
    @MessageMapping("/chat.send")
    @SendTo("/topic/chat/general")  // Укажите конкретный topic
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        message.setTimestamp(new Date());
        message.setType("CHAT");
        return message;
    }
    
    // Обработка набора текста
    @MessageMapping("/chat.typing")
    @SendTo("/topic/typing/general")
    public ChatMessage handleTyping(@Payload ChatMessage message) {
        message.setTimestamp(new Date());
        return message;
    }
    
    // Выход пользователя
    @MessageMapping("/chat.leave")
    public void leaveChat(@Payload ChatMessage message) {
        onlineUsers.remove(message.getSender());
        broadcastOnlineUsers();
    }
    
    @MessageMapping("/chat.send.{roomId}")
    @SendTo("/topic/chat.{roomId}")
    public ChatMessage sendMessageToRoom(@DestinationVariable String roomId, @Payload ChatMessage message) {
        message.setTimestamp(new Date());
        return message;
    }
    
    private void broadcastOnlineUsers() {
    	List<String> usersCopy = new ArrayList<>(onlineUsers);
        messagingTemplate.convertAndSend("/topic/online", usersCopy);
    }
    
    public static class ChatMessage {
        private String sender;
        private String content;
        private Date timestamp;
        private String room;
        private String type;
        private boolean typing;
        
        // Геттеры и сеттеры
        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public Date getTimestamp() { return timestamp; }
        public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
        
        public String getRoom() { return room; }
        public void setRoom(String room) { this.room = room; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public boolean isTyping() { return typing; }
        public void setTyping(boolean typing) { this.typing = typing; }
    }
}