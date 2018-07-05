package fr.elecomte.test.configs;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.stream.Stream;

import javax.jws.WebService;
import javax.servlet.Servlet;
import javax.xml.namespace.QName;

import org.apache.cxf.Bus;
import org.apache.cxf.databinding.DataBinding;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * <p>
 * Where the drama occurs... This auto-config uses a programatic CXF Soap service init,
 * checking WebService annotated beans. We add many features automatically to the
 * initialized endpoints. The endpoint are specified from service impl, which is a bean
 * spring provided by the context. If the custom conditional is specified in this class,
 * autowiring is not processed in services implements (even if they are spring beans). If
 * the conditional is removed, the autowiring is specified...
 * </p>
 * 
 * @author elecomte
 * @since v1.0.0
 * @version 1
 */
@Configuration
@ConditionalOnExistBeanWithAnnotation(WebService.class)
@ConditionalOnClass({ Servlet.class, JaxWsServerFactoryBean.class })
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
public class TestConditionalFailureConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestConditionalFailureConfiguration.class);

	@Autowired
	private Bus bus;

	/**
	 * Re-use the default property from CXF starter (shared with JAX-RS).
	 */
	@Value("${cxf.path}")
	private String cxfPath;

	@Bean
	public DataBinding cxfDatabinding() {
		return new JAXBDataBinding();
	}

	/**
	 * @return
	 */
	@Bean
	public ServletRegistrationBean<CXFServlet> cxfServletRegistration() {
		return new ServletRegistrationBean<>(new CXFServlet(), this.cxfPath + "*");
	}

	@Bean
	public Object initJaxWsServers(DefaultListableBeanFactory beanFactory) {

		LOGGER.info("[CONF] Begin CXF endpoint init");

		beanFactory.getBeansWithAnnotation(WebService.class).values().stream()
				.forEach(str -> {

					Class<?> type = getAnnotatedInterfaceForBean(str, WebService.class).orElseThrow(() -> new RuntimeException(
							str.getClass().getName() + " is not implementing an interface defining a Webservice : check hierarchy"));

					// Standardized uri / name, for server compliance
					String name = type.getSimpleName();
					String uri = "/" + name;

					JaxWsServerFactoryBean bean = new JaxWsServerFactoryBean();

					// Impl webservice details
					WebService ws = AnnotationUtils.findAnnotation(str.getClass(), WebService.class);

					bean.setAddress(uri);
					bean.setBus(this.bus);
					bean.setServiceClass(type);
					bean.setServiceBean(str);
					bean.setServiceName(new QName(ws.targetNamespace(), ws.serviceName() != null ? ws.serviceName() : ws.name()));
					bean.setDataBinding(cxfDatabinding());

					// Create JAX-FS server
					Server serv = bean.create();

					// And register it in spring
					beanFactory.registerSingleton(name, serv);

					LOGGER.info("[CONF] Added SOAP service {} to uri {} for Implementation {}", name, uri,
							str.getClass().getName());
				});

		return "completed";
	}

	protected static Optional<Class<?>> getAnnotatedInterfaceForBean(Object bean, Class<? extends Annotation> annotationType) {
		return Stream.of(bean.getClass().getInterfaces()).filter(t -> t.getDeclaredAnnotation(annotationType) != null).findFirst();
	}

}
