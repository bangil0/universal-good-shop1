package com.fajar.service;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fajar.controller.BaseController;
import com.fajar.dto.RegistryModel;
import com.fajar.dto.SessionData;

@Service
public class RegistryService {

	public static final String PAGE_REQUEST = "page_req_id";

	public static final String PAGE_REQUEST_ID = "requestId";

	public static final String JSESSSIONID = "JSESSIONID";

	@Autowired
	private Registry registry;

	@PostConstruct
	public void init() {
		LogProxyFactory.setLoggers(this);
		set(UserSessionService.SESSION_DATA, new SessionData());
	}

	/**
	 * get remote object
	 * 
	 * @param <T>
	 * @param key
	 * @return
	 */
	public <T> T getModel(String key) {
		try {
			T object = (T) registry.lookup(key);
			System.out.println("==registry model: " + object);
			return object;
		} catch (RemoteException | NotBoundException e) {
			System.out.println("key not bound");
			return null;
		} catch (Exception ex) {
			System.out.println("Unexpected error");
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * set registry remote object
	 * 
	 * @param key
	 * @param registryModel
	 * @return
	 */
	public boolean set(String key, Remote registryModel) {
		try {
			if (getModel(key) == null) {
				registry.bind(key, registryModel);
			} else {
				registry.rebind(key, registryModel);
			}
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * unbind remote object
	 * 
	 * @param key
	 * @return
	 */
	public boolean unbind(String key) {
		try {
			registry.unbind(key);
			return true;
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * register new page request to request list
	 * 
	 * @param cookie
	 * @return
	 */
	public String addPageRequest(String cookie) {
		String pageRequestId = UUID.randomUUID().toString();
		if (getModel(PAGE_REQUEST) != null) {
			RegistryModel model = getModel(PAGE_REQUEST);
			model.getTokens().put(pageRequestId, cookie);
			if (set(PAGE_REQUEST, model)) {
				return pageRequestId;
			}

		} else {
			RegistryModel model = RegistryModel.builder().tokens(new HashMap<>()).build();
			model.getTokens().put(pageRequestId, cookie);
			if (set(PAGE_REQUEST, model)) {
				return pageRequestId;
			}
		}
		return null;

	}

	/**
	 * check page request against cookie jsessionID
	 * 
	 * @param req
	 * @return
	 */
	public boolean validatePageRequest(HttpServletRequest req) {
		System.out.println("Will validate page request");
		try {
			RegistryModel model = getModel(PAGE_REQUEST);

			if (null == model) {
				return false;
			}

			Cookie jsessionCookie = BaseController.getCookie(JSESSSIONID, req.getCookies());
			String pageRequestId = req.getHeader(PAGE_REQUEST_ID);
			boolean exist = model.getTokens().get(pageRequestId) != null;
			if (exist) {
				String reuqestIdValue = (String) model.getTokens().get(pageRequestId);

				System.out.println(" . . . . . Request ID value: " + reuqestIdValue + " vs JSessionId: "
						+ jsessionCookie.getValue());

				return reuqestIdValue.equals(jsessionCookie.getValue());
			} else {
				System.out.println("x x x x Request ID not found x x x x");
			}

			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
