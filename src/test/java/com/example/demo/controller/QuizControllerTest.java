package com.example.demo.controller;

import com.example.demo.dto.SubmittedUserSolutionDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void 정상적인_Post_요청시_서버로부터_상태코드200을_받는다() throws Exception{
        String json = mapper.writeValueAsString(new SubmittedUserSolutionDto(1L, 1L, "3", true));
        String URL = "/quiz/user/solution";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(URL).contentType(MediaType.APPLICATION_JSON).content(json);
        mockMvc.perform(requestBuilder).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }
}
