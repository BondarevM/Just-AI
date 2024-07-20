package com.example.vkBot.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class VkController {

    @Value("${vk.access.token}")
    private String accessToken;

    @Value("${vk.confirmation.token}")
    private String confirmationToken;

    @PostMapping("/")
    public ResponseEntity<String> getMessage(@RequestBody String body) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(body);

            String type = root.path("type").asText();

            if ("confirmation".equals(type)) {
                return ResponseEntity.ok(confirmationToken);
            }

            if ("message_new".equals(type)) {
                JsonNode message = root.path("object").path("message");
                String text = message.path("text").asText();
                int peerId = message.path("peer_id").asInt();

                String responseText = "Вы сказали: " + text;
                sendMessage(peerId, responseText);
            }

            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    private void sendMessage(int peerId, String text) {
        String url = "https://api.vk.com/method/messages.send";
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("v", "5.199");
        params.put("user_id", String.valueOf(peerId));
        params.put("message", text);
        params.put("random_id", String.valueOf(System.currentTimeMillis()));

        restTemplate.getForObject(url + "?access_token={access_token}&v={v}&user_id={user_id}&message={message}&random_id={random_id}", String.class, params);
    }
}
