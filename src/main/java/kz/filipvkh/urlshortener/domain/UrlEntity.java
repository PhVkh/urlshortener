package kz.filipvkh.urlshortener.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "URLS")
public class UrlEntity {

    @Id
    private String path;
    private String url;
    private LocalDate created;
}
