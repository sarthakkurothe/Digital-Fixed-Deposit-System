package com.fdsystem.backend.repository;

import com.fdsystem.backend.entity.FixedDeposit;
import com.fdsystem.backend.entity.User;
import com.fdsystem.backend.entity.enums.FdStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedDepositRepository extends JpaRepository<FixedDeposit,Long> {
    List<FixedDeposit> findAllByUser(User user);
    List<FixedDeposit> findAllByStatus(FdStatus status);
    @Query(value = "select count(*) from fixed_deposits", nativeQuery = true)
    Long countTotalFds();
}
