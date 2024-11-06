package com.hackathon.hackathon.chatGpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chat-gpt")
public class ChatController {

    private final ChatgptService chatgptService;

    public ChatController(ChatgptService chatgptService) {
        this.chatgptService = chatgptService;
    }

    @PostMapping("")
    public String getResponse(@RequestBody String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "모든 답변을 한국어로 해주세요."),
                        Map.of("role", "user", "content", message)
                )
        ));
        return chatgptService.getChatResponse(message);
    }
}
