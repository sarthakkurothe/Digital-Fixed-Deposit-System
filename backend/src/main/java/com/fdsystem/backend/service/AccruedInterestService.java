package com.fdsystem.backend.service;

import com.fdsystem.backend.model.FixedDeposit;
import com.fdsystem.backend.model.User;
import com.fdsystem.backend.repository.FixedDepositRepository;
import com.fdsystem.backend.repository.UserRepository;
import com.fdsystem.backend.util.enums.FdStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class AccruedInterestService {
    @Autowired
    private FixedDepositRepository fixedDepositRepository;

    @Autowired
    private UserRepository userRepository;

    public void calculateAccruedInterest(User user) {
        List<FixedDeposit> fixedDeposits = fixedDepositRepository.findAllByUser(user);

        for (FixedDeposit fd : fixedDeposits) {
            double interest = 0.0;
            double principal = fd.getAmount();
            double rate = fd.getInterest_rate();
            int tenure = fd.getTenure_months();

            switch (fd.getStatus()) {
                case MATURED:
                    // Case 1: Calculate for full tenure
                    interest = (principal * rate * tenure) / (100 * 12);
                    if (tenure >= 24) {
                        interest += (interest * rate * (tenure / 12)) / 100;
                    }
                    break;

                case BROKEN:
                    // Case 2: If broken in less than 3 months, interest is 0
                    int monthsElapsed = (int) ((System.currentTimeMillis() - fd.getStart_date().getTime()) / (1000L * 60 * 60 * 24 * 30));
                    if (monthsElapsed < 3) {
                        interest = 0.0;
                    } else {
                        interest = (principal * rate * monthsElapsed) / (100 * 12);
                        if (monthsElapsed >= 24) {
                            interest += (interest * rate * (monthsElapsed / 12)) / 100;
                        }
                    }
                    break;

                case ACTIVE:
                case PENDING:
                    // Case 3: Calculate till date

                    monthsElapsed = (int) ((System.currentTimeMillis() - fd.getStart_date().getTime()) / (1000L * 60 * 60 * 24 * 30));

                    int applicableMonths = Math.min(tenure, monthsElapsed);

                    //adding new logic to set status to matured if tenure is completed
                    if (monthsElapsed >= tenure) {
                        fd.setStatus(FdStatus.MATURED);
                    }

                    interest = (principal * rate * applicableMonths) / (100 * 12);
                    if (applicableMonths >= 24) {
                        interest += (interest * rate * (applicableMonths / 12)) / 100;
                    }
                    break;

                default:
                    // Other statuses, do nothing
                    continue;
            }

            fd.setAccrued_interest(interest);
            fixedDepositRepository.save(fd);
        }
    }
}