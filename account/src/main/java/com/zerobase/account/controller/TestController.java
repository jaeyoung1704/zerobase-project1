package com.zerobase.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.zerobase.account.service.AccountService;
import com.zerobase.account.service.RedisTestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {
	private final RedisTestService redisTestService;

	@GetMapping("/get-lock")
	public String getLock() {
		return redisTestService.getLock();

	}
}
