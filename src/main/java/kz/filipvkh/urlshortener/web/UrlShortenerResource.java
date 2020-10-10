package kz.filipvkh.urlshortener.web;

import kz.filipvkh.urlshortener.service.UrlService;
import kz.filipvkh.urlshortener.service.model.dto.UrlDto;
import kz.filipvkh.urlshortener.service.model.exceptions.PathIsBusyException;
import kz.filipvkh.urlshortener.service.model.exceptions.PathNotFoundException;
import kz.filipvkh.urlshortener.service.model.exceptions.UrlIsNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UrlShortenerResource {

    @Autowired
    private UrlService urlService;

    @PostMapping(value = "/shorten", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> shortenToRandomURL(@RequestBody UrlDto urlDto) throws PathIsBusyException, UrlIsNotValidException {
        return new ResponseEntity<>(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/" +
                urlService.encodeUrl(urlDto), HttpStatus.OK);
    }

    @GetMapping("/{path}")
    public ResponseEntity<String> followShortenedUrl(@PathVariable String path) throws PathNotFoundException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", urlService.getUrl(path));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

}
