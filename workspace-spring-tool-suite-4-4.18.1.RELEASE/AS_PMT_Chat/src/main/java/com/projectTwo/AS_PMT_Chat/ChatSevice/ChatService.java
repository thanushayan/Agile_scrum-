package com.projectTwo.AS_PMT_Chat.ChatSevice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projectTwo.AS_PMT_Chat.Entity.ChatEntity;
import com.projectTwo.AS_PMT_Chat.Repository.ChatRepository;

@Service
public class ChatService {
    @Autowired
    private ChatRepository chatMessageRepository;

    public List<ChatEntity> getChatMessagesByProjectId(String projectId) {
        return chatMessageRepository.findByProjectId(projectId);
    }

    public ChatEntity saveChatMessage(ChatEntity chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
}
