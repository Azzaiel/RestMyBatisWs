package net.virtela.service.impl;

import net.virtela.exception.ServiceException;
import net.virtela.model.AuthenticationRequest;
import net.virtela.model.LoginCredential;
import net.virtela.service.AuthService;

public class AuthServiceImpl implements AuthService {

	final static String GOOGLE_ENDPOINT = "https://www.google.com/accounts/o8/id";

	@Override
	public LoginCredential googleLogin(AuthenticationRequest authRequest) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
