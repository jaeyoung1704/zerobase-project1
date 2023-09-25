package com.zerobase.account.dto;

import java.time.LocalDateTime;

import com.zerobase.account.type.ErrorCode;
import com.zerobase.account.type.TransactionResult;
import com.zerobase.account.type.TransactionType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 계좌 생성시 요청/응답 객체
 * 
 *
 */
public class CreateTransaction {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
	@NotNull
	@Min(1)
	private Long userId;

	@NotNull
	private String accountNumber;

	@NotNull
	@Min(1)
	private Long transactionValue;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Response {
	private String accountNumber;
	private TransactionType transactionType;
	private TransactionResult transactionResult;
	private ErrorCode errorCode;
	private Long transactionId;
	private Long transactionValue;
	private LocalDateTime occuredAt;

	public static Response from(TransactionDto transactionDto) {
	    return Response.builder()
		.accountNumber(transactionDto.getAccountNumber())
		.transactionType(transactionDto.getTransactionType())
		.transactionResult(transactionDto.getTransactionResult())
		.errorCode(transactionDto.getErrorCode())
		.transactionId(transactionDto.getId())
		.transactionValue(transactionDto.getTransactionValue())
		.occuredAt(transactionDto.getOccuredAt())
		.build();
	}
    }
}
