package fr.elecomte.test.services.impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Sub component for validation of injection processing in SOAP context
 * </p>
 * 
 * @author elecomte
 * @since v1.0.0
 * @version 1
 */
@Component
public class TestComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestComponent.class);

	public void testInner() {
		LOGGER.info("[INNER COMP] Testing inner component call in SOAP service");
	}

}
