package net.virtela.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlRootElement
public class ErrorMessage {

	@ApiModelProperty(value = "Error Code.")
	private int code;
	@ApiModelProperty(value = "Error Message.")
	private String message;

	public ErrorMessage() {
		super();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
