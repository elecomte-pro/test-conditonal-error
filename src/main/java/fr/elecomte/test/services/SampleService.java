package fr.elecomte.test.services;

import javax.jws.WebParam;
import javax.jws.WebService;

import fr.elecomte.test.services.dto.WhateverResponse;

/**
 * @author elecomte
 * @since v1.0.0
 * @version 1
 */
@WebService(name = "SampleService", targetNamespace = "urn:SampleServiceV1")
public interface SampleService {

	/**
	 * @param name
	 * @return
	 */
	WhateverResponse findSomething(@WebParam(name = "name") String name);
}
