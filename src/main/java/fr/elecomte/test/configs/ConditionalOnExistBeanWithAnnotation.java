package fr.elecomte.test.configs;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Conditional;

/**
 * <p>
 * A custom conditional on whatever you need. If used, this conditional changes the
 * behavior on bean autowired processing in auto-config
 * </p>
 * 
 * @author elecomte
 * @since v1.0.0
 * @version 1
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnSomethingCondition.class)
public @interface ConditionalOnExistBeanWithAnnotation {

	/**
	 * <p>
	 * The annotation which needs to be applied to a component, somewhere in the
	 * application
	 * </p>
	 */
	Class<? extends Annotation> value();
}
