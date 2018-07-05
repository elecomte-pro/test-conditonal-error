package fr.elecomte.test.services.impls;

import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.elecomte.test.services.SampleService;
import fr.elecomte.test.services.dto.WhateverResponse;

/**
 * @author elecomte
 * @since v1.0.0
 * @version 1
 */
@Service
@WebService(
		serviceName = "SampleService",
		portName = "SampleServicePort",
		endpointInterface = "fr.elecomte.test.services.SampleService",
		targetNamespace = "urn:SampleServiceV1",
		wsdlLocation = "SampleService.wsdl")
public class StandardSampleService implements SampleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StandardSampleService.class);

	@Autowired
	private TestComponent comp;

	/**
	 * /**
	 * 
	 * @param name
	 * @return
	 * @see fr.elecomte.test.services.SampleService#findSomething(java.lang.String)
	 */
	@Override
	public WhateverResponse findSomething(String name) {

		if (this.comp == null) {
			LOGGER.warn("[WEBSERVICE IMPL] CALL SERVICE IMPL => component is MISSING :'(");
			return new WhateverResponse(name, "missing");
		}

		LOGGER.info("[WEBSERVICE IMPL] CALL SERVICE IMPL => component is PRESENT :-)");
		this.comp.testInner();
		return new WhateverResponse(name, "present");
	}

}
