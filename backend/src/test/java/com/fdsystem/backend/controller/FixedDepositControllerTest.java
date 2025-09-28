package com.fdsystem.backend.controller;

import com.fdsystem.backend.dto.BreakPreviewResponse;
import com.fdsystem.backend.service.FixedDepositService;
import com.fdsystem.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {
        FixedDepositController.class,
        FixedDepositControllerTest.MockConfig.class
})
public class FixedDepositControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(FixedDepositControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FixedDepositService fixedDepositService;

    @Configuration
    static class MockConfig {
        @Bean
        public FixedDepositService fixedDepositService() {
            return Mockito.mock(FixedDepositService.class);
        }

        @Bean
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }

    @Test
    void testBreakFD_returnsCreatedStatus() throws Exception {
        Mockito.doNothing().when(fixedDepositService).breakFD(1L);

        logger.info("Sending POST request to /fd/1/break");
        mockMvc.perform(post("/fd/1/break"))
                .andExpect(status().isCreated());

        logger.info("Verified breakFD(1L) was called");
        Mockito.verify(fixedDepositService).breakFD(1L);
    }

    @Test
    void testGetBreakPreview_returnsCorrectResponse() throws Exception {
        BreakPreviewResponse mockResponse = new BreakPreviewResponse(
                1L, 10000.0, 500.0, null, null, 500.0, 10000.0, 5.0, 12, 2
        );

        Mockito.when(fixedDepositService.getBreakPreview(1L)).thenReturn(mockResponse);

        logger.info("Sending GET request to /fd/1/break-preview");
        mockMvc.perform(get("/fd/1/break-preview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fdId").value(1))
                .andExpect(jsonPath("$.principalAmount").value(10000.0))
                .andExpect(jsonPath("$.penalty").value(500.0))
                .andExpect(jsonPath("$.payout").value(10000.0));

        logger.info("Verified getBreakPreview(1L) returned expected response");
    }
}

