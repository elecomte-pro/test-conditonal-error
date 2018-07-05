package fr.elecomte.test.services.dto;

/**
 *
 * @author elecomte
 * @since v1.0.0
 * @version 1
 */
public class WhateverResponse {

	private String name;

	private String other;

	/**
	 * 
	 */
	public WhateverResponse() {
		super();
	}

	/**
	 * @param name
	 * @param other
	 */
	public WhateverResponse(String name, String other) {
		super();
		this.name = name;
		this.other = other;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the other
	 */
	public String getOther() {
		return this.other;
	}

	/**
	 * @param other
	 *            the other to set
	 */
	public void setOther(String other) {
		this.other = other;
	}

}
