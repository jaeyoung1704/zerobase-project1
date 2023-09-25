package com.zerobase.account.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.zerobase.account.dto.CheckAccount;
import com.zerobase.account.dto.CheckTransaction;
import com.zerobase.account.dto.CreateAccount;
import com.zerobase.account.dto.CreateTransaction;
import com.zerobase.account.dto.DeleteAccount;
import com.zerobase.account.dto.DeleteTransaction;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.service.AccountService;
import com.zerobase.account.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/transaction/*")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/")
    public String AccountHome() {
	return "/transaction/index";
    }

    @GetMapping("/create")
    public String createAccount() throws Exception {
	log.info("생성페이지");
	return "/transaction/create";
    }

    @PostMapping("/create")
    public String createTransaction(Model m, CreateTransaction.Request request)
	throws Exception {
	log.warn("create 접근");
	log.warn(request.getAccountNumber());
	log.warn(request.getTransactionValue() + "");
	CreateTransaction.Response response = new CreateTransaction.Response();
	try {
	    response = CreateTransaction.Response.from(transactionService
		.createTransaction(request.getUserId(), request.getAccountNumber(),
				   request.getTransactionValue()));
	}
	catch (AccountException e) {
	    log.error("에러 발생");
	    log.error(e.getErrorMsg());
	    m.addAttribute("msg", "거래 생성 실패\n" + e.getErrorCode().toString() + " : "
		+ e.getErrorMsg());
	    m.addAttribute("url", "/transactoin/");
	    return "alert";
	}
	m.addAttribute("createdTransaction", response);
	return "/transaction/createSuccess";
    }

    @GetMapping("/check")
    public String checkTransaction() throws Exception {
	log.info("조회페이지");
	return "/transaction/check";
    }

    @PostMapping("/check")
    public String checkTransaction(Model m,
				   @ModelAttribute CheckTransaction.Request request)
	throws Exception {
	log.info("계좌 조회 페이지");
	CheckTransaction.Response response = new CheckTransaction.Response();
	try {
	    response = CheckTransaction.Response.from(
						      transactionService
							  .getTransaction(request
							      .getTransactionId()));
	}
	catch (AccountException e) {
	    log.error("에러 발생");
	    log.error(e.getErrorMsg());
	    m.addAttribute("msg", "거래 조회 실패\n" + e.getErrorCode().toString() + " : "
		+ e.getErrorMsg());
	    m.addAttribute("url", "/transaction/");
	    return "alert";
	}
	m.addAttribute("transaction", response);
	return "/transaction/checkSuccess";
    }

    @GetMapping("/delete")
    public String deleteAccount() throws Exception {
	log.info("삭제페이지");
	return "/transaction/delete";
    }

    @PostMapping("/delete")
    public String delteAccount(Model m,
			       @ModelAttribute DeleteTransaction.Request request)
	throws Exception {
	log.info("계좌 삭제 페이지");
	DeleteTransaction.Response response = new DeleteTransaction.Response();
	response = DeleteTransaction.Response.from(
						   transactionService
						       .deleteTransaction(request
							   .getTransactionId(),
									  request
									      .getAccountNumber(),
									  request
									      .getTransactionValue())
	);
	m.addAttribute("canceledTransaction", response);
	return "/transaction/deleteSuccess";
    }
}
