package com.zerobase.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zerobase.account.service.RedisTestService;

@SpringBootTest
public class AccountServiceTest {
	@Autowired
	private RedisTestService redisTestService;

	@Test
	void getLockTest() {
		String result = redisTestService.getLock();
		assertEquals("Lock success", result);
	}
}
