package com.fdsystem.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdsystem.backend.dto.BreakPreviewResponse;
import com.fdsystem.backend.dto.FixedDepositDTO;
import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.UserPrincipal;
import com.fdsystem.backend.entity.enums.FdStatus;
import com.fdsystem.backend.entity.enums.Role;
import com.fdsystem.backend.service.FixedDepositService;
import com.fdsystem.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FixedDepositControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(FixedDepositControllerTest.class);

    private MockMvc mockMvc;

    @Mock
    private FixedDepositService fixedDepositService;

    @Mock
    private UserService userService;
    
    @Mock(lenient = true)
    private Authentication authentication;
    
    @Mock(lenient = true)
    private SecurityContext securityContext;

    @InjectMocks
    private FixedDepositController fixedDepositController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // Test data
    private User testUser;
    private UserPrincipal userPrincipal;

    @BeforeEach
    public void setup() {
        // Initialize test user and principal
        testUser = createTestUser();
        userPrincipal = new UserPrincipal(testUser);
        
        // Setup mockMvc
        mockMvc = MockMvcBuilders
                .standaloneSetup(fixedDepositController)
                .build();
        
        // Setup security context with lenient stubs
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);
    }
    
    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Role.ROLE_USER);
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
        // Add date of birth to avoid NullPointerException in other parts of the code
        user.setDateOfBirth(new java.sql.Date(System.currentTimeMillis() - 25L * 365 * 24 * 60 * 60 * 1000)); // 25 years ago
        return user;
    }


    @Test
    @DisplayName("View Fixed Deposits by User ID")
    void testViewFDs_returnsList() throws Exception {
        // Arrange
        FixedDeposit fd1 = new FixedDeposit();
        fd1.setAmount(10000.0);
        
        FixedDeposit fd2 = new FixedDeposit();
        fd2.setAmount(20000.0);

        List<FixedDeposit> fds = Arrays.asList(fd1, fd2);

        when(fixedDepositService.getFdsByUserId(1L)).thenReturn(fds);

        // Act & Assert
        logger.info("Sending GET request to /fd/user/1");
        mockMvc.perform(get("/fd/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(10000.0))
                .andExpect(jsonPath("$[1].amount").value(20000.0));
                
        // Verify service was called
        Mockito.verify(fixedDepositService).getFdsByUserId(1L);
    }

    @Test
    @DisplayName("Break Fixed Deposit")
    void testBreakFD_returnsCreatedStatus() throws Exception {
        // Arrange
        Mockito.doNothing().when(fixedDepositService).breakFD(1L);

        // Act & Assert
        mockMvc.perform(post("/fd/1/break"))
                .andExpect(status().isCreated());

        // Verify service was called
        Mockito.verify(fixedDepositService).breakFD(1L);
    }

    @Test
    @DisplayName("Get Break Preview")
    void testGetBreakPreview_returnsCorrectResponse() throws Exception {
        // Arrange
        BreakPreviewResponse mockResponse = new BreakPreviewResponse(
                1L, 10000.0, 500.0, null, null, 500.0, 10000.0, 5.0, 12, 2
        );

        when(fixedDepositService.getBreakPreview(1L)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(get("/fd/1/break-preview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fdId").value(1))
                .andExpect(jsonPath("$.principalAmount").value(10000.0))
                .andExpect(jsonPath("$.penalty").value(500.0))
                .andExpect(jsonPath("$.payout").value(10000.0));
                
        // Verify service was called
        Mockito.verify(fixedDepositService).getBreakPreview(1L);
    }

    @Test
    @DisplayName("View All Fixed Deposits")
    void testViewAllFDs_returnsList() throws Exception {
        // Arrange
        FixedDeposit fd = new FixedDeposit();
        fd.setAmount(15000.0);

        when(fixedDepositService.getAll()).thenReturn(List.of(fd));

        // Act & Assert
        mockMvc.perform(get("/fd/fds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(15000.0));
                
        // Verify service was called
        Mockito.verify(fixedDepositService).getAll();
    }

    @Test
    @DisplayName("View Accrued Interest")
    void testViewAccruedInterest_returnsDouble() throws Exception {
        // Arrange
        FixedDeposit fd = new FixedDeposit();
        fd.setAccrued_interest(750.0);

        when(fixedDepositService.getFdById(1L)).thenReturn(fd);

        // Act & Assert
        mockMvc.perform(get("/fd/1/interest"))
                .andExpect(status().isOk())
                .andExpect(content().string("750.0"));
                
        // Verify service was called
        Mockito.verify(fixedDepositService).getFdById(1L);
    }

    @Test
    @DisplayName("Update FD Status")
    void testUpdateStatus_returnsOk() throws Exception {
        // Arrange
        FixedDeposit fd = new FixedDeposit();
        fd.setStatus(FdStatus.ACTIVE);

        when(fixedDepositService.getFdById(1L)).thenReturn(fd);
        Mockito.doNothing().when(fixedDepositService).bookFD(any(FixedDeposit.class));

        // Act & Assert
        mockMvc.perform(put("/fd/1/status")
                        .param("status", "BROKEN"))
                .andExpect(status().isOk());

        // Verify service was called
        Mockito.verify(fixedDepositService).getFdById(1L);
        Mockito.verify(fixedDepositService).bookFD(any(FixedDeposit.class));
    }
    
    @Test
    @DisplayName("Create New Fixed Deposit")
    void testBookFD_returnsCreatedStatus() throws Exception {
        // Arrange
        FixedDepositDTO fdDTO = new FixedDepositDTO();
        fdDTO.setAmount(50000.0);
        fdDTO.setInterest_rate(5.5);
        fdDTO.setTenure_months(12);
        fdDTO.setUser_id(1L); // Set the user ID
        
        FixedDeposit savedFd = new FixedDeposit();
        savedFd.setId(5L);
        savedFd.setAmount(50000.0);
        savedFd.setUser(testUser);
        savedFd.setStatus(FdStatus.PENDING);
        
        // Mock the getUserById method to return the test user
        when(userService.getUserById(1L)).thenReturn(java.util.Optional.of(testUser));
        Mockito.doNothing().when(fixedDepositService).bookFD(any(FixedDeposit.class));
        
        // Act & Assert
        mockMvc.perform(post("/fd/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fdDTO)))
                .andExpect(status().isCreated());
                
        // Verify service was called
        Mockito.verify(userService).getUserById(1L);
        Mockito.verify(fixedDepositService).bookFD(any(FixedDeposit.class));
    }
}
