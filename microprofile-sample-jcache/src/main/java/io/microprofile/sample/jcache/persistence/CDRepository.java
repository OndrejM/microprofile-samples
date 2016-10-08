package io.microprofile.sample.jcache.persistence;

import io.microprofile.sample.jcache.model.CD;
import io.microprofile.sample.jcache.utils.QLogger;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CDRepository {
    
    @Inject
    @QLogger
    private Logger logger;
    
    public List<CD> getTopCDs() {
        return getRandomNumbers().stream()
                .map(CD::new)
                .collect(Collectors.toList());
    }

    private List<Integer> getRandomNumbers() {
        final List<Integer> randomNumbers = new ArrayList<>();
        final Random r = new Random();
        randomNumbers.add(r.nextInt(100) + 1101);
        randomNumbers.add(r.nextInt(100) + 1101);
        randomNumbers.add(r.nextInt(100) + 1101);
        randomNumbers.add(r.nextInt(100) + 1101);
        randomNumbers.add(r.nextInt(100) + 1101);

        logger.info("Top CD ids are " + randomNumbers);

        return randomNumbers;
    }
}
