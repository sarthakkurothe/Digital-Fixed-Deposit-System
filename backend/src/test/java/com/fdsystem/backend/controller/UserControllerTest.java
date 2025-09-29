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
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        
        // Set up test data
        testUser = createTestUser();
        userPrincipal = new UserPrincipal(testUser);
        fixedDeposits = new ArrayList<>();
        
        // Set up mock MVC
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        
        // Set up security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        SecurityContextHolder.setContext(securityContext);
    }
    
    @AfterEach
    public void tearDown() {
        // Clear SecurityContextHolder after each test
        SecurityContextHolder.clearContext();
    }
    
    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setAge(30);
        user.setRole(Role.ROLE_USER);
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
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
        // Arrange: Set up fixed deposits with different statuses
        fixedDeposits.add(createFixedDeposit(1L, 10000.0, 500.0, FdStatus.ACTIVE));
        fixedDeposits.add(createFixedDeposit(2L, 20000.0, 1000.0, FdStatus.ACTIVE));
        fixedDeposits.add(createFixedDeposit(3L, 15000.0, 300.0, FdStatus.PENDING));
        
        // Expected values
        double expectedTotalInvestment = 45000.0; // 10000 + 20000 + 15000
        double expectedInterestEarned = 1800.0;   // 500 + 1000 + 300
        long expectedActiveFDs = 2;               // 2 active FDs
        double expectedAverageInterest = 900.0;   // 1800 / 2 (total interest / active FDs count)
        
        // Mock service calls
        when(fixedDepositService.getFdsByUserId(testUser.getId())).thenReturn(fixedDeposits);
        doNothing().when(accruedInterestService).calculateAccruedInterest(testUser);
        
        // Act & Assert using MockMvc
        mockMvc.perform(get("/user/investments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.totalInvestment").value(expectedTotalInvestment))
                .andExpect(jsonPath("$.interestEarned").value(expectedInterestEarned))
                .andExpect(jsonPath("$.activeFDs").value(expectedActiveFDs))
                .andExpect(jsonPath("$.averageInterest").value(expectedAverageInterest));
        
        // Verify service method calls
        verify(fixedDepositService, times(3)).getFdsByUserId(testUser.getId());
        verify(accruedInterestService).calculateAccruedInterest(testUser);
    }
    
    @Test
    @DisplayName("Test getDashboardStats with no active fixed deposits returns zero average interest")
    public void testGetDashboardStats_WithNoActiveFDs_ReturnsZeroAverageInterest() throws Exception {
        // Arrange: Set up fixed deposits with non-active statuses
        fixedDeposits.add(createFixedDeposit(1L, 10000.0, 500.0, FdStatus.PENDING));
        fixedDeposits.add(createFixedDeposit(2L, 20000.0, 1000.0, FdStatus.BROKEN));
        
        // Expected values
        double expectedTotalInvestment = 30000.0; // 10000 + 20000
        double expectedInterestEarned = 1500.0;   // 500 + 1000
        long expectedActiveFDs = 0;               // No active FDs
        double expectedAverageInterest = 0.0;     // No active FDs, so average is 0
        
        // Mock service calls
        when(fixedDepositService.getFdsByUserId(testUser.getId())).thenReturn(fixedDeposits);
        doNothing().when(accruedInterestService).calculateAccruedInterest(testUser);
        
        // Act & Assert using MockMvc
        mockMvc.perform(get("/user/investments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.totalInvestment").value(expectedTotalInvestment))
                .andExpect(jsonPath("$.interestEarned").value(expectedInterestEarned))
                .andExpect(jsonPath("$.activeFDs").value(expectedActiveFDs))
                .andExpect(jsonPath("$.averageInterest").value(expectedAverageInterest));
        
        // Verify service method calls
        verify(fixedDepositService, times(3)).getFdsByUserId(testUser.getId());
        verify(accruedInterestService).calculateAccruedInterest(testUser);
    }
    
    @Test
    @DisplayName("Test getDashboardStats with no fixed deposits returns empty statistics")
    public void testGetDashboardStats_WithNoFDs_ReturnsEmptyStats() throws Exception {
        // Arrange: Set up empty fixed deposits list
        // Empty fixedDeposits list is already initialized in setup()
        
        // Expected values
        double expectedTotalInvestment = 0.0;
        double expectedInterestEarned = 0.0;
        long expectedActiveFDs = 0;
        double expectedAverageInterest = 0.0;
        
        // Mock service calls
        when(fixedDepositService.getFdsByUserId(testUser.getId())).thenReturn(fixedDeposits);
        doNothing().when(accruedInterestService).calculateAccruedInterest(testUser);
        
        // Act & Assert using MockMvc
        mockMvc.perform(get("/user/investments")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.totalInvestment").value(expectedTotalInvestment))
                .andExpect(jsonPath("$.interestEarned").value(expectedInterestEarned))
                .andExpect(jsonPath("$.activeFDs").value(expectedActiveFDs))
                .andExpect(jsonPath("$.averageInterest").value(expectedAverageInterest));
        
        // Verify service method calls
        verify(fixedDepositService, times(3)).getFdsByUserId(testUser.getId());
        verify(accruedInterestService).calculateAccruedInterest(testUser);
    }
    
    @Test
    @DisplayName("Test getDashboardStats with authentication failure")
    public void testGetDashboardStats_WithAuthenticationFailure() {
        // Arrange: Set up authentication failure
        // Create a fresh security context for this test only
        SecurityContext testSecurityContext = mock(SecurityContext.class);
        Authentication testAuthentication = mock(Authentication.class);
        
        // Set null authentication to simulate failure
        when(testSecurityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(testSecurityContext);
        
        // Act & Assert using direct controller call to catch the exception
        try {
            // Direct call to controller method instead of using MockMvc
            userController.getDashboardStats();
            fail("Expected exception was not thrown");
        } catch (Exception e) {
            // Expected behavior - we should get an exception when authentication fails
            assertTrue(e instanceof NullPointerException || 
                      e.getCause() instanceof NullPointerException ||
                      e.getMessage() != null && e.getMessage().contains("Authentication"));
        } finally {
            // Clean up test-specific security context
            SecurityContextHolder.clearContext();
        }
        
        // Verify that service methods were not called
        verify(fixedDepositService, never()).getFdsByUserId(anyLong());
        verify(accruedInterestService, never()).calculateAccruedInterest(any(User.class));
    }
    
    @Test
    @DisplayName("Test getDashboardStats with direct method invocation")
    public void testGetDashboardStats_DirectMethodInvocation() {
        // Arrange
        fixedDeposits.add(createFixedDeposit(1L, 10000.0, 500.0, FdStatus.ACTIVE));
        fixedDeposits.add(createFixedDeposit(2L, 20000.0, 1000.0, FdStatus.ACTIVE));
        
        // Mock service calls
        when(fixedDepositService.getFdsByUserId(testUser.getId())).thenReturn(fixedDeposits);
        doNothing().when(accruedInterestService).calculateAccruedInterest(testUser);
        
        // Act
        ResponseEntity<?> responseEntity = userController.getDashboardStats();
        
        // Assert
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody() instanceof UserDashboardDTO);
        
        UserDashboardDTO dashboardDTO = (UserDashboardDTO) responseEntity.getBody();
        assertEquals(30000.0, dashboardDTO.getTotalInvestment());
        assertEquals(1500.0, dashboardDTO.getInterestEarned());
        assertEquals(2, dashboardDTO.getActiveFDs());
        assertEquals(750.0, dashboardDTO.getAverageInterest(), "Average interest should be 750.0 (1500/2)");
        
        // Verify service method calls
        verify(fixedDepositService, times(3)).getFdsByUserId(testUser.getId());
        verify(accruedInterestService).calculateAccruedInterest(testUser);
    }
}