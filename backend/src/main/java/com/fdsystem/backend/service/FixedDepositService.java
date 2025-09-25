package com.fdsystem.backend.service;

import com.fdsystem.backend.dto.BreakPreviewResponse;
import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.SupportTicket;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.repository.FixedDepositRepository;
import com.fdsystem.backend.repository.SupportTicketRepository;
import com.fdsystem.backend.repository.UserRepository;
import com.fdsystem.backend.entity.enums.FdStatus;
import com.fdsystem.backend.entity.enums.SupportTicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service public class FixedDepositService {

    private FixedDepositRepository fixedDepositRepository;
    @Autowired
    private SupportTicketRepository supportTicketRepository;

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

    public void breakFD(Long fdId) {
        FixedDeposit fd = fixedDepositRepository.findById(fdId).get();
        SupportTicket ticket = new SupportTicket();
        ticket.setUser(fd.getUser());
        ticket.setFixedDeposit(fd);
        ticket.setSubject("Break Fixed Deposit");
        ticket.setDescription("Premature Withdrawal");
        ticket.setStatus(SupportTicketStatus.OPEN);
        supportTicketRepository.save(ticket);
        fd.setStatus(FdStatus.PENDING);
        fixedDepositRepository.save(fd);
    }

    public BreakPreviewResponse getBreakPreview(Long fdId) {
        FixedDeposit fd = this.fixedDepositRepository.findById(fdId).get();
        double penalty = calculatePenalty(fd);
        double payout = fd.getAmount() + fd.getAccrued_interest() - penalty;
        return new BreakPreviewResponse(fd.getId(), fd.getAmount(), fd.getAccrued_interest(), fd.getStart_date(), fd.getMaturity_date(), penalty, payout, fd.getStatus().toString());
    }

    private double calculatePenalty(FixedDeposit fd) {
        LocalDate startDate = fd.getStart_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        long monthsElapsed = ChronoUnit.MONTHS.between(startDate, today);
        double accruedInterest = fd.getAccrued_interest();
        double penalty = 0;
        if (monthsElapsed < 3) {
            penalty = accruedInterest;
        } else if (monthsElapsed < fd.getTenure_months()) {
            penalty = accruedInterest * 0.01;
        }
        return penalty;
    }

    public List<FixedDeposit> getAllFDsByStatus(FdStatus fdStatus){
        return this.fixedDepositRepository.findAllByStatus(fdStatus);
    }

    public void setFixedDepositStatus(long id, FdStatus fdStatus){
        FixedDeposit fixedDeposit = this.fixedDepositRepository.findById(id).get();
        fixedDeposit.setStatus(fdStatus);
    }
}
