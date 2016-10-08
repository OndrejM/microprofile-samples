/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.microprofile.cache.impl.cdi;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.enterprise.context.ApplicationScoped;

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
}
