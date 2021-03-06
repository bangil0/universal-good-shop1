package com.fajar.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.entity.BaseEntity;
import com.fajar.entity.Category;
import com.fajar.entity.Menu;
import com.fajar.entity.User;
import com.fajar.repository.CategoryRepository;
import com.fajar.repository.MenuRepository;
import com.fajar.util.EntityUtil;

@Service
public class ComponentService {
	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private UserSessionService userSessionService;

	public List<Menu> getDashboardMenus(HttpServletRequest request) {
		List<Menu> menus = menuRepository.findByPageStartsWith("HOME");
		List<BaseEntity> entities = new ArrayList<BaseEntity>();
		menus = validateAccess(userSessionService.getUserFromSession(request), menus);
		for (Menu menu : menus) {
			menu.setUrl(request.getContextPath() + menu.getUrl());
			entities.add(menu);
		}
		return EntityUtil.validateDefaultValue(entities);
	}

	public List<Menu> getManagementMenus(HttpServletRequest request) {
		List<Menu> menus = menuRepository.findByPageStartsWith("MNGMNT");
		List<BaseEntity> entities = new ArrayList<BaseEntity>();

		menus = validateAccess(userSessionService.getUserFromSession(request), menus);
		for (Menu menu : menus) {
			menu.setUrl(request.getContextPath() + menu.getUrl());
			entities.add(menu);
		}
		return EntityUtil.validateDefaultValue(entities);
	}

	private boolean hasAccess(User user, String menuAccess) {
		boolean hasAccess = false;
		for (String userAccess : user.getRole().getAccess().split(",")) {
			if (userAccess.equals(menuAccess)) {
				hasAccess = true;
				break;
			}
		}

		return hasAccess;
	}

	private List<Menu> validateAccess(User user, List<Menu> menus) {
		List<Menu> newMenus = new ArrayList<>();
		for (Menu menu : menus) {
			String[] menuAccess = menu.getPage().split("-");
			if (menuAccess.length <= 1) {
				newMenus.add(menu);
				continue;
			} else if (hasAccess(user, menuAccess[1])) {
				newMenus.add(menu);
				continue;
			}

		}
		return newMenus;
	}

	public List<Menu> getTransactionMenus(HttpServletRequest request) {
		List<Menu> menus 			= menuRepository.findByPageStartsWith("TRX");
		menus = validateAccess(userSessionService.getUserFromSession(request), menus);
		
		List<BaseEntity> entities 	= new ArrayList<BaseEntity>();
		
		for (Menu menu : menus) {
			menu.setUrl(request.getContextPath() + menu.getUrl());
			entities.add(menu);
		}
		return EntityUtil.validateDefaultValue(entities);
	}

	public List<Menu> getPublicMenus(HttpServletRequest request) {
		List<Menu> menus 			= menuRepository.findByPageStartsWith("PUBLIC");
		List<BaseEntity> entities 	= new ArrayList<BaseEntity>();
		
		for (Menu menu : menus) {
			menu.setUrl(request.getContextPath() + menu.getUrl());
			entities.add(menu);
		}
		
		return EntityUtil.validateDefaultValue(entities);
	}

	public List<Category> getAllCategories() {
		return categoryRepository.findByDeletedFalse();
	}

	public void checkAccess(User user, String url) throws Exception {
		Menu menu = menuRepository.findTop1ByUrl(url);
		if (menu == null) {
			throw new Exception("Not Found");

		}
		String[] menuAccess = menu.getPage().split("-");
		if (menuAccess.length > 1) {
			String access 			= menuAccess[1];
			String[] userAccesses 	= user.getRole().getAccess().split(",");
			boolean hasAccess 		= hasAccess(user, access);
			if (!hasAccess) {
				throw new Exception("Has No Access");
			}
		}

	}

}
