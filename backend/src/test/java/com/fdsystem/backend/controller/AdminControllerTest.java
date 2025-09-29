package com.fdsystem.backend.controller;

import com.fdsystem.backend.dto.AdminDashboardDto;
import com.fdsystem.backend.dto.AdminFixedDepositDto;
import com.fdsystem.backend.dto.AdminSupportTicketDto;
import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.enums.FdStatus;
import com.fdsystem.backend.entity.enums.SupportTicketStatus;
import com.fdsystem.backend.service.FixedDepositService;
import com.fdsystem.backend.service.SupportTicketService;
import com.fdsystem.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private FixedDepositService fixedDepositService;

    @Mock
    private SupportTicketService supportTicketService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    @DisplayName("Get FDs - Empty List")
    public void getFds_returnsOkAndEmptyList_whenNoFds() throws Exception {
        when(fixedDepositService.getAllFDs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/fds"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"))
                .andDo(print());

        verify(fixedDepositService, times(1)).getAllFDs();
        verifyNoMoreInteractions(fixedDepositService);
    }
    
    @Test
    @DisplayName("Get FDs - With Data")
    public void getFds_returnsOkAndData_whenFdsExist() throws Exception {
        // Prepare test data
        List<AdminFixedDepositDto> mockFDs = new ArrayList<>();
        
        AdminFixedDepositDto fd1 = new AdminFixedDepositDto();
        fd1.setFdId(1L);
        fd1.setName("John Doe");
        fd1.setEmail("john@example.com");
        fd1.setAmount(10000.0);
        fd1.setInterest_rate(5.5);
        fd1.setMature_date(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)); // 30 days from now
        fd1.setFdStatus(FdStatus.ACTIVE.toString());
        
        AdminFixedDepositDto fd2 = new AdminFixedDepositDto();
        fd2.setFdId(2L);
        fd2.setName("Jane Smith");
        fd2.setEmail("jane@example.com");
        fd2.setAmount(20000.0);
        fd2.setInterest_rate(6.0);
        fd2.setMature_date(new Date(System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000)); // 60 days from now
        fd2.setFdStatus(FdStatus.PENDING.toString());
        
        mockFDs.add(fd1);
        mockFDs.add(fd2);
        
        when(fixedDepositService.getAllFDs()).thenReturn(mockFDs);
        
        // Execute and verify
        mockMvc.perform(get("/admin/fds"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fdId", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].email", is("john@example.com")))
                .andExpect(jsonPath("$[0].amount", is(10000.0)))
                .andExpect(jsonPath("$[0].fdStatus", is("ACTIVE")))
                .andExpect(jsonPath("$[1].fdId", is(2)))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")))
                .andExpect(jsonPath("$[1].amount", is(20000.0)))
                .andExpect(jsonPath("$[1].fdStatus", is("PENDING")))
                .andDo(print());
                
        verify(fixedDepositService, times(1)).getAllFDs();
        verifyNoMoreInteractions(fixedDepositService);
    }

    @Test
    public void setFDStatusById_returnsOk_whenValidStatusProvided() throws Exception {
        long fdId = 10L;
        String json = "{\"status\":\"BROKEN\"}";

        mockMvc.perform(put("/admin/fd/{id}", fdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(fixedDepositService, times(1)).setFixedDepositStatus(eq(fdId), eq(FdStatus.BROKEN));
    }

    @Test
    public void setFDStatusById_returnsBadRequest_whenStatusMissing() throws Exception {
        long fdId = 11L;
        String json = "{}";

        mockMvc.perform(put("/admin/fd/{id}", fdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(fixedDepositService, never()).setFixedDepositStatus(anyLong(), any());
    }

    @Test
    public void setFDStatusById_returnsBadRequest_whenInvalidStatus() throws Exception {
        long fdId = 12L;
        String json = "{\"status\":\"INVALID_STATUS\"}";

        mockMvc.perform(put("/admin/fd/{id}", fdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(fixedDepositService, never()).setFixedDepositStatus(anyLong(), any());
    }

    @Test
    public void getAllTickets_returnsOkAndEmptyList_whenNoTickets() throws Exception {
        when(supportTicketService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));

        verify(supportTicketService, times(1)).getAll();
    }

    @Test
    public void setTicketStatusById_callsServiceAndReturnsOk() throws Exception {
        long ticketId = 5L;
        String responseText = "Resolved by admin";

        mockMvc.perform(post("/admin/tickets/{id}", ticketId)
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(responseText))
                .andExpect(status().isOk());

        verify(supportTicketService, times(1))
                .setTicketStatusById(eq(ticketId), eq(responseText), eq(SupportTicketStatus.CLOSED));
    }

    @Test
    public void getDashBoardInfo_returnsComputedCounts() throws Exception {
        when(supportTicketService.getAllStatusTicketsCount(eq(SupportTicketStatus.OPEN.toString()))).thenReturn(3L);
        when(supportTicketService.getAllStatusTicketsCount(eq(SupportTicketStatus.CLOSED.toString()))).thenReturn(7L);
        when(userService.getAllUsersCount()).thenReturn(42L);
        when(fixedDepositService.getAllFdsCount()).thenReturn(100L);

        mockMvc.perform(get("/admin/dashboard/info"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalOpenTickets").value(3))
                .andExpect(jsonPath("$.totalTickets").value(10))
                .andExpect(jsonPath("$.totalUsers").value(42))
                .andExpect(jsonPath("$.totalFDs").value(100));

        verify(supportTicketService, times(1)).getAllStatusTicketsCount(eq(SupportTicketStatus.OPEN.toString()));
        verify(supportTicketService, times(1)).getAllStatusTicketsCount(eq(SupportTicketStatus.CLOSED.toString()));
        verify(userService, times(1)).getAllUsersCount();
        verify(fixedDepositService, times(1)).getAllFdsCount();
    }
}