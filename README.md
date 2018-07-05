# Sample project for conditional error test

This is a spring-boot 2 project, with a simple JAX-WS service (implemented with CXF) and a basic auto-configuration where a custom conditional is used.

## Identified problem

In the conditional condition process **fr.elecomte.test.configs.OnSomethingCondition** two kind of bean search are available : one where the bean names are used only, one where the bean instances are loaded.

As the conditional is processed very early in spring-boot context lifecycle, the Autowired Processor may not be loaded yet when processing bean init : the initialized bean (accessed by instance search) are then initialized without autowiring processing, and because they are singleton, they are never processed with autowiring => @Autowired fields / methods are not interpreted...

This mean that you must not get beans from context in spring-boot conditions used in auto-configuration : search for bean names only (this way the bean spec is used only, and the bean is not initialized)

## Testing

Update **fr.elecomte.test.configs.OnSomethingCondition.BY_BEAN** to try search "by name" or "by bean", and run **fr.elecomte.test.TestConditionalErrorApplication**. Check logs for service control on autowired field.