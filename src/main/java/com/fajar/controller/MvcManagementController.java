package com.fajar.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fajar.config.EntityProperty;
import com.fajar.entity.Unit;
import com.fajar.entity.UserRole;
import com.fajar.parameter.Routing;
import com.fajar.service.EntityService;
import com.fajar.service.UserSessionService;
import com.fajar.service.WebAppConfiguration;
import com.fajar.util.EntityUtil;
import com.fajar.util.MVCUtil;

/**
 * 
 * @author fajar
 *
 */
@Controller
@RequestMapping("management")
public class MvcManagementController {

	Logger log = LoggerFactory.getLogger(MvcManagementController.class);
	@Autowired
	private UserSessionService userService;
	@Autowired
	private EntityService entityService;
	@Autowired
	private WebAppConfiguration webAppConfiguration;
	
	private static String basePage;

	public MvcManagementController() {
		log.info("-----------------MvcManagementController------------------");
	}

	@PostConstruct
	private void init() {
		basePage =webAppConfiguration.getBasePage();
	}
	
	@RequestMapping(value = { "/unit" })
	public String unit(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath() + "/account/login");
		}
		EntityProperty entityProperty = EntityUtil.createEntityProperty("Unit", null);
		model.addAttribute("entityProperty", entityProperty);
		model  =constructCommonModel(request, model, "Unit");
		return basePage;
	}

	@RequestMapping(value = { "/supplier" })
	public String supplier(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath() + "/account/login");
		}
		EntityProperty entityProperty = EntityUtil.createEntityProperty("Supplier", null);
		model.addAttribute("entityProperty", entityProperty);
		model  =constructCommonModel(request, model, "Supplier");
		return basePage;
	}
	
	@RequestMapping(value = { "/customer" })
	public String customer(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath() + "/account/login");
		}
		EntityProperty entityProperty = EntityUtil.createEntityProperty("Customer", null);
		model.addAttribute("entityProperty", entityProperty);
		model  =constructCommonModel(request, model, "Customer");
		return basePage;
	}
	
	@RequestMapping(value = { "/product" })
	public String product(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath() + "/account/login");
		}
		 EntityProperty entityProperty = EntityUtil.createEntityProperty("Product", null);
		model.addAttribute("entityProperty", entityProperty);
		model  =constructCommonModel(request, model, "Product");
		return basePage;
	}
	
	@RequestMapping(value = { "/category" })
	public String category(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath() + "/account/login");
		}
		 EntityProperty entityProperty = EntityUtil.createEntityProperty("Category", null);
		model.addAttribute("entityProperty", entityProperty);
		model  =constructCommonModel(request, model, "Category");
		return basePage;
	}
	
	@RequestMapping(value = { "/transaction" })
	public String transaction(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath() + "/account/login");
		}
		 EntityProperty entityProperty = EntityUtil.createEntityProperty("Transaction", null);
		model.addAttribute("entityProperty", entityProperty);
		model  =constructCommonModel(request, model, "Transaction");
		model.addAttribute("editable",false);
		return basePage;
	}
	
	@RequestMapping(value = { "/user" })
	public String user(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath() + "/account/login");
		}
		HashMap<String, Object> listObject= new HashMap<>();
		List<UserRole> roles =entityService.getAllUserRole();
		listObject.put("userRole", roles);
		EntityProperty entityProperty = EntityUtil.createEntityProperty("User", listObject);
		model.addAttribute("entityProperty", entityProperty);
		log.info("============ENTITY PROPERTY: "+entityProperty);
		model  =constructCommonModel(request, model, "User");
		return basePage;
	}
	
	@RequestMapping(value = { "/menu" })
	public String menu(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!userService.hasSession(request)) {
			response.sendRedirect(request.getContextPath() + "/account/login");
		}
		EntityProperty entityProperty = EntityUtil.createEntityProperty("Menu", null);
		model.addAttribute("entityProperty", entityProperty);
		log.info("============ENTITY PROPERTY: "+entityProperty);
		model  =constructCommonModel(request, model, "Menu");
		return basePage;
	}
	
	private Model constructCommonModel(HttpServletRequest request, Model model, String title) {
		model.addAttribute("contextPath",request.getContextPath());
		String host = MVCUtil.getHost(request);
		model.addAttribute("host", host);
		model.addAttribute("imagePath","WebAsset/Shop1/Images");
		model.addAttribute("title", "Management::"+title);
		model.addAttribute("editable",true);
		
		model.addAttribute("pageUrl", "shop/entity-management-page");
		return model;
	}


}
