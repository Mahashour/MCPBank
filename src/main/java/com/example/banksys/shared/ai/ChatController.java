package com.example.banksys.shared.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider){
        this.chatClient = builder.defaultTools(toolCallbackProvider).build();
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String userMessage){
        return chatClient.prompt(userMessage).call().content();
    }
}
