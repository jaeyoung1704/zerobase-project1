package com.zerobase.account.exception;

import com.zerobase.account.type.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMsg;

    public AccountException(ErrorCode errorCode) {
	this.errorCode = errorCode;
	errorMsg = errorCode.getDescription();
    }

}
