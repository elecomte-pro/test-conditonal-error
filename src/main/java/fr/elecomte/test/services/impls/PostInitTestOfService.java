package fr.elecomte.test.services.impls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import fr.elecomte.test.services.SampleService;

/**
 * @author elecomte
 * @since v0.0.8
 * @version 1
 */
@Component
public class PostInitTestOfService {

	@Autowired
	private SampleService service;

	/**
	 * <p>
	 * Fired on spring app context refresh event (once ctxt is initialized)
	 * </p>
	 * 
	 * @param event
	 *            fired at context completed load
	 */
	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {
		this.service.findSomething("back-test");
	}
}
