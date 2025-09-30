package com.fdsystem.backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date; import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List; import java.util.Optional;

import com.fdsystem.backend.dto.BreakPreviewResponse;
import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.enums.FdStatus;
import com.fdsystem.backend.repository.FixedDepositRepository;
import com.fdsystem.backend.repository.SupportTicketRepository;
import com.fdsystem.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class FixedDepositServiceTest {

    @Mock
    private FixedDepositRepository fixedDepositRepository;

    @Mock
    private SupportTicketRepository supportTicketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccruedInterestService accruedInterestService;

    @InjectMocks
    private FixedDepositService fixedDepositService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookFD_setsDatesAndStatusAndSaves() {

        // Arrange
        FixedDeposit fd = new FixedDeposit();
        fd.setTenure_months(12);

        // Mock repository save
        when(fixedDepositRepository.save(any(FixedDeposit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        fixedDepositService.bookFD(fd);

        // Assert
        assertNotNull(fd.getStart_date(), "Start date should not be null");
        assertNotNull(fd.getMaturity_date(), "Maturity date should not be null");
        assertEquals(FdStatus.ACTIVE, fd.getStatus(), "FD should be active after booking");
        assertNotNull(fd.getCreated_at(), "Created_at should not be null");

        LocalDate expectedMaturityDate = LocalDate.now().plusMonths(12);
        assertEquals(Date.valueOf(expectedMaturityDate), fd.getMaturity_date(), "Maturity date should be start_date + tenure_months");

        // Verify save called
        verify(fixedDepositRepository, times(1)).save(fd);
    }

    @Test
    void testGetFdsByUserId_returnsDepositsForUser() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        FixedDeposit fd1 = new FixedDeposit();
        fd1.setTenure_months(6);
        FixedDeposit fd2 = new FixedDeposit();
        fd2.setTenure_months(12);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(fixedDepositRepository.findAllByUser(user)).thenReturn(Arrays.asList(fd1, fd2));

        // Act
        List<FixedDeposit> result = fixedDepositService.getFdsByUserId(userId);

        // Assert
        assertEquals(2, result.size(), "Should return 2 fixed deposits");
        verify(userRepository, times(1)).findById(userId);
        verify(fixedDepositRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testGetBreakPreview_penaltyFullAccruedInterestIfLessThan3Months() {
        // Arrange
        FixedDeposit fd = new FixedDeposit();
        fd.setId(1L);
        fd.setStart_date(java.util.Date.from(
                LocalDate.now().minusMonths(2).atStartOfDay(ZoneId.systemDefault()).toInstant()
        ));
        fd.setTenure_months(12);
        fd.setAmount(10000.0);
        fd.setAccrued_interest(500.0);
        fd.setInterest_rate(5.0);

        when(fixedDepositRepository.findById(1L)).thenReturn(Optional.of(fd));

        // Act
        BreakPreviewResponse response = fixedDepositService.getBreakPreview(1L);

        // Assert
        assertEquals(500.0, response.getPenalty(), "Penalty should equal full accrued interest");
        assertEquals(10000.0, response.getPayout(), "Payout should just be the amount");
        assertEquals(2, response.getTimeElapsed(), "Months elapsed should be 2");
    }

    @Test
    void testGetBreakPreview_penaltyPartialIfMidTenure() {
        // Arrange
        FixedDeposit fd = new FixedDeposit();
        fd.setId(2L);
        fd.setStart_date(java.util.Date.from(
                LocalDate.now().minusMonths(6).atStartOfDay(ZoneId.systemDefault()).toInstant()
        ));
        fd.setTenure_months(12);
        fd.setAmount(10000.0);
        fd.setAccrued_interest(600.0);
        fd.setInterest_rate(6.0); // Penalty = accruedInterest * (1 / interestRate) = 600 * (1/6) = 100

        when(fixedDepositRepository.findById(2L)).thenReturn(Optional.of(fd));

        // Act
        BreakPreviewResponse response = fixedDepositService.getBreakPreview(2L);

        // Assert
        assertEquals(100.0, response.getPenalty(), "Penalty should be accrued interest divided by interest rate");
        assertEquals(10500.0, response.getPayout(), "Payout should be amount + interest - penalty");
        assertEquals(6, response.getTimeElapsed(), "Months elapsed should be 6");
    }
    

    @Test
    void testGetAll_returnsAllFDs() {
        FixedDeposit fd1 = new FixedDeposit();
        fd1.setId(1L);
        FixedDeposit fd2 = new FixedDeposit();
        fd2.setId(2L);

        when(fixedDepositRepository.findAll()).thenReturn(Arrays.asList(fd1, fd2));

        List<FixedDeposit> result = fixedDepositService.getAll();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(fixedDepositRepository, times(1)).findAll();
    }

    @Test
    void testGetFdById_returnsCorrectFD() {
        FixedDeposit fd = new FixedDeposit();
        fd.setId(5L);
        when(fixedDepositRepository.findById(5L)).thenReturn(Optional.of(fd));

        FixedDeposit result = fixedDepositService.getFdById(5L);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        verify(fixedDepositRepository, times(1)).findById(5L);
    }

    @Test
    void testGetAllFDs_convertsEntitiesToDtos() {
        User user = new User();
        user.setName("Lakshmi");
        user.setEmail("lakshmi@example.com");

        FixedDeposit fd = new FixedDeposit();
        fd.setId(10L);
        fd.setAmount(50000.0);
        fd.setInterest_rate(6.5);
        fd.setUser(user);
        fd.setMaturity_date(Date.valueOf(LocalDate.now().plusMonths(12)));
        fd.setStatus(FdStatus.ACTIVE);

        when(fixedDepositRepository.findAll()).thenReturn(List.of(fd));

        var dtos = fixedDepositService.getAllFDs();

        assertEquals(1, dtos.size());
        assertEquals(10L, dtos.get(0).getFdId());
        assertEquals("Lakshmi", dtos.get(0).getName());
        assertEquals("lakshmi@example.com", dtos.get(0).getEmail());
        assertEquals("ACTIVE", dtos.get(0).getFdStatus());
        verify(fixedDepositRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFdsCount_returnsCorrectCount() {
        when(fixedDepositRepository.countTotalFds()).thenReturn(42L);

        long result = fixedDepositService.getAllFdsCount();

        assertEquals(42L, result);
        verify(fixedDepositRepository, times(1)).countTotalFds();
    }
    
    @Test
    void testBreakFD_createsTicketAndChangesFDStatus() {
        // Arrange
        Long fdId = 3L;
        
        // Create test data
        User testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        
        FixedDeposit fd = new FixedDeposit();
        fd.setId(fdId);
        fd.setUser(testUser);
        fd.setAmount(10000.0);
        fd.setInterest_rate(5.0);
        fd.setTenure_months(12);
        fd.setStatus(FdStatus.ACTIVE);
        fd.setStart_date(java.util.Date.from(
                LocalDate.now().minusMonths(4).atStartOfDay(ZoneId.systemDefault()).toInstant()
        ));
        
        // Mock repository responses
        when(fixedDepositRepository.findById(fdId)).thenReturn(Optional.of(fd));
        when(supportTicketRepository.save(any(com.fdsystem.backend.entity.SupportTicket.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(fixedDepositRepository.save(any(FixedDeposit.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
            
        // Act
        fixedDepositService.breakFD(fdId);
        
        // Assert
        // Verify FD status changed to PENDING
        assertEquals(FdStatus.PENDING, fd.getStatus(), "FD status should be changed to PENDING");
        
        // Verify support ticket was created
        verify(supportTicketRepository, times(1)).save(argThat(ticket -> {
            return ticket.getUser().equals(testUser) &&
                   ticket.getFixedDeposit().equals(fd) &&
                   ticket.getSubject().equals("Break Fixed Deposit") &&
                   ticket.getDescription().equals("Premature Withdrawal") &&
                   ticket.getStatus().equals(com.fdsystem.backend.entity.enums.SupportTicketStatus.OPEN);
        }));

        verify(fixedDepositRepository, times(1)).save(fd);
    }

    @Test
    void testSetFixedDepositStatus_CompoundInterestPath() {
        // Arrange
        Long fdId = 99L;
        double principal = 50000.0;
        double rate = 7.5;
        int tenureMonths = 36;
        int applicableMonths = 30;
        double compoundInterest = 5000.0;
        double roundedInterest = 5000.0;

        User user = new User();
        user.setId(1L);
        FixedDeposit fd = new FixedDeposit();
        fd.setId(fdId);
        fd.setUser(user);
        fd.setAmount(principal);
        fd.setInterest_rate(rate);
        fd.setTenure_months(tenureMonths);
        fd.setStatus(FdStatus.ACTIVE);
        fd.setStart_date(java.util.Date.from(
                LocalDate.now().minusMonths(applicableMonths).atStartOfDay(ZoneId.systemDefault()).toInstant()
        ));

        when(fixedDepositRepository.findById(fdId)).thenReturn(Optional.of(fd));
        when(accruedInterestService.computeCompoundInterest(principal, rate, applicableMonths)).thenReturn(compoundInterest);
        when(accruedInterestService.roundTwoDecimals(compoundInterest)).thenReturn(roundedInterest);
        when(fixedDepositRepository.save(any(FixedDeposit.class))).thenReturn(fd);

        // Act
        fixedDepositService.setFixedDepositStatus(fdId, FdStatus.BROKEN);

        // Assert
        assertEquals(FdStatus.BROKEN, fd.getStatus());
        assertEquals(roundedInterest, fd.getAccrued_interest());
        verify(accruedInterestService, times(1)).computeCompoundInterest(principal, rate, applicableMonths);
        verify(accruedInterestService, times(1)).roundTwoDecimals(compoundInterest);
        verify(fixedDepositRepository, times(1)).save(fd);
    }

}
