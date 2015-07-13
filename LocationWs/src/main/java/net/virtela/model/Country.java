package net.virtela.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Country implements Serializable {

	private static final long serialVersionUID = -1519511480373569500L;

	private Long id;
	private String name;
	private String code;
	private String lastModBy;
	private Date lastModDate;

	public Country() {
		super();
	}

	public Country(Long id, String name, String code, String lastModBy,
			Date lastModDate) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.lastModBy = lastModBy;
		this.lastModDate = lastModDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLastModBy() {
		return lastModBy;
	}

	public void setLastModBy(String lastModBy) {
		this.lastModBy = lastModBy;
	}

	public Date getLastModDate() {
		return lastModDate;
	}

	public void setLastModDate(Date lastModDate) {
		this.lastModDate = lastModDate;
	}

}
