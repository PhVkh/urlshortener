package kz.filipvkh.urlshortener.service.model.mapper;

import kz.filipvkh.urlshortener.domain.UrlEntity;
import kz.filipvkh.urlshortener.service.model.dto.UrlDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UrlMapper {

    UrlEntity convert(UrlDto urlDto);
}
