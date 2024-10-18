package shop.biday.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import shop.biday.exception.model.ErrorMethodArgumentResponse;

import java.util.List;

@Slf4j
@RestControllerAdvice(basePackages = "shop.biday.controller")
public class ArgumentExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
    ) {
        log.error("handleMethodArgumentNotValid ex: ", ex.getMessage());
        BindingResult bindingResult = ex.getBindingResult();
        List<ErrorMethodArgumentResponse> responses = bindingResult.getFieldErrors()
                .stream()
                .map(error -> new ErrorMethodArgumentResponse(HttpStatus.BAD_REQUEST,
                        error.getField(), error.getDefaultMessage()))
                .toList();

        return new ResponseEntity<>(responses, HttpStatus.BAD_REQUEST);
    }
}
