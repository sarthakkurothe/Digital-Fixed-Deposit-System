package com.fdsystem.backend.controller;

import com.fdsystem.backend.dto.UserDashboardDTO;
import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.UserPrincipal;
import com.fdsystem.backend.entity.enums.FdStatus;
import com.fdsystem.backend.entity.enums.Role;
import com.fdsystem.backend.service.AccruedInterestService;
import com.fdsystem.backend.service.FixedDepositService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FixedDepositService fixedDepositService;
    
    @Mock
    private AccruedInterestService accruedInterestService;
    
    @Mock(lenient = true)
    private Authentication authentication;
    
    @Mock(lenient = true)
    private SecurityContext securityContext;
    
    @InjectMocks
    private UserController userController;
    
    // Test data
    private User testUser;
    private List<FixedDeposit> fixedDeposits;
    private UserPrincipal userPrincipal;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
        

        testUser = createTestUser();
        userPrincipal = new UserPrincipal(testUser);
        fixedDeposits = new ArrayList<>();
        
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);
    }
    
    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }
    
    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Role.ROLE_USER);
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
        user.setDateOfBirth(new java.sql.Date(System.currentTimeMillis() - 25L * 365 * 24 * 60 * 60 * 1000)); // 25 years ago
        return user;
    }
    
    private FixedDeposit createFixedDeposit(long id, double amount, double accruedInterest, FdStatus status) {
        FixedDeposit fd = new FixedDeposit();
        fd.setId(id);
        fd.setAmount(amount);
        fd.setAccrued_interest(accruedInterest);
        fd.setStatus(status);
        fd.setUser(testUser);
        return fd;
    }

    @Test
    @DisplayName("Test getDashboardStats with active fixed deposits returns correct statistics")
    public void testGetDashboardStats_WithActiveFixedDeposits_ReturnsCorrectStats() throws Exception {
        fixedDeposits.add(createFixedDeposit(1L, 10000.0, 500.0, FdStatus.ACTIVE));
        fixedDeposits.add(createFixedDeposit(2L, 20000.0, 1000.0, FdStatus.ACTIVE));
        fixedDeposits.add(createFixedDeposit(3L, 15000.0, 300.0, FdStatus.PENDING));
        
        // Expected values
        double expectedTotalInvestment = 45000.0; // 10000 + 20000 + 15000
        double expectedInterestEarned = 1800.0;   // 500 + 1000 + 300
        long expectedActiveFDs = 2;               // 2 active FDs
        // The controller calculates average interest as (total interest earned / active FDs)
        // Not just the interest from active FDs
        double expectedAverageInterest = 900.0; // 1800.0 / 2
        
        when(fixedDepositService.getFdsByUserId(testUser.getId())).thenReturn(fixedDeposits);
        doNothing().when(accruedInterestService).calculateAccruedInterest(testUser);
        
        mockMvc.perform(get("/user/investments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.totalInvestment").value(expectedTotalInvestment))
                .andExpect(jsonPath("$.interestEarned").value(expectedInterestEarned))
                .andExpect(jsonPath("$.activeFDs").value(expectedActiveFDs))
                .andExpect(jsonPath("$.averageInterest").value(expectedAverageInterest));
        
        verify(fixedDepositService, times(3)).getFdsByUserId(testUser.getId());
        verify(accruedInterestService).calculateAccruedInterest(testUser);
    }
    
    @Test
    @DisplayName("Test getDashboardStats with no active fixed deposits returns zero average interest")
    public void testGetDashboardStats_WithNoActiveFDs_ReturnsZeroAverageInterest() throws Exception {
        fixedDeposits.add(createFixedDeposit(1L, 10000.0, 500.0, FdStatus.PENDING));
        fixedDeposits.add(createFixedDeposit(2L, 20000.0, 1000.0, FdStatus.BROKEN));
        
        double expectedTotalInvestment = 30000.0; // 10000 + 20000
        double expectedInterestEarned = 1500.0;   // 500 + 1000
        long expectedActiveFDs = 0;               // No active FDs
        double expectedAverageInterest = 0.0;     // No active FDs, so average is 0
        
        when(fixedDepositService.getFdsByUserId(testUser.getId())).thenReturn(fixedDeposits);
        doNothing().when(accruedInterestService).calculateAccruedInterest(testUser);
        
        mockMvc.perform(get("/user/investments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.totalInvestment").value(expectedTotalInvestment))
                .andExpect(jsonPath("$.interestEarned").value(expectedInterestEarned))
                .andExpect(jsonPath("$.activeFDs").value(expectedActiveFDs))
                .andExpect(jsonPath("$.averageInterest").value(expectedAverageInterest));
        
        verify(fixedDepositService, times(3)).getFdsByUserId(testUser.getId());
        verify(accruedInterestService).calculateAccruedInterest(testUser);
    }
    
    @Test
    @DisplayName("Test getDashboardStats with no fixed deposits returns empty statistics")
    public void testGetDashboardStats_WithNoFDs_ReturnsEmptyStats() throws Exception {

        double expectedTotalInvestment = 0.0;
        double expectedInterestEarned = 0.0;
        long expectedActiveFDs = 0;
        double expectedAverageInterest = 0.0;
        
        when(fixedDepositService.getFdsByUserId(testUser.getId())).thenReturn(fixedDeposits);
        doNothing().when(accruedInterestService).calculateAccruedInterest(testUser);
        
        mockMvc.perform(get("/user/investments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.totalInvestment").value(expectedTotalInvestment))
                .andExpect(jsonPath("$.interestEarned").value(expectedInterestEarned))
                .andExpect(jsonPath("$.activeFDs").value(expectedActiveFDs))
                .andExpect(jsonPath("$.averageInterest").value(expectedAverageInterest));
        
        verify(fixedDepositService, times(3)).getFdsByUserId(testUser.getId());
        verify(accruedInterestService).calculateAccruedInterest(testUser);
    }
    
    @Test
    @DisplayName("Test getDashboardStats with authentication failure")
    public void testGetDashboardStats_WithAuthenticationFailure() {

        SecurityContext testSecurityContext = mock(SecurityContext.class);
        Authentication testAuthentication = mock(Authentication.class);
        
        when(testSecurityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(testSecurityContext);
        
        try {
            userController.getDashboardStats();
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException ||
                      e.getCause() instanceof NullPointerException ||
                      e.getMessage() != null && e.getMessage().contains("Authentication"));
        } finally {
            SecurityContextHolder.clearContext();
        }
        
        verify(fixedDepositService, never()).getFdsByUserId(anyLong());
        verify(accruedInterestService, never()).calculateAccruedInterest(any(User.class));
    }
    
    @Test
    @DisplayName("Test getDashboardStats with direct method invocation")
    public void testGetDashboardStats_DirectMethodInvocation() {
        fixedDeposits.add(createFixedDeposit(1L, 10000.0, 500.0, FdStatus.ACTIVE));
        fixedDeposits.add(createFixedDeposit(2L, 20000.0, 1000.0, FdStatus.ACTIVE));
        
        when(fixedDepositService.getFdsByUserId(testUser.getId())).thenReturn(fixedDeposits);
        doNothing().when(accruedInterestService).calculateAccruedInterest(testUser);
        

        ResponseEntity<?> responseEntity = userController.getDashboardStats();
        
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof UserDashboardDTO);
        
        UserDashboardDTO dashboardDTO = (UserDashboardDTO) responseEntity.getBody();
        assertEquals(30000.0, dashboardDTO.getTotalInvestment());
        assertEquals(1500.0, dashboardDTO.getInterestEarned());
        assertEquals(2, dashboardDTO.getActiveFDs());
        double expectedAverageInterest = 750.0; // 1500.0 / 2
        assertEquals(expectedAverageInterest, dashboardDTO.getAverageInterest(), 
                    "Average interest should be " + expectedAverageInterest + " (total interest / active FDs)");
        
        verify(fixedDepositService, times(3)).getFdsByUserId(testUser.getId());
        verify(accruedInterestService).calculateAccruedInterest(testUser);
    }
}