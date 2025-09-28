package com.fdsystem.backend.service;

import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.enums.FdStatus;
import com.fdsystem.backend.repository.FixedDepositRepository;
import com.fdsystem.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccruedInterestServiceTest {

    @Mock
    private FixedDepositRepository fixedDepositRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccruedInterestService accruedInterestService;

    private User user;
    private FixedDeposit fdMonthly;
    private FixedDeposit fdYearly;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);

        Calendar cal = Calendar.getInstance();

        fdMonthly = new FixedDeposit();
        fdMonthly.setId(1L);
        fdMonthly.setUser(user);
        fdMonthly.setAmount(12000.0);
        fdMonthly.setInterest_rate(6.0);
        fdMonthly.setTenure_months(12);
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -12); // 1 year ago
        fdMonthly.setStart_date(cal.getTime());
        fdMonthly.setStatus(FdStatus.ACTIVE);

        fdYearly = new FixedDeposit();
        fdYearly.setId(2L);
        fdYearly.setUser(user);
        fdYearly.setAmount(15000.0);
        fdYearly.setInterest_rate(7.5);
        fdYearly.setTenure_months(24);
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -24); // 2 years ago
        fdYearly.setStart_date(cal.getTime());
        fdYearly.setStatus(FdStatus.ACTIVE);
    }

    @Test
    void testComputeSimpleInterest_MonthlyAndYearly() {
        double monthlyInterest = accruedInterestService.computeSimpleInterest(12000, 6, 12);
        assertEquals(720.0, monthlyInterest, 0.01);

        double yearlyInterest = accruedInterestService.computeSimpleInterest(15000, 7.5, 24);
        assertEquals(2250.0, yearlyInterest, 0.01);
    }

    @Test
    void testComputeCompoundInterest_MonthlyAndYearly() {
        double monthlyCompound = accruedInterestService.computeCompoundInterest(12000, 6, 12);
        assertTrue(monthlyCompound > 0);

        double yearlyCompound = accruedInterestService.computeCompoundInterest(15000, 7.5, 24);
        assertTrue(yearlyCompound > 0);
    }

    @Test
    void testFDStatusUpdateOnMaturity_MonthlyAndYearly() {
        List<FixedDeposit> fdList = Arrays.asList(fdMonthly, fdYearly);
        when(fixedDepositRepository.findAllByUser(user)).thenReturn(fdList);
        when(fixedDepositRepository.save(any(FixedDeposit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        accruedInterestService.calculateAccruedInterest(user);

        assertEquals(FdStatus.MATURED, fdMonthly.getStatus());
        assertEquals(FdStatus.MATURED, fdYearly.getStatus());
        assertTrue(fdMonthly.getAccrued_interest() > 0);
        assertTrue(fdYearly.getAccrued_interest() > 0);
        verify(fixedDepositRepository, times(2)).save(any(FixedDeposit.class));
    }

    @Test
    void testRoundTwoDecimals() {
        double rounded = accruedInterestService.roundTwoDecimals(123.4567);
        assertEquals(123.46, rounded);
    }
}