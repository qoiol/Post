package com.example.postservice.controller;

import com.example.postservice.controller.request.LoginRequest;
import com.example.postservice.controller.request.PostCreateRequest;
import com.example.postservice.controller.response.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @MockitoBean
//    private PostService postService;

    @Test
    @WithMockUser(username = "test")
    void 포스트작성() throws Exception {
        String title = "title";
        String content = "content";

        MvcResult test = mockMvc.perform(
                post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new LoginRequest("test", "1234")))
                )
                .andDo(print())
                .andReturn();

        LoginResponse loginResponse = objectMapper.readValue("{"+ test.getResponse().getContentAsString().split("\\{")[2].replaceFirst("}", ""), LoginResponse.class);

        mockMvc.perform(
                        post("/api/v1/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, content)))
                                .header("Authorization", "Bearer " + loginResponse.getToken())
                ).andDo(print())
                .andExpect(status().isOk());
    }
}
