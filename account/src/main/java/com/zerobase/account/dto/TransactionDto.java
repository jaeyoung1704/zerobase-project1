package com.zerobase.account.dto;

import java.time.LocalDateTime;

import com.zerobase.account.domain.Transaction;
import com.zerobase.account.type.ErrorCode;
import com.zerobase.account.type.TransactionResult;
import com.zerobase.account.type.TransactionType;

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
public class TransactionDto {
    private Long id;
    private Long userId;
    private String accountNumber;
    private Long transactionValue;

    private TransactionResult transactionResult;
    private TransactionType transactionType;
    private ErrorCode errorCode;
    private LocalDateTime occuredAt;

    public static TransactionDto fromEntity(Transaction transaction) {
	return TransactionDto.builder()
	    .id(transaction.getId())
	    .userId(transaction.getUserId())
	    .accountNumber(transaction.getAccountNumber())
	    .transactionValue(transaction.getTransactionValue())
	    .transactionResult(transaction.getTransactionResult())
	    .transactionType(transaction.getTransactionType())
	    .errorCode(transaction.getErrorCode())
	    .occuredAt(transaction.getOccuredAt())
	    .build();
    }

}
