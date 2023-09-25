package com.zerobase.account.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerobase.account.domain.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

}
