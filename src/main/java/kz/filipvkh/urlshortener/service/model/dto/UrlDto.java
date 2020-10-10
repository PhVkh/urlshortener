package kz.filipvkh.urlshortener.service.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UrlDto {

    private String path;
    private String url;
    private LocalDate created;
}
