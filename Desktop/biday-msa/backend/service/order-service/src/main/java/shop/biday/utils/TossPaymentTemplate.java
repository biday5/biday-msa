package shop.biday.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import shop.biday.exception.PaymentException;
import shop.biday.model.domain.PaymentModel;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class TossPaymentTemplate {

    @Value("${payments.toss.secret.key}")
    private String widgetSecretKey;

    private final RestTemplate restTemplate;

    public TossPaymentTemplate(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public PaymentModel getPayment(ResponseEntity<PaymentModel> response) {
        PaymentModel paymentModel = response.getBody();
        if (paymentModel == null) {
            throw new PaymentException(HttpStatus.BAD_REQUEST, "INVALID_REQUEST", "잘못된 요청입니다.");
        }
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            throw new PaymentException(
                    HttpStatus.valueOf(response.getStatusCode().value()),
                    paymentModel.getCode(),
                    paymentModel.getMessage());
        }

        return paymentModel;
    }

    public ResponseEntity<PaymentModel> exchangeGetMethod(String uri) {
        return restTemplate.exchange(
                getUri(uri),
                HttpMethod.GET,
                new HttpEntity<>(getHttpHeaders()),
                PaymentModel.class);
    }

    public <T> ResponseEntity<PaymentModel> exchangePostMethod(String uri, T request) {
        return restTemplate.exchange(
                getUri(uri),
                HttpMethod.POST,
                new HttpEntity<>(request, getHttpHeaders()),
                PaymentModel.class);
    }

    public HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(getAuthorizationHeader());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private URI getUri(String path) {
        return UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("api.tosspayments.com")
                .path("/v1/payments/" + path)
                .build()
                .encode()
                .toUri();
    }

    public String getAuthorizationHeader() {
        String authorization = widgetSecretKey + ":";
        return new String(Base64.getEncoder().encode(authorization.getBytes(StandardCharsets.UTF_8)));
    }
}
