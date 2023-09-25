package com.zerobase.account.service;

import com.zerobase.account.domain.Account;
import com.zerobase.account.domain.Users;
import com.zerobase.account.dto.AccountDto;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.repo.AccountRepo;
import com.zerobase.account.repo.UserRepo;
import com.zerobase.account.type.AccountStatus;
import com.zerobase.account.type.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.zerobase.account.type.AccountStatus.IN_USE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepo accountRepo;
    private final UserRepo userRepo;

    /**
     * 사용자가 있는지 조회, 계좌 번호 생성 계좌 저장, 정보 반환
     */
    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance)
	throws AccountException {
	// 해당 유저 없음 예외
	Users user = userRepo.findById(userId)
	    .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
	// 초기 잔액 음수 예외
	if (initialBalance < 0)
	    throw new AccountException(ErrorCode.INITIAL_BALANCE);
	// 유저 계좌수 조회
	int size = 0;
	Optional<List<Account>> accounts = accountRepo.findAllByUsers(user);
	if (accounts.isPresent())
	    size = accounts.get().size();
	// 계좌수가 이미 10개면 예외 반환
	if (size >= 10)
	    throw new AccountException(ErrorCode.ACCOUNT_OVER_10);
	Random rd = new Random();

	// 랜덤 10자리 생성을 중복이 아닌숫자가 나올때까지 반복
	String newAccountNumber = "";
	while (true) {
	    // 0~899999999중 랜덤 +1000000000
	    long randomNumber = (long) (rd.nextDouble() * 9000000000l) + 1000000000l;
	    newAccountNumber = randomNumber + "";
	    // 계좌번호로 조회한 결과가 없는경우에만 반복해제
	    if (!accountRepo.findById(randomNumber).isPresent())
		break;
	}
	// 테이블에 추가하고 추가한 계좌정보를 dto로 변형해서 반환
	return AccountDto.fromEntity(
				     accountRepo.save(Account.builder()
					 .users(user)
					 .accountStatus(IN_USE)
					 .accountNumber(newAccountNumber)
					 .balance(initialBalance)
					 .registeredAt(LocalDateTime.now())
					 .build())
	);
    }

    /**
     * 유저있는지 조회, 해당유저아이디로 계좌 조회, 계좌목록 반환
     */
    @Transactional
    public List<AccountDto> getAccount(Long userId) {
	// 유저 없으면 예외 반환
	Users user = userRepo.findById(userId)
	    .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));

	// 유저 계좌 조회
	int size = 0;
	Optional<List<Account>> accounts = accountRepo.findAllByUsers(user);
	// 없으면 빈 리스트 반환
	if (!accounts.isPresent())
	    return new ArrayList<AccountDto>();
	// 있으면 해당 리스트 반환
	return accounts.get()
	    .stream()
	    .map(e -> AccountDto.fromEntity(e))
	    .collect(Collectors.toList());
    }

    /**
     * 유저 아이디 있는지 조회, 계좌번호 있는지 조회, 삭제후 삭제정보 반환
     */
    @Transactional
    public AccountDto deleteAccount(Long userId, Long accountId)
	throws AccountException {
	// 해당 유저 없음 예외
	Users user = userRepo.findById(userId)
	    .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
	// 해당 계좌 없음 예외
	Account account = accountRepo.findByAccountNumber(accountId.toString())
	    .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
	// 소유주 다름 예외
	if (account.getUsers().getId() != userId)
	    throw new AccountException(ErrorCode.USER_DIFFERENT);
	// 이미 해지된 계좌 예외
	if (account.getAccountStatus() == AccountStatus.UNREGISTERED)
	    throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
	// 잔액 있음 예외
	if (account.getBalance() > 0)
	    throw new AccountException(ErrorCode.BALANCE_REMAIN);

	// 예외 모두 통과시 해지처리후 다시 repo에 저장
	account.setAccountStatus(AccountStatus.UNREGISTERED);
	account.setUnRegisteredAt(LocalDateTime.now());
	accountRepo.save(account);
	return AccountDto.fromEntity(account);
    }
}
