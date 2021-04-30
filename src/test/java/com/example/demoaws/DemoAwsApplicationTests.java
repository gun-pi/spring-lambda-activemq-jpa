package com.example.demoaws;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class DemoAwsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRequest() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/handle")
                .contentType(MediaType.TEXT_PLAIN)
                .content("test"))
                .andReturn();
        Assertions.assertTrue(mockMvc.perform(asyncDispatch(result))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .matches("[-+]?\\d+"));
    }

}
