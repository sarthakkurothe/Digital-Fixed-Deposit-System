package com.fdsystem.backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date; import java.sql.Timestamp;
import java.time.LocalDate; import java.util.Arrays;
import java.util.List; import java.util.Optional;

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
}
