package kz.filipvkh.urlshortener.scheduler;

import kz.filipvkh.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class OldUrlCollector {
    
    @Autowired
    private UrlRepository urlRepository;

    @Scheduled(fixedDelay = 24 * 3600 * 1000)
    public void deleteOldPaths() {
        LocalDate latestPossibleDate = LocalDate.now().minusDays(31);
        urlRepository.findAllByCreatedBefore(latestPossibleDate).forEach(urlRepository::delete);
    }
}
