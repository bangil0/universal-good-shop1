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
import com.fajar.service.UserSessionService;

@Controller 
@RequestMapping("account")
public class MvcAccountController {
	Logger log = LoggerFactory.getLogger(MvcAccountController.class);
	@Autowired
	private UserSessionService userSessionService;
	
	public MvcAccountController() {
		log.info("----------------MvcAccountController---------------");
	}

	@RequestMapping(value = { "/login" })
	public String login(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (userSessionService.hasSession(request)) {
			response.sendRedirect(request.getContextPath()+"/admin/home");
		}
		model.addAttribute("contextPath",request.getContextPath());
		return "shop/login-page";
	}
	
	@RequestMapping(value = { "/logout" })
	public String logout(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (userSessionService.hasSession(request)) {
			userSessionService.logout(request);
		}
		model.addAttribute("contextPath",request.getContextPath());
		return "shop/login-page";
	}
	
	@RequestMapping(value = { "/register" })
	public String register(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (userSessionService.hasSession(request)) {
			response.sendRedirect(request.getContextPath()+ "/admin/home");
		}
		model.addAttribute("contextPath",request.getContextPath());
		return "shop/register-page";
	}
}