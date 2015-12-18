package net.virtela.exception;

import net.virtela.model.ErrorMessages;

public class ServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6072614693742076991L;
	private ErrorMessages errorMessages;

	public ServiceException() {
		super();
	}

	public ServiceException(ErrorMessages errorMessages) {
		super();
		this.errorMessages = errorMessages;
	}

	public void setErrorMessages(ErrorMessages errorMessages) {
		this.errorMessages = errorMessages;
	}

	public ErrorMessages getErrorMessages() {
		if (this.errorMessages != null) {
			return this.errorMessages;
		}
		return new ErrorMessages();
	}

	public int getHttpErrorCode() {
		return errorMessages.getCode();
	}

}
