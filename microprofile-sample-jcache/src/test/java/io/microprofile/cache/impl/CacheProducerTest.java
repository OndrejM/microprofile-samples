package io.microprofile.cache.impl;

import io.microprofile.cache.impl.cdi.CacheProducer;
import java.io.File;
import javax.cache.Cache;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import io.microprofile.cache.api.ManagedCache;
import javax.cache.configuration.Configuration;

/**
 *
 * @author omihalyi
 */
@RunWith(Arquillian.class)
@ApplicationScoped
public class CacheProducerTest {
    
    // ======================================
    // =         Deployment methods         =
    // ======================================
    @Deployment
    public static Archive<?> archive() {
        return ShrinkWrap
                .create(WebArchive.class)
                .addClass(CacheProducer.class)
                .addClass(ManagedCache.class)
                //.addClass(CacheProducerTest.class)
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/beans.xml")),
                        "beans.xml");
    }
    
    @Inject
    @ManagedCache
    private Cache cache;
    
    @Inject
    @ManagedCache
    private Cache<String, String> stringCache;
    
    @Test
    public void testRawCacheInjected() {
        assertNotNull(cache);
    }
    
    @Test
    //@Ignore // producer does not match, need to create a producer dynamically in CDI xtension for every injection point
    public void testTypedCacheInjected() {
        assertNotNull(stringCache);
        final Configuration config = stringCache.getConfiguration(Configuration.class);
        assertNotNull(config);
    }
    
}
