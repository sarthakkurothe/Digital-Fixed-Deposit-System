package com.fdsystem.backend.repository;

import com.fdsystem.backend.model.FixedDeposit;
import com.fdsystem.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedDepositRepository extends JpaRepository<FixedDeposit,Long> {
    List<FixedDeposit> findAllByUser(User user);
}
