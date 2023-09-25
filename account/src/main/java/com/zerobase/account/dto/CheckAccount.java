package com.zerobase.account.dto;

import java.util.List;

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
public class CheckAccount {
    @Getter
    @Setter
    public static class Request {
	@NotNull
	@Min(1)
	private Long userId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
	List<AccountDto> accountDtoList;
    }
}
