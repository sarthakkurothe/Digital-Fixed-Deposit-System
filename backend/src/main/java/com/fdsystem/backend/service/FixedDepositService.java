package com.fdsystem.backend.service;

import com.fdsystem.backend.dto.AdminFixedDepositDto;
import com.fdsystem.backend.dto.BreakPreviewResponse;
import com.fdsystem.backend.dto.FixedDepositDTO;
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
import java.util.ArrayList;
import java.util.List;

@Service public class FixedDepositService {

    private final FixedDepositRepository fixedDepositRepository;
    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;
    private final AccruedInterestService accruedInterestService;

    @Autowired
    public FixedDepositService(FixedDepositRepository fixedDepositRepository,
                               SupportTicketRepository supportTicketRepository,
                               UserRepository userRepository,
                               AccruedInterestService accruedInterestService) {
        this.fixedDepositRepository = fixedDepositRepository;
        this.supportTicketRepository = supportTicketRepository;
        this.userRepository = userRepository;
        this.accruedInterestService = accruedInterestService;
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
        ticket.setCreatedDate(Date.valueOf(LocalDate.now()));
        supportTicketRepository.save(ticket);
        fd.setStatus(FdStatus.PENDING);
        fixedDepositRepository.save(fd);
    }

    public BreakPreviewResponse getBreakPreview(Long fdId) {
        FixedDeposit fd = this.fixedDepositRepository.findById(fdId).get();
        LocalDate startDate = fd.getStart_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now();
        long monthsElapsed = ChronoUnit.MONTHS.between(startDate, today);
        double accruedInterest = fd.getAccrued_interest();
        double penalty = 0;
        if (monthsElapsed < 3) {
            penalty = accruedInterest;
        } else if (monthsElapsed < fd.getTenure_months()) {
            penalty = accruedInterest * ( 1 / fd.getInterest_rate());
            penalty = Math.round(penalty * 100.0) / 100.0;
        }
        double payout = fd.getAmount() + fd.getAccrued_interest() - penalty;
        return new BreakPreviewResponse(fd.getId(), fd.getAmount(), fd.getAccrued_interest(), fd.getStart_date(), fd.getMaturity_date(), penalty, payout, fd.getInterest_rate(), fd.getTenure_months(), monthsElapsed);
    }

    public void setFixedDepositStatus(Long id, FdStatus fdStatus) {
        FixedDeposit fixedDeposit = this.fixedDepositRepository.findById(id).get();
        fixedDeposit.setStatus(fdStatus);

        // logic to calculate interest and maturity amount based on status change
        LocalDate startDate = fixedDeposit.getStart_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        long monthsElapsed = ChronoUnit.MONTHS.between(startDate, now);
        if (monthsElapsed < 0) monthsElapsed = 0;
        double principal = fixedDeposit.getAmount();
        double rate = fixedDeposit.getInterest_rate();  // in percent, e.g. 7.0
        int tenureMonths = fixedDeposit.getTenure_months();
        double interest = 0.0;
        boolean isCompoundEligible = (tenureMonths >= 24);
        int applicableMonths = (int) Math.min(monthsElapsed, tenureMonths);

        if (applicableMonths < 3) {
            interest = 0.0;
        } else {
            // compound is actually compounded quaterly(4times) per year ,  based on scheme whose ternue is >= 24 months
            // but if broken before 24 months, then only simple interest is paid for elapsed months
            if (isCompoundEligible && applicableMonths >= 24) {
                interest = accruedInterestService.computeCompoundInterest(principal, rate, applicableMonths);
            } else {
                interest = accruedInterestService.computeSimpleInterest(principal, rate, applicableMonths);
            }
        }

        interest = accruedInterestService.roundTwoDecimals(interest);
        fixedDeposit.setAccrued_interest(interest);
        // accured interest is calculated with same interets , but payout is reduced by penalty

        this.fixedDepositRepository.save(fixedDeposit);

    }

    public List<AdminFixedDepositDto> getAllFDs() {
        List<FixedDeposit> allFds = this.fixedDepositRepository.findAll();
        List<AdminFixedDepositDto> allFdsDto = new ArrayList<>();
        for(FixedDeposit fixedDeposit: allFds){
            AdminFixedDepositDto fixedDepositDto = new AdminFixedDepositDto();
            fixedDepositDto.setFdId(fixedDeposit.getId());
            fixedDepositDto.setAmount(fixedDeposit.getAmount());
            fixedDepositDto.setName(fixedDeposit.getUser().getName());
            fixedDepositDto.setInterest_rate(fixedDeposit.getInterest_rate());
            fixedDepositDto.setEmail(fixedDeposit.getUser().getEmail());
            fixedDepositDto.setMature_date(fixedDeposit.getMaturity_date());
            fixedDepositDto.setFdStatus(fixedDeposit.getStatus().toString());
            allFdsDto.add(fixedDepositDto);
        }

        return allFdsDto;
    }

    public long getAllFdsCount(){
        return this.fixedDepositRepository.countTotalFds();
    }
}
