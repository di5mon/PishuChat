package gds.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

@Controller
public class ChatController {
    
    @GetMapping("/")
    public String chatPage( Model model) {
        // Получаем имя пользователя из аутентификации
    //    String username = authentication != null ? authentication.getName() : "Гость";
        model.addAttribute("username", "User_" + (int)(Math.random() * 1000));
        return "chat";
    }
    
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        message.setTimestamp(new Date());
        return message;
    }
    
 // Класс для сообщений
    public static class ChatMessage {
        private String sender;
        private String content;
        private Date timestamp;
        
        // Геттеры и сеттеры
        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public Date getTimestamp() { return timestamp; }
        public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    }
}
