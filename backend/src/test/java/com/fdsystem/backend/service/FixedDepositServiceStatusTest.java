package com.fdsystem.backend.service;

import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.enums.FdStatus;
import com.fdsystem.backend.repository.FixedDepositRepository;
import com.fdsystem.backend.repository.SupportTicketRepository;
import com.fdsystem.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FixedDepositServiceStatusTest {

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

    private FixedDeposit fd;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        fd = new FixedDeposit();
        fd.setId(200L);
        fd.setUser(user);
        fd.setAmount(15000.0);
        fd.setInterest_rate(6.0);
        fd.setTenure_months(12);
        // default start date will be overridden in each test
        fd.setStatus(FdStatus.ACTIVE);
        fd.setAccrued_interest(0.0);

        when(fixedDepositRepository.save(any(FixedDeposit.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void testBrokenStatusBeforeThreeMonths_NoInterest() {
        fd.setStart_date(Date.from(LocalDate.now().minusMonths(2).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(fixedDepositRepository.findById(200L)).thenReturn(Optional.of(fd));

        when(accruedInterestService.roundTwoDecimals(0.0)).thenReturn(0.0);

        fixedDepositService.setFixedDepositStatus(200L, FdStatus.BROKEN);

        assertEquals(FdStatus.BROKEN, fd.getStatus());
        assertEquals(0.0, fd.getAccrued_interest());

        verify(accruedInterestService).roundTwoDecimals(0.0);
        verify(accruedInterestService, never()).computeSimpleInterest(anyDouble(), anyDouble(), anyInt());
        verify(fixedDepositRepository).save(fd);
    }

    @Test
    void testBrokenAtHalfTenure_PaysHalfInterest() {
        // arrange: tenure is 12 months, break at month 6 -> service computes interest using 6 months
        fd.setStart_date(Date.from(LocalDate.now().minusMonths(6).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        when(fixedDepositRepository.findById(200L)).thenReturn(Optional.of(fd));

        double sixMonthInterest = 540.0; // expected interest for 6 months (half of 1080)
        when(accruedInterestService.computeSimpleInterest(15000.0, 6.0, 6)).thenReturn(sixMonthInterest);
        when(accruedInterestService.roundTwoDecimals(sixMonthInterest)).thenReturn(sixMonthInterest);

        fixedDepositService.setFixedDepositStatus(200L, FdStatus.BROKEN);

        assertEquals(FdStatus.BROKEN, fd.getStatus());
        assertEquals(sixMonthInterest, fd.getAccrued_interest());

        verify(accruedInterestService).computeSimpleInterest(15000.0, 6.0, 6);
        verify(accruedInterestService).roundTwoDecimals(sixMonthInterest);
        verify(fixedDepositRepository).save(fd);
    }
}