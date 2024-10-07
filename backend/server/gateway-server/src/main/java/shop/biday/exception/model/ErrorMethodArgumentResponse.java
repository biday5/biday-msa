package shop.biday.exception.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMethodArgumentResponse {

    private HttpStatus status;
    private String field;
    private String message;
}
