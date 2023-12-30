package com.projectTwo.AS_PMT_Chat.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.projectTwo.AS_PMT_Chat.ChatSevice.ChatService;
import com.projectTwo.AS_PMT_Chat.Entity.ChatEntity;
import com.projectTwo.AS_PMT_Chat.Repository.ChatRepository;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Allow cross-origin requests from your frontend app
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping("/{projectId}")
    public List<ChatEntity> getChatMessages(@PathVariable String projectId) {
        return chatService.getChatMessagesByProjectId(projectId);
    }

    @PostMapping
    public ChatEntity saveChatMessage(@RequestBody ChatEntity chatMessage) {
        return chatService.saveChatMessage(chatMessage);
    }
}
