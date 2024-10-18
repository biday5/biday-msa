package shop.biday.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentException extends RuntimeException {

    private HttpStatus status;
    private String code;
    private String message;
}
