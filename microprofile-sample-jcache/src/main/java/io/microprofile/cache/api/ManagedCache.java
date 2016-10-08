package io.microprofile.cache.api;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Qualifier to inject container managed javax.cache.Cache implementation.
 */
@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface ManagedCache {
    /**
     * The name of the Cache in the Cache Manager
     * @return 
     */
    @Nonbinding
    String cacheName() default "";
    
    /**
     * The class of the Cache Keys
     * @return 
     */
    @Nonbinding
    Class keyClass() default Undefined.class;
    
    /**
     * The class of the cache values
     * @return 
     */
    @Nonbinding
    Class valueClass() default Undefined.class;
    
    public static class Undefined {};
}
