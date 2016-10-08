/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.microprofile.cache.impl.cdi;

import java.lang.reflect.*;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.*;
import javax.cache.configuration.*;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import io.microprofile.cache.api.ManagedCache;

/**
 *
 * @author mike
 */
@ApplicationScoped
public class CacheProducer {

    private CacheManager cm;

    @PostConstruct
    public void init() {
        cm = Caching.getCachingProvider().getCacheManager();
    }

    @PreDestroy
    public void close() {
        cm.close();
    }

    /*
     An example of a producer for generic types. Should be generated for every type of injection point from a CDI extension. See http://jdevelopment.nl/dynamic-cdi-producers/
    */
    @Produces
    @Dependent
    @ManagedCache
    @SuppressWarnings("unchecked")
    public Cache<String, String> createStringCache(InjectionPoint ip) {
        return createCache(ip);
    }

    /**
     * Produces a Cache for injection. The cache will be created based on the values of the ManagedCache annotation.
     *
     * @param ip
     * @return
     */
    @Produces
    @Dependent
    @ManagedCache
    @SuppressWarnings("unchecked")
    public Cache createCache(InjectionPoint ip) {
        Cache result = null;

        //determine the cache name first start with the default name
        ManagedCache ncqualifier = ip.getAnnotated().getAnnotation(ManagedCache.class);
        CacheManager manager = cm;

        MetaData md = readMetaData(ncqualifier, ip);
        String cacheName = ip.getMember().getDeclaringClass().getCanonicalName() 
                + ":" + md.keyClass.getCanonicalName()
                + ":" + md.valueClass.getCanonicalName();

        // configure the cache based on the annotation and metadata
        Class keyClass = md.keyClass;
        Class valueClass = md.valueClass;
        result = manager.getCache(cacheName, keyClass, valueClass);
        if (result == null) {
            MutableConfiguration<Object, Object> config = new MutableConfiguration<>();
            config.setTypes(keyClass, valueClass);

            result = manager.createCache(cacheName, config);
        }
        return result;
    }

    private MetaData readMetaData(ManagedCache ncqualifier, InjectionPoint ip) {
        MetaData md = new MetaData();
        if (ncqualifier != null) {
            md.keyClass = ncqualifier.keyClass();
            md.valueClass = ncqualifier.valueClass();
            String qualifierName = ncqualifier.cacheName();
            if (!"".equals(qualifierName)) {
                md.cacheName = qualifierName;
            }
        }
        if (md.cacheName == null) {
            md.cacheName = ip.getMember().getDeclaringClass().getCanonicalName();
        }
        if (ManagedCache.Undefined.class.equals(md.keyClass) 
                || ManagedCache.Undefined.class.equals(md.valueClass)) {
            ParameterizedType paramType = getParamTypeFromInjectionPoint(ip);
            if (paramType != null) {
                final Type[] genericTypeArguments = paramType.getActualTypeArguments();
                final int genericTypeCount = genericTypeArguments.length;
                if (ManagedCache.Undefined.class.equals(md.keyClass) && genericTypeCount > 0) {
                    md.keyClass = (Class) genericTypeArguments[0];
                }
                if (ManagedCache.Undefined.class.equals(md.valueClass) && genericTypeCount > 1) {
                    md.valueClass = (Class) genericTypeArguments[1];
                }
            }
        }
        if (ManagedCache.Undefined.class.equals(md.keyClass)) {
            md.keyClass = Object.class;
        }
        if (ManagedCache.Undefined.class.equals(md.valueClass)) {
            md.valueClass = Object.class;
        }
        return md;
    }

    private ParameterizedType getParamTypeFromInjectionPoint(InjectionPoint ip) {
        final Member member = ip.getMember();
        ParameterizedType paramType = null;
        if (member instanceof Field) {
            final Type genericType = ((Field) member).getGenericType();
            if (genericType instanceof ParameterizedType) {
                paramType = (ParameterizedType) genericType;
            }
        }
        // TODO member is method, constr
        return paramType;
    }

    private static class MetaData {

        public Class<?> keyClass;
        public Class<?> valueClass;
        public String cacheName;
    }
}
