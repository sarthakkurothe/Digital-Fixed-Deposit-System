package com.fdsystem.backend.service;

import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.exception.InterestCalculationException;
import com.fdsystem.backend.repository.FixedDepositRepository;
import com.fdsystem.backend.repository.UserRepository;
import com.fdsystem.backend.entity.enums.FdStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import java.util.List;



/**
 * Service for calculating accrued interest on fixed deposits
 * Handles interest calculations based on various schemes and statuses
 */
@Service
public class AccruedInterestService {
    @Autowired
    private FixedDepositRepository fixedDepositRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Calculates accrued interest for all fixed deposits of a user
     * Updates the interest values and potentially changes FD status to MATURED when applicable
     * 
     * @param user The user whose fixed deposits need interest calculation
     * @throws InterestCalculationException If there are invalid parameters for interest calculation
     */
    public void calculateAccruedInterest(User user) {
        List<FixedDeposit> fixedDeposits = fixedDepositRepository.findAllByUser(user);

        for (FixedDeposit fd : fixedDeposits) {
            double principal = fd.getAmount();
            double rate = fd.getInterest_rate();  // in percent, e.g. 7.0
            int tenureMonths = fd.getTenure_months();

            // Compute elapsed months via java.time
            LocalDate startDate = fd.getStart_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate now = LocalDate.now();
            long monthsElapsed = ChronoUnit.MONTHS.between(startDate, now);
            if (monthsElapsed < 0) monthsElapsed = 0;

            if (principal <= 0) {
                throw new InterestCalculationException("Principal must be positive");
            }
            if (rate < 0) {
                throw new InterestCalculationException("Interest rate must be non-negative");
            }
            if (tenureMonths <= 0) {
                throw new InterestCalculationException("Tenure months must be positive");
            }
            if (monthsElapsed < 0) {
                throw new InterestCalculationException("Elapsed months cannot be negative");
            }

            // Decide how many months to consider
            int applicableMonths = (int) Math.min(monthsElapsed, tenureMonths);

            // Determine whether compounding is eligible
            boolean isCompoundEligible = (tenureMonths >= 24);

            double interest = 0.0;
            double maturityAmount = principal;

            switch (fd.getStatus()) {
//                It is calculated only once , when FD is set to matured status taken from Case Active below
//                case MATURED:
//                      case 1: matured - pay interest for the full tenure (calculate full interest based in interest sceheme)
//


                // broken case is removed because we need to do it only once when user breaks the FD (FixedDepositService.setFixedDepositStatus)
//                case BROKEN:
//                      case 2: broken before 3 months - no interest
//                      case 3: broken after 3 months but before maturity - simple interest for elapsed
//                                   compound is actually compounded quaterly(4times) per year ,  based on scheme whose ternue is >= 24 months
//                                   but if broken before 24 months, then only simple interest is paid for elapsed months
//

                case ACTIVE:
                case PENDING:
                    //case 4: active/pending - simple interest for elapsed months, if tenure >= 24 months, compound interest for elapsed months
                    // also, if elapsed months >= tenure months, set status to matured
                    if (applicableMonths >= tenureMonths) {
                        fd.setStatus(FdStatus.MATURED);
                    }
                    if (isCompoundEligible ) {
                        interest = computeCompoundInterest(principal, rate, applicableMonths);
                    } else {
                        interest = computeSimpleInterest(principal, rate, applicableMonths);
                    }

                    interest = roundTwoDecimals(interest);
                    maturityAmount = roundTwoDecimals(principal + interest);

                    fd.setAccrued_interest(interest);
                    break;

                default:
                    // do nothing
                    continue;
            }


            fixedDepositRepository.save(fd);
        }
    }

    /**
     * Computes simple interest for a given principal, rate and time period
     * 
     * @param principal The principal amount
     * @param ratePercent The annual interest rate as a percentage (e.g., 7.0 for 7%)
     * @param months The time period in months
     * @return The simple interest amount
     */
    public double computeSimpleInterest(double principal, double ratePercent, int months) {
        double years = months / 12.0;
        return (principal * ratePercent * years) / 100.0;
    }

    /**
     * Computes compound interest with quarterly compounding
     * 
     * @param principal The principal amount
     * @param ratePercent The annual interest rate as a percentage (e.g., 7.0 for 7%)
     * @param months The time period in months
     * @return The compound interest amount (maturity amount - principal)
     */
    public double computeCompoundInterest(double principal, double ratePercent, int months) {
        double rateDec = ratePercent / 100.0;
        double years = months / 12.0;
        int n = 4;  // quarterly
        double amount = principal * Math.pow(1 + rateDec / n, n * years);
        return amount - principal;
    }

    /**
     * Rounds a decimal value to two decimal places
     * 
     * @param value The value to round
     * @return The rounded value with two decimal places
     */
    public double roundTwoDecimals(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }



}