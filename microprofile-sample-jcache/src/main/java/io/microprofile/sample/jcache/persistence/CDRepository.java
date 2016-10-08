package io.microprofile.sample.jcache.persistence;

import io.microprofile.sample.jcache.model.CD;
import io.microprofile.sample.jcache.utils.QLogger;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.cache.annotation.*;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@CacheDefaults(cacheName = "cds")
public class CDRepository {
    
    @Inject
    @QLogger
    private Logger logger;
    
    @CacheResult //results are cached forever for the same limit argument
    public List<CD> getTopCDs(@CacheKey Integer limit) {
        return getRandomNumbers(limit).stream()
                .map(CD::new)
                .collect(Collectors.toList());
    }

    private List<Integer> getRandomNumbers(int limit) {
        final List<Integer> randomNumbers = new ArrayList<>();
        final Random r = new Random();
        for (int i = 0; i < limit; i++) {
            randomNumbers.add(r.nextInt(100) + 1101);
        }

        logger.info("Top CD ids are " + randomNumbers);

        return randomNumbers;
    }
}
