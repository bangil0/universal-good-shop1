package com.fajar.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fajar.dto.ShopApiRequest;
import com.fajar.dto.ShopApiResponse;
import com.fajar.service.AccountService;
import com.fajar.service.TransactionService;
import com.fajar.service.UserSessionService;

@CrossOrigin
@RestController
@RequestMapping("/api/transaction")
public class TransactionApiController {
	Logger log = LoggerFactory.getLogger(TransactionApiController.class);
	@Autowired
	private UserSessionService userSessionService;
	@Autowired
	private TransactionService transactionService;

	public TransactionApiController() {
		log.info("------------------EntityApiController-----------------");
	}

	@PostMapping(value = "/supply", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ShopApiResponse addSupply(@RequestBody ShopApiRequest request, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException {
		log.info("register {}", request);
		ShopApiResponse response = transactionService.submitNew(request, httpRequest);
		return response;
	}
	
	@PostMapping(value = "/purchase", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ShopApiResponse purchase(@RequestBody ShopApiRequest request, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException {
		log.info("register {}", request);
		ShopApiResponse response = transactionService.addPurchaseTransaction(request, httpRequest);
		return response;
	}
	@PostMapping(value = "/stocks", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ShopApiResponse stockinfo(@RequestBody ShopApiRequest request, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException {
		log.info("register {}", request);
		ShopApiResponse response = transactionService.getStocks(request, false);
		return response;
	}
	
	@PostMapping(value = "/stockinfo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ShopApiResponse stockInfo(@RequestBody ShopApiRequest request, HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws IOException {
		log.info("register {}", request);
		ShopApiResponse response = transactionService.stockInfo(request);
		return response;
	}
	

}
