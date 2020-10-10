package kz.filipvkh.urlshortener.repository;

import kz.filipvkh.urlshortener.domain.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UrlRepository extends JpaRepository<UrlEntity, String> {

   List<UrlEntity> findAllByCreatedBefore(LocalDate dateOfDeath);
}
