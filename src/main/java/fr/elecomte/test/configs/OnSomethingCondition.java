package fr.elecomte.test.configs;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * <p>
 * Some condition on whatever you need. The outcome is based on spring bean search in
 * context.
 * </p>
 * 
 * @author elecomte
 * @since v1.0.0
 * @version 1
 */
public class OnSomethingCondition extends SpringBootCondition {

	private final static boolean BY_BEAN = false;

	/**
	 * @param context
	 * @param metadata
	 * @return
	 * @see org.springframework.boot.autoconfigure.condition.SpringBootCondition#getMatchOutcome(org.springframework.context.annotation.ConditionContext,
	 *      org.springframework.core.type.AnnotatedTypeMetadata)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {

		// Get conditionnal annot properties
		Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnExistBeanWithAnnotation.class.getName());
		Object value = attributes.get("value");

		// Default case : on specified annotation ...
		Class<? extends Annotation> annotType = (Class<? extends Annotation>) value;

		// ... search for annotated bean in CTXT
		Collection<String> beanNames = BY_BEAN
				? searchBeansWithAnnotationSupportRepeatable(context.getBeanFactory(), annotType)
				: getBeanNamesForAnnotationSupportRepeatable(context.getBeanFactory(), annotType, true);

		// Get any kind of result. Here fixed to matched for testing
		return new ConditionOutcome(beanNames.size() > 0, "Match condition");
	}

	/**
	 * @param ctxt
	 * @param annotationType
	 * @return
	 */
	public static Collection<String> searchBeansWithAnnotationSupportRepeatable(ListableBeanFactory ctxt,
			Class<? extends Annotation> annotationType) {

		Map<String, Object> beans = ctxt.getBeansWithAnnotation(annotationType);

		// Search also repeatable
		Repeatable rep = AnnotationUtils.findAnnotation(annotationType, Repeatable.class);

		if (rep != null) {
			beans.putAll(ctxt.getBeansWithAnnotation(rep.value()));
		}

		return beans.keySet();

	}

	/**
	 * <p>
	 * Search for beans by annotation, once no class def found processed, with support on
	 * Context hierarchy
	 * </p>
	 * <p>
	 * Based on standard
	 * <code>org.springframework.boot.autoconfigure.condition.OnBeanCondition.getMatchingBeans(ConditionContext, BeanSearchSpec)</code>
	 * process used in <code>@ConditionalOnBean</code>
	 * </p>
	 * 
	 * @param beanFactory
	 * @param annotType
	 * @param considerHierarchy
	 * @return
	 */
	private static Collection<String> getBeanNamesForAnnotationSupportRepeatable(
			ConfigurableListableBeanFactory beanFactory,
			Class<? extends Annotation> annotType,
			boolean considerHierarchy) {

		Collection<String> result = new ArrayList<>();

		result.addAll(Arrays.asList(beanFactory.getBeanNamesForAnnotation(annotType)));

		if (considerHierarchy) {
			if (beanFactory.getParentBeanFactory() instanceof ConfigurableListableBeanFactory) {
				Collection<String> parentResult = getBeanNamesForAnnotationSupportRepeatable(
						(ConfigurableListableBeanFactory) beanFactory.getParentBeanFactory(), annotType, true);

				for (String beanName : parentResult) {
					if (!result.contains(beanName) && !beanFactory.containsLocalBean(beanName)) {
						result.add(beanName);
					}
				}
			}
		}

		// Search also repeatable
		Repeatable rep = AnnotationUtils.findAnnotation(annotType, Repeatable.class);

		if (rep != null) {
			result.addAll(getBeanNamesForAnnotationSupportRepeatable(beanFactory, rep.value(), considerHierarchy));
		}

		return result;
	}
}