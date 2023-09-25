package com.zerobase.account.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 계좌 생성시 요청/응답 객체
 * 
 *
 */
public class DeleteAccount {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
	@NotNull
	@Min(1)
	private Long userId;

	@NotNull
	@Min(1000000000)
	private Long accountId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
	private Long userId;
	private String accountNumber;
	private LocalDateTime unRegisteredAt;

	public static Response from(AccountDto accountDto) {
	    return Response.builder()
		.userId(accountDto.getUserId())
		.accountNumber(accountDto.getAccountNumber())
		.unRegisteredAt(accountDto.getRegisteredAt())
		.build();
	}
    }
}
