package com.zerobase.account.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zerobase.account.domain.Account;
import com.zerobase.account.domain.Users;
import com.zerobase.account.dto.AccountDto;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findById(Long id);

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByUsers(Users user);

    Optional<List<Account>> findAllByUsers(Users user);
}
