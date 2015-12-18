package net.virtela.service;

import net.virtela.exception.ServiceException;
import net.virtela.model.AuthenticationRequest;
import net.virtela.model.LoginCredential;

public interface AuthService {

	public LoginCredential googleLogin(AuthenticationRequest authRequest) throws ServiceException;

}
