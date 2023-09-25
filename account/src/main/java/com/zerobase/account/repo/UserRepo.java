package com.zerobase.account.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerobase.account.domain.Users;

public interface UserRepo extends JpaRepository<Users, Long> {

}
