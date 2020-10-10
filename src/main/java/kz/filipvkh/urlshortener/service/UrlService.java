package kz.filipvkh.urlshortener.service;

import kz.filipvkh.urlshortener.domain.UrlEntity;
import kz.filipvkh.urlshortener.repository.UrlRepository;
import kz.filipvkh.urlshortener.service.model.dto.UrlDto;
import kz.filipvkh.urlshortener.service.model.exceptions.PathIsBusyException;
import kz.filipvkh.urlshortener.service.model.exceptions.PathNotFoundException;
import kz.filipvkh.urlshortener.service.model.exceptions.UrlIsNotValidException;
import kz.filipvkh.urlshortener.service.model.mapper.UrlMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDate;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UrlMapper urlMapper;

    private boolean isPathBusy(String path) {
        return urlRepository.findById(path).isPresent();
    }

    private void checkIfUrlValid(String url) throws UrlIsNotValidException {
        try {
            URL u = new URL(url);
            u.toURI();
        } catch (Exception e) {
            throw new UrlIsNotValidException();
        }
    }

    private String getRandomNonexistentPath() {
        String path = RandomStringUtils.randomAlphanumeric(6);
        if (isPathBusy(path)) {
            return getRandomNonexistentPath();
        } else {
            return path;
        }
    }

    public String encodeUrl(UrlDto urlDto) throws PathIsBusyException, UrlIsNotValidException {
        checkIfUrlValid(urlDto.getUrl());

        if (urlDto.getPath() != null) {
            if (isPathBusy(urlDto.getPath())) {
                throw new PathIsBusyException();
            }
        } else {
            urlDto.setPath(getRandomNonexistentPath());
        }

        UrlEntity urlEntity = urlMapper.convert(urlDto);
        urlEntity.setCreated(LocalDate.now());
        urlRepository.save(urlEntity);
        return urlDto.getPath();
    }

    public String getUrl(String path) throws PathNotFoundException {
        return urlRepository.findById(path).map(UrlEntity::getUrl).orElseThrow(PathNotFoundException::new);
    }
}
