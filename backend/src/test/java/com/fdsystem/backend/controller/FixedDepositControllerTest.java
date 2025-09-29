package com.fdsystem.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdsystem.backend.dto.BreakPreviewResponse;
import com.fdsystem.backend.dto.FixedDepositDTO;
import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.enums.FdStatus;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    void testViewFDs_returnsList() throws Exception {
        FixedDeposit fd1 = new FixedDeposit();
        fd1.setAmount(10000.0);
        FixedDeposit fd2 = new FixedDeposit();
        fd2.setAmount(20000.0);

        List<FixedDeposit> fds = Arrays.asList(fd1, fd2);

        Mockito.when(fixedDepositService.getFdsByUserId(1L)).thenReturn(fds);

        logger.info("Sending GET request to /fd/user/1");
        mockMvc.perform(get("/fd/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(10000.0))
                .andExpect(jsonPath("$[1].amount").value(20000.0));
    }

    @Test
    void testBreakFD_returnsCreatedStatus() throws Exception {
        Mockito.doNothing().when(fixedDepositService).breakFD(1L);

        mockMvc.perform(post("/fd/1/break"))
                .andExpect(status().isCreated());

        Mockito.verify(fixedDepositService).breakFD(1L);
    }

    @Test
    void testGetBreakPreview_returnsCorrectResponse() throws Exception {
        BreakPreviewResponse mockResponse = new BreakPreviewResponse(
                1L, 10000.0, 500.0, null, null, 500.0, 10000.0, 5.0, 12, 2
        );

        Mockito.when(fixedDepositService.getBreakPreview(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/fd/1/break-preview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fdId").value(1))
                .andExpect(jsonPath("$.principalAmount").value(10000.0))
                .andExpect(jsonPath("$.penalty").value(500.0))
                .andExpect(jsonPath("$.payout").value(10000.0));
    }

    @Test
    void testViewAllFDs_returnsList() throws Exception {
        FixedDeposit fd = new FixedDeposit();
        fd.setAmount(15000.0);

        Mockito.when(fixedDepositService.getAll()).thenReturn(List.of(fd));

        mockMvc.perform(get("/fd/fds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(15000.0));
    }

    @Test
    void testViewAccruedInterest_returnsDouble() throws Exception {
        FixedDeposit fd = new FixedDeposit();
        fd.setAccrued_interest(750.0);

        Mockito.when(fixedDepositService.getFdById(1L)).thenReturn(fd);

        mockMvc.perform(get("/fd/1/interest"))
                .andExpect(status().isOk())
                .andExpect(content().string("750.0"));
    }

    @Test
    void testUpdateStatus_returnsOk() throws Exception {
        FixedDeposit fd = new FixedDeposit();
        fd.setStatus(FdStatus.ACTIVE);

        Mockito.when(fixedDepositService.getFdById(1L)).thenReturn(fd);
        Mockito.doNothing().when(fixedDepositService).bookFD(Mockito.any(FixedDeposit.class));

        mockMvc.perform(put("/fd/1/status")
                        .param("status", "BROKEN"))
                .andExpect(status().isOk());

        Mockito.verify(fixedDepositService).bookFD(Mockito.any(FixedDeposit.class));
    }

}
