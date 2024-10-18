package shop.biday.exception.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import shop.biday.exception.PaymentException;
import shop.biday.exception.model.ErrorResponse;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class PaymentExceptionHandlerAdvice {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException e)
            throws JsonProcessingException {
        log.error("[handleHttpClientErrorException] e: ", e.getMessage());
        String responseBody = e.getResponseBodyAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        String code = jsonNode.path("code").asText();
        String message = jsonNode.path("message").asText();

        return new ResponseEntity<>(new ErrorResponse(status, code, message), status);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handlePaymentException(PaymentException e) {
        log.error("[handlePaymentException] e: ", e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(e.getStatus(), e.getCode(), e.getMessage()),
                e.getStatus());
    }
}
