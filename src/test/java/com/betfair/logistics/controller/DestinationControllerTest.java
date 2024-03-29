package com.betfair.logistics.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class DestinationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createDestination_BlankName() throws Exception {
        //given
        String createPayload = """
            {
                "id": null,
                "name": "",
                "distance": 52
            }""";

        //when
        mockMvc.perform(post("/destinations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createPayload))
                .andExpect(status().isBadRequest());

        //then


    }

}