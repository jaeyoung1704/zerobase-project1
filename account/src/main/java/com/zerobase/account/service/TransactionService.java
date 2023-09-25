package com.zerobase.account.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.zerobase.account.domain.Account;
import com.zerobase.account.domain.Transaction;
import com.zerobase.account.domain.Users;
import com.zerobase.account.dto.AccountDto;
import com.zerobase.account.dto.TransactionDto;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.repo.AccountRepo;
import com.zerobase.account.repo.TransactionRepo;
import com.zerobase.account.repo.UserRepo;
import com.zerobase.account.type.AccountStatus;
import com.zerobase.account.type.ErrorCode;
import com.zerobase.account.type.TransactionResult;
import com.zerobase.account.type.TransactionType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final TransactionRepo transactionRepo;

    @Transactional
    public TransactionDto createTransaction(Long userId, String accountNumber,
					    Long transactionValue)
	throws AccountException {
	// 에러코드는 최상위에러만 기록
	ErrorCode errcd = null;
	// 해당 유저 없음 예외
	if (!userRepo.findById(userId).isPresent())
	    errcd = ErrorCode.USER_NOT_FOUND;
	// 해당 계좌 없음 예외
	Account account = Account.builder().accountNumber(accountNumber).build();
	if (accountRepo.findByAccountNumber(accountNumber).isPresent())
	    account = accountRepo.findByAccountNumber(accountNumber).get();
	else if (errcd == null)
	    errcd = ErrorCode.ACCOUNT_NOT_FOUND;

	// 소유주 다름 예외
	if (errcd == null && account.getUsers().getId() != userId)
	    errcd = ErrorCode.USER_DIFFERENT;
	// 이미 해지된 계좌 예외
	if (errcd == null
	    && account.getAccountStatus() == AccountStatus.UNREGISTERED)
	    errcd = ErrorCode.ACCOUNT_ALREADY_UNREGISTERED;
	// 거래금액 음수 예외
	if (transactionValue <= 0 && errcd == null)
	    errcd = ErrorCode.TRANSACTION_VALUE_MINUS;
	// 거래금액 잔액부족 예외
	if (errcd == null && transactionValue > account.getBalance())
	    errcd = ErrorCode.TRANSACTION_VALUE_OVER;
	// 에러코드 있으면 실패
	TransactionResult result =
	    errcd == null ? TransactionResult.SUCCEED : TransactionResult.FALIED;
	// 실패 아닐때만 잔액 삭감
	if (errcd == null)
	    account.setBalance(account.getBalance() - transactionValue);
	accountRepo.save(account);
	// 테이블에 추가하고 추가한 계좌정보를 dto로 변형해서 반환
	Transaction transaction = Transaction.builder()
	    .userId(userId)
	    .accountNumber(accountNumber)
	    .transactionType(TransactionType.USE)
	    .transactionResult(result)
	    .errorCode(errcd)
	    .transactionValue(transactionValue)
	    .occuredAt(LocalDateTime.now())
	    .build();
	return TransactionDto.fromEntity(
					 transactionRepo.save(transaction));
    }

    @Transactional
    public TransactionDto getTransaction(Long transactionId) {
	// 해당거래 없음 예외
	Transaction transaction = transactionRepo.findById(transactionId)
	    .orElseThrow(() -> new AccountException(
		ErrorCode.TRANSACTION_NOT_FOUND));

	// DTO로 변환후 반환
	return TransactionDto.fromEntity(transaction);
    }

    @Transactional
    public TransactionDto deleteTransaction(Long transactionId, String accountNumber,
					    Long transactionValue)
	throws AccountException {
	// 에러코드는 최상위에러만 기록
	ErrorCode errcd = null;
	// 해당 거래 없음 예외
	Transaction transaction = Transaction.builder().id(transactionId).build();
	if (transactionRepo.findById(transactionId).isPresent())
	    transaction = transactionRepo.findById(transactionId).get();
	else
	    errcd = ErrorCode.TRANSACTION_NOT_FOUND;
	// 실패한 거래 예외
	if (transaction.getTransactionResult() == TransactionResult.FALIED)
	    errcd = ErrorCode.CANNOT_CANCEL_FAILED_TRANSACTION;
	// 거래 취소 취소 예외
	if (transaction.getTransactionType() == TransactionType.CANCEL)
	    errcd = ErrorCode.CANNOT_CANCEL_CANCEL_TRANSACTION;
	// 이미 취소된 거래 예외
	if (transaction.getTransactionResult() == TransactionResult.CANCELED)
	    errcd = ErrorCode.TRANSACTION_ALREADY_CANCELED;
	// 해당 계좌 없음 예외
	Account account = Account.builder().accountNumber(accountNumber).build();
	if (accountRepo.findByAccountNumber(accountNumber).isPresent())
	    account = accountRepo.findByAccountNumber(accountNumber).get();
	else if (errcd == null)
	    errcd = ErrorCode.ACCOUNT_NOT_FOUND;

	// 이미 해지된 계좌 예외
	if (errcd == null
	    && account.getAccountStatus() == AccountStatus.UNREGISTERED)
	    errcd = ErrorCode.ACCOUNT_ALREADY_UNREGISTERED;
	// 거래금액 불일치 예외
	if (transactionValue != transaction.getTransactionValue()) {
	    log.error(transactionValue + "");
	    log.error(transaction.getTransactionValue() + "");
	    errcd = ErrorCode.TRANSACTION_VALUE_DIFFERENT;
	}
	// 에러코드 있으면 실패
	TransactionResult result =
	    errcd == null ? TransactionResult.SUCCEED : TransactionResult.FALIED;
	// 실패 아닐때만 잔액 돌려줌
	if (errcd == null)
	    account.setBalance(account.getBalance() + transactionValue);
	accountRepo.save(account);
	// 테이블에 추가하고 추가한 계좌정보를 dto로 변형해서 반환
	transaction = Transaction.builder()
	    .userId(account.getUsers().getId())
	    .accountNumber(accountNumber)
	    .transactionType(TransactionType.CANCEL)
	    .transactionResult(result)
	    .errorCode(errcd)
	    .transactionValue(transactionValue)
	    .occuredAt(LocalDateTime.now())
	    .build();
	return TransactionDto.fromEntity(
					 transactionRepo.save(transaction));
    }

}
