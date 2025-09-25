package com.fdsystem.backend.service;

import com.fdsystem.backend.model.FixedDeposit;
import com.fdsystem.backend.model.User;
import com.fdsystem.backend.repository.FixedDepositRepository;
import com.fdsystem.backend.repository.UserRepository;
import com.fdsystem.backend.util.enums.FdStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Service public class FixedDepositService {
    private FixedDepositRepository fixedDepositRepository;

    @Autowired
    private UserRepository userRepository;

    public FixedDepositService(FixedDepositRepository fixedDepositRepository){
        this.fixedDepositRepository = fixedDepositRepository;
    }

    public void bookFD(FixedDeposit fixedDeposit){
        int tenure_months=fixedDeposit.getTenure_months();
        Date start_date= Date.valueOf(LocalDate.now());
        Date maturity_date=Date.valueOf(LocalDate.now().plusMonths(tenure_months));
        fixedDeposit.setStart_date(start_date);
        fixedDeposit.setMaturity_date(maturity_date);
        fixedDeposit.setStatus(FdStatus.ACTIVE);
        fixedDeposit.setCreated_at(new Timestamp(System.currentTimeMillis()));
        this.fixedDepositRepository.save(fixedDeposit);
    }

    public List<FixedDeposit> getFdsByUserId(Long user_id){
        User user = userRepository.findById(user_id).get();
        return this.fixedDepositRepository.findAllByUser(user);
    }

    public List<FixedDeposit> getAll(){
        return this.fixedDepositRepository.findAll();
    }

    public  FixedDeposit getFdById(Long fd_id){
        return this.fixedDepositRepository.findById(fd_id).get();
    }
}
