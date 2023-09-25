package com.zerobase.account.controller;

import com.zerobase.account.domain.Account;
import com.zerobase.account.dto.AccountDto;
import com.zerobase.account.repo.AccountRepo;
import com.zerobase.account.service.AccountService;
import com.zerobase.account.type.AccountStatus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Mock
    private AccountRepo accountRepo;

    @InjectMocks
    private AccountService accountService;

    @Test
    @DisplayName("계좌 조회 성공")
    void testXXX() {
	// given
	given(accountRepo.findById(anyLong())).willReturn(Optional.of(Account.builder()
	    .accountStatus(AccountStatus.UNREGISTERED)
	    .accountNumber("65789")
	    .build()));
	ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);

	// when
	AccountDto account = accountService.getAccount(4555L).get(0);

	// then
	verify(accountRepo, times(1)).findById(captor.capture());
	verify(accountRepo, times(0)).save(any());
	assertEquals(4555L, captor.getValue());
	assertNotEquals(45515L, captor.getValue());
	assertEquals("65789", account.getAccountNumber());
    }

    @Test
    @DisplayName("계좌 조회 실패 - 음수로 조회")
    void testFailedToSearchAccount() {
	// given
	// when
	RuntimeException exception =
	    assertThrows(RuntimeException.class, () -> accountService.getAccount(-10L));

	// then
	assertEquals("Minus", exception.getMessage());
    }

    @Test
    @DisplayName("Test 이름 변경")
    void testGetAccount() {
	// given
	given(accountRepo.findById(anyLong())).willReturn(Optional.of(Account.builder()
	    .accountStatus(AccountStatus.UNREGISTERED)
	    .accountNumber("65789")
	    .build()));

	// when
	AccountDto account = accountService.getAccount(4555L).get(0);

	// then
	assertEquals("65789", account.getAccountNumber());
//	assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
    }

    @Test
    void testGetAccount2() {
	// given
	given(accountRepo.findById(anyLong())).willReturn(Optional.of(Account.builder()
	    .accountStatus(AccountStatus.UNREGISTERED)
	    .accountNumber("65789")
	    .build()));

	// when
	AccountDto account = accountService.getAccount(4555L).get(0);

	// then
	assertEquals("65789", account.getAccountNumber());
//	assertEquals(AccountStatus.UNREGISTERED, account.getAccountStatus());
    }
}
