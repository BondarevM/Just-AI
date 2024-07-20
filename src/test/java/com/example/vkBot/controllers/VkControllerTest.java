package com.example.vkBot.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VkController.class)
public class VkControllerTest {

    @Value("${vk.confirmation.token}")
    private String confirmationToken;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getMessage_ShouldHaveStatusOk_AndHaveConfirmationToken_InConfirmationCase() throws Exception {
        String requestBody = "{\"type\":\"confirmation\"}";
        String expectedResponse = confirmationToken;

        MvcResult result = mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void getMessage_ShouldHaveStatusOk_ExpectedBody_InNewMessageCase() throws Exception {
        String requestBody = "{\"type\":\"message_new\",\"object\":{\"message\":{\"text\":\"hello\",\"peer_id\":12345}}}";
        String expectedResponse = "ok";

        MvcResult result = mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        assertEquals(expectedResponse, actualResponse);
    }
}
