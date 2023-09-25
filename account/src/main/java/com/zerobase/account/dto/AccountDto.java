package com.zerobase.account.dto;

import java.time.LocalDateTime;

import com.zerobase.account.domain.Account;
import com.zerobase.account.type.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountDto {
    private Long userId;
    private String accountNumber;
    private Long balance;
    private AccountStatus status;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    public static AccountDto fromEntity(Account account) {
	return AccountDto.builder()
	    .userId(account.getUsers().getId())
	    .accountNumber(account.getAccountNumber())
	    .balance(account.getBalance())
	    .status(account.getAccountStatus())
	    .registeredAt(account.getRegisteredAt())
	    .unRegisteredAt(account.getUnRegisteredAt())
	    .build();
    }

}
