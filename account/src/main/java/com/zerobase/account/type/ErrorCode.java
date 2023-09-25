package com.zerobase.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("해당 사용자가 없습니다. 더미사용자(1~5)를 이용해주세요."),
    INITIAL_BALANCE("초기 잔액은 0 이상으로 입력해주세요 "),
    ACCOUNT_OVER_10("해당 사용자의 최대 보유 가능 계좌수를 초과했습니다"),
    ACCOUNT_NOT_FOUND("해당 계좌가 없습니다. 계좌 조회로 검색해주세요."),
    ACCOUNT_ALREADY_UNREGISTERED("해당 계좌는 이미 해지된 상태입니다."),
    BALANCE_REMAIN("계좌에 잔액이 남아있어 해지할 수 없습니다. "),
    USER_DIFFERENT("해당계좌는 다른 소유주의 계좌입니다."),
    TRANSACTION_VALUE_MINUS("거래 금액은 1이상으로 해주세요."),
    TRANSACTION_VALUE_OVER("거래금액이 잔액보다 큽니다."),
    TRANSACTION_NOT_FOUND("해당 거래가 없습니다."),
    TRANSACTION_VALUE_DIFFERENT("거래금액이 일치하지 않습니다."),
    CANNOT_CANCEL_FAILED_TRANSACTION("실패한 거래는 취소할 수 없습니다"),
    CANNOT_CANCEL_CANCEL_TRANSACTION("거래 취소는 취소할 수 없습니다."),
    TRANSACTION_ALREADY_CANCELED("이미 취소된 거래입니다.")
    ;
    private final String description;

}
