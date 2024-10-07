package shop.biday.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import shop.biday.exception.auth.JwtAuthenticationException;
import shop.biday.exception.model.ErrorResponse;


@Slf4j
@ControllerAdvice
public class JwtExceptionHandlerAdvice {

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<?> handleJwtAuthenticationException(JwtAuthenticationException e) {
        log.error("[JwtAuthenticationException] e: ", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.UNAUTHORIZED,
                "UNAUTHORIZED", "JWT 인증에 실패했습니다."),
                HttpStatus.UNAUTHORIZED
        );
    }

    // 기존 Redis 예외 핸들러 (예시로 포함)
    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<?> handleRedisConnectionFailureException(RedisConnectionFailureException e) {
        log.error("[redisConnectionFailureException] e: ", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "SERVER_ERROR", "서버와 연결할 수 없습니다. 잠시 후 다시 시도해주세요."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(SerializationException.class)
    public ResponseEntity<?> handleSerializationException(SerializationException e) {
        log.error("[SerializationException] e: ", e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "SERIALIZE_ERROR", "데이터 직렬화 중 오류가 발생했습니다."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
