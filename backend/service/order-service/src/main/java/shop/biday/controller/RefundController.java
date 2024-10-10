package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.dto.RefundRequest;
import shop.biday.service.RefundService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refunds")
@Tag(name = "refunds", description = "Refund Controller")
public class RefundController {

    private final RefundService refundService;

    @Operation(summary = "결제 취소", description = "결제 취소를 합니다.")
    @Parameters({
            @Parameter(name = "id", description = "결제 ID", example = "1"),
            @Parameter(examples = {
                    @ExampleObject(name = "exampleRefundRequest", value = """ 
                        { 
                            "cancelReason" : "취소 사유"
                        } 
                    """)})
    })
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping
    public ResponseEntity<?> cancelPayment(@RequestParam("id") Long id,
                                           @RequestBody @Validated RefundRequest cancelRequest) {
        log.info("cancelPayment id: {}, paymentCancelRequest: {}", id, cancelRequest);
        return new ResponseEntity<>(refundService.cancel(id, cancelRequest), HttpStatus.CREATED);
    }
}
