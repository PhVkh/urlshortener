package kz.filipvkh.urlshortener.web.handler;

import kz.filipvkh.urlshortener.service.model.exceptions.PathIsBusyException;
import kz.filipvkh.urlshortener.service.model.exceptions.PathNotFoundException;
import kz.filipvkh.urlshortener.service.model.exceptions.UrlIsNotValidException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UrlExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = PathNotFoundException.class)
    public ResponseEntity<Object> pathNotFound(final PathNotFoundException ex, final WebRequest request) {
        return super.handleExceptionInternal(ex, "данная ссылка не существует или была удалена", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = PathIsBusyException.class)
    public ResponseEntity<Object> pathIsBusy(final PathIsBusyException ex, final WebRequest request) {
        return super.handleExceptionInternal(ex, "данный адрес уже занят, попробуй другой", new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = UrlIsNotValidException.class)
    public ResponseEntity<Object> urlIsNotValid(final UrlIsNotValidException ex, final WebRequest request) {
        return super.handleExceptionInternal(ex, "введенный адрес не является ссылкой :(", new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
