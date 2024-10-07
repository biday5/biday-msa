package shop.biday.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefundRequest(
        @Schema(description = "취소 이유", example = "고객 변심")
        @NotBlank(message = "결제 취소 이유는 필수입니다.")
        String cancelReason) {
}
