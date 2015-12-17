package net.virtela.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlRootElement
@ApiModel(value = "ErrorMessages", description = "Default Application Error Message container")
public class ErrorMessages {
	private static final long serialVersionUID = 4801642684397151922L;

	@ApiModelProperty(value = "Error Code.")
	private int code;
	private List<ErrorMessage> errors = new ArrayList<>();

	public ErrorMessages() {
		super();
	}

	public ErrorMessages(int code) {
		super();
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<ErrorMessage> getErrors() {
		return this.errors;
	}

	public void setErrors(List<ErrorMessage> errors) {
		this.errors.clear();
		this.errors.addAll(errors);
	}

	public void addErrorMessage(ErrorMessage errorMessage) {
		if (errorMessage != null) {
			this.errors.add(errorMessage);
		}
	}
}
