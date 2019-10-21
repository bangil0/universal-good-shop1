package com.fajar.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fajar.parameter.Routing;
import com.fajar.service.ComponentService;
import com.fajar.service.TransactionService;
import com.fajar.service.UserSessionService;

/**
 * 
 * @author fajar
 *
 */
@Controller
@RequestMapping("admin")
public class MvcAdminController {

	Logger log = LoggerFactory.getLogger(MvcAdminController.class);
	@Autowired
	private UserSessionService userService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private ComponentService componentService;
	
	public MvcAdminController() {
		log.info("-----------------MvcAdminController------------------");
	}

	@RequestMapping(value = { "/home" })
	public String index(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath()+"/account/login");
		}
		model.addAttribute("menus", componentService.getHomeMenus(request));
		model.addAttribute("contextPath",request.getContextPath());
		model.addAttribute("title", "Shop::Dashboard");
		model.addAttribute("pageUrl", "shop/home-page");
		return "BASE_PAGE";
	}
	
	@RequestMapping(value = { "/transaction/in" })
	public String incomingTransaction(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath()+"/account/login");
		}
		model.addAttribute("contextPath",request.getContextPath()); 
		model.addAttribute("title", "Shop::Supply");
		model.addAttribute("pageUrl", "shop/transaction-in-page");
		return "BASE_PAGE";
	}
	
	@RequestMapping(value = { "/transaction/out" })
	public String outTransaction(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath()+"/account/login");
		}
	
		model.addAttribute("contextPath",request.getContextPath());
		model.addAttribute("title", "Shop::Purchase");
		model.addAttribute("pageUrl", "shop/transaction-out-page");
		return "BASE_PAGE";
	}
	
	 
	
 
}