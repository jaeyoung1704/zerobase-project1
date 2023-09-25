package com.zerobase.account.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zerobase.account.domain.Account;
import com.zerobase.account.dto.*;
import com.zerobase.account.exception.AccountException;
import com.zerobase.account.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/account/*")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/")
    public String AccountHome() {
	return "/account/index";
    }

    @GetMapping("/create")
    public String createAccount() throws Exception {
	log.info("생성페이지");
	return "/account/create";
    }

    @PostMapping("/create")
    public String createAccount(Model m, CreateAccount.Request request)
	throws Exception {
	CreateAccount.Response response = new CreateAccount.Response();
	try {
	    response = CreateAccount.Response.from(accountService
		.createAccount(request.getUserId(), request.getInitialBalance()));
	}
	catch (AccountException e) {
	    log.error("에러 발생");
	    log.error(e.getErrorMsg());
	    m.addAttribute("msg", "계좌 추가 실패\n" + e.getErrorCode().toString() + " : "
		+ e.getErrorMsg());
	    m.addAttribute("url", "/account/");
	    return "alert";
	}
	m.addAttribute("msg", "계좌를 성공적으로 추가했습니다");
	m.addAttribute("createdAccount", response);
	return "/account/createSuccess";
    }

    @GetMapping("/check")
    public String checkAccount() throws Exception {
	log.info("조회페이지");
	return "/account/check";
    }

    @PostMapping("/check")
    public String checkAccount(Model m, @ModelAttribute CheckAccount.Request request)
	throws Exception {
	log.info("계좌 조회 페이지");
	CheckAccount.Response response = new CheckAccount.Response();
	try {
	    response = new CheckAccount.Response(
		accountService.getAccount(request.getUserId()));
	}
	catch (AccountException e) {
	    log.error("에러 발생");
	    log.error(e.getErrorMsg());
	    m.addAttribute("msg", "계좌 조회 실패\n" + e.getErrorCode().toString() + " : "
		+ e.getErrorMsg());
	    m.addAttribute("url", "/account/");
	    return "alert";
	}
	m.addAttribute("accountList", response);
	return "/account/checkSuccess";
    }

    @GetMapping("/delete")
    public String deleteAccount() throws Exception {
	log.info("삭제페이지");
	return "/account/delete";
    }

    @PostMapping("/delete")
    public String delteAccount(Model m,
			       @ModelAttribute DeleteAccount.Request request)
	throws Exception {
	log.info("계좌 삭제 페이지");
	DeleteAccount.Response response = new DeleteAccount.Response();
	try {
	    response = DeleteAccount.Response.from(
						   accountService
						       .deleteAccount(request
							   .getUserId(),
								      request
									  .getAccountId()));
	}
	catch (AccountException e) {
	    log.error("에러 발생");
	    log.error(e.getErrorMsg());
	    m.addAttribute("msg", "계좌 삭제 실패\n" + e.getErrorCode().toString() + " : "
		+ e.getErrorMsg());
	    m.addAttribute("url", "/account/");
	    return "alert";
	}
	m.addAttribute("unregisteredAccount", response);
	return "/account/deleteSuccess";
    }
}
